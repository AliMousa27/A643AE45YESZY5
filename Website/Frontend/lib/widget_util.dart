import 'dart:math';
import 'package:flutter/material.dart';
import 'package:flutter_application/Bookings/bookings.dart';
import 'package:flutter_application/Map/map.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_application/dental_records_page.dart';
import 'package:flutter_application/initial_page.dart';
import 'package:flutter_application/setting.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class WidgetUtil {
  static List<bool> hovering = List<bool>.filled(5, false);

  static ElevatedButton createBTN(BuildContext context, Color textColor,
      Size btnSize, Color btnColor, VoidCallback onPressed, String text) {
    return ElevatedButton(
        //assign the button to a passed in callback function
        onPressed: onPressed,
        style: ButtonStyle(
            fixedSize: MaterialStateProperty.all<Size>(btnSize),
            shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(30.0),
            )),
            backgroundColor: MaterialStateProperty.all<Color>(btnColor)),
        child: createText(textColor, 23, text));
  }

  static double textScaleFactor(BuildContext context,
      {double maxTextScaleFactor = 1}) {
    final width = MediaQuery.of(context).size.width;
    double val = (width / 1400) * maxTextScaleFactor;
    return max(0.7, min(val, maxTextScaleFactor));
  }

  static Text createText(Color textColor, double fontSize, String text,
      [BuildContext? context]) {
    return Text(
      text,
      overflow: TextOverflow.ellipsis,
      style: TextStyle(
        color: textColor,
        fontSize: fontSize,
        fontFamily: "suezone",
      ),
    );
  }

