import 'dart:convert';
import 'dart:math';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/Bookings/Bookings.dart';
import 'package:flutter_application/Map/map.dart';

class WidgetUtil {
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
    double scale = context == null ? 1 : textScaleFactor(context);
    return Text(
      text,
      overflow: TextOverflow.ellipsis,
      textScaleFactor: scale,
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
}