//used to book and cancel apointment to load and await a response from the server
  static void proccessARequest(
      BuildContext context,
      String titleMessage,
      String subtitleMessage,
      Future<bool> Function(String json) requestFunction,
      String json) async {
    late BuildContext dialogContext;
    showDialog(
      context: context,
      barrierDismissible: false,
      builder: (BuildContext context) {
        dialogContext = context;

        return const Dialog(
          child: Padding(
            padding: EdgeInsets.all(20.0),
            child: Row(
              mainAxisSize: MainAxisSize.min,
              children: [
                CircularProgressIndicator(),
                SizedBox(width: 30),
                Text("Processing..."),
              ],
            ),
          ),
        );
      },
    );

    bool success = await requestFunction(json);
    if (context.mounted) Navigator.pop(dialogContext);

    if (success && context.mounted) {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return showSuccessOrFailure(
              "Booking successful",
              "Your booking was successfully completed",
              const Icon(Icons.check, color: Colors.green),
              success,
              context);
        },
      );
    } else if (context.mounted) {
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return showSuccessOrFailure(
              "Booking failed",
              "Your booking was not completed, please try again later",
              const Icon(Icons.error, color: Colors.red),
              success,
              context);
        },
      );
    }
  }

  static AlertDialog showSuccessOrFailure(String titleMessage,
      String subtitleMessage, Icon icon, bool success, BuildContext context) {
    return AlertDialog(
      title: Text(titleMessage),
      content: Row(
        children: [
          icon,
          Text(subtitleMessage),
        ],
      ),
      actions: <Widget>[
        Center(
          child: TextButton(
            child: const Text('OK'),
            onPressed: () {
              Navigator.of(context).pop();
              //reload the page to reflect that the booking is not there
              //logic: request back from server to get timeslots -> timeslot wont be shown since its booked
              if (success) {
                //current page
                var pageName = ModalRoute.of(context)!.settings.name;
                var page = pageName == "/mappage"
                    ? const MapPage()
                    : const MyBookings();
                Navigator.pushReplacement(
                    context, MaterialPageRoute(builder: (context) => page));
              }
            },
          ),
        ),
      ],
    );
  }

  static AppBar buildNavBar(BuildContext context, double screenWidth,
      Color textColor, Color btnColor, Color btnHoverColor) {
    bool isDesktop = screenWidth > 668;
    return AppBar(
      backgroundColor: Theme.of(context).colorScheme.primary,
      leading: Padding(
        padding: const EdgeInsets.only(left: 8.0),
        child: Image.asset(
          'assets/FullTooth.png',
        ),
      ),
      title: SizedBox(
        width: 200,
        child: Image.asset(
          'assets/ToothTrek.png',
          fit: BoxFit.scaleDown,
        ),
      ),
      actions: isDesktop
          ? buildNavbarActions(
              screenWidth,
              Theme.of(context).colorScheme.onPrimary,
              Theme.of(context).colorScheme.primaryContainer,
              Theme.of(context).colorScheme.primary,
              context)
          : [
              buildMobileNavBar(
                  screenWidth, textColor, btnColor, btnHoverColor, context),
            ],
    );
  }

  static Widget buildMobileNavBar(
    double screenWidth,
    Color textColor,
    Color btnColor,
    Color btnHoverColor,
    BuildContext context,
  ) {
    return PopupMenuButton<int>(
      icon: Icon(Icons.menu, color: textColor),
      onSelected: (int index) {
        navigateBasedOnIndex(index, context);
      },
      itemBuilder: (BuildContext context) {
        List<PopupMenuEntry<int>> items = [];
        List<String> actionPrompts = [
          AppLocalizations.of(context)!.navbar_my_bookings,
          AppLocalizations.of(context)!.navbar_map,
          AppLocalizations.of(context)!.navbar_settings,
          AppLocalizations.of(context)!.navbar_logout,
          AppLocalizations.of(context)!.navbar_dental_records
        ];
        for (int i = 0; i < actionPrompts.length; i++) {
          items.add(
            PopupMenuItem<int>(
              value: i,
              child: Text(actionPrompts[i]),
            ),
          );
        }
        return items;
      },
    );
  }

  static List<Widget> buildNavbarActions(
    double screenWidth,
    Color textColor,
    Color btnColor,
    Color btnHoverColor,
    BuildContext context,
  ) {
    List<Widget> actions = [];

    List<String> actionPrompts = [
      AppLocalizations.of(context)!.navbar_my_bookings,
      AppLocalizations.of(context)!.navbar_map,
      AppLocalizations.of(context)!.navbar_settings,
      AppLocalizations.of(context)!.navbar_logout,
      AppLocalizations.of(context)!.navbar_dental_records
    ];
    for (int i = 0; i < actionPrompts.length; i++) {
      actions.add(
        ElevatedButton(
          style: ButtonStyle(
            backgroundColor: MaterialStateProperty.resolveWith<Color>(
              (Set<MaterialState> states) {
                if (states.contains(MaterialState.hovered)) {
                  return Colors.white.withOpacity(0.8);
                }
                return hovering[i] ? btnHoverColor : btnColor;
              },
            ),
            elevation: MaterialStateProperty.resolveWith<double>(
              (Set<MaterialState> states) {
                if (states.contains(MaterialState.hovered)) return 10;
                return 2;
              },
            ),
          ),
          onPressed: () {
            navigateBasedOnIndex(i, context);
          },
          child: Text(
            actionPrompts[i],
            style: TextStyle(color: textColor, fontSize: 14),
          ),
        ),
      );
      actions.add(SizedBox(width: screenWidth * 0.02));
    }
    actions.add(SizedBox(width: screenWidth * 0.04));

    return actions;
  }

  static void navigateBasedOnIndex(int i, BuildContext context) {
    switch (i) {
      case 0:
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context) => const MyBookings()));
        break;
      case 1:
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context) => const MapPage()));
        break;
      case 2:
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context) => const SettingPage()));
        break;
      case 3:
        FirebaseAuth.instance.signOut();
        Navigator.of(context)
            .push(MaterialPageRoute(builder: (context) => const InitialPage()));
        break;
      case 4:
        Navigator.of(context).push(
            MaterialPageRoute(builder: (context) => const DentalRecordsPage()));
        break;
    }
  }
}
