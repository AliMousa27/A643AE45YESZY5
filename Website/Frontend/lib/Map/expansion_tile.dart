import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/Map/map.dart';
import 'package:flutter_application/request.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';
import '../widget_util.dart';
import 'dentist_apointment.dart';

class CustomExpansionTile extends StatefulWidget {
  final bool expanded;
  final DentistOffice office;
  final ScrollController scrollController;
  final int index;
  CustomExpansionTile({
    Key? key,
    required this.expanded,
    required this.office,
    required this.scrollController,
    required this.index,
  }) : super(key: key);

  @override
  CustomExpansionTileState createState() => CustomExpansionTileState();
}

class CustomExpansionTileState extends State<CustomExpansionTile> {
  bool isExpanded = false;
  final tileKey = GlobalKey();
  late int selectedAppointmentId;

  final ExpansionTileController expansionTileController =
      ExpansionTileController();

  @override
  void initState() {
    super.initState();
    isExpanded = widget.expanded;
  }

  @override
  Widget build(BuildContext context) {
    return createListCalendars(context, widget.office);
  }

  void onDateSelected(CalendarTapDetails calendarTapDetails) {
    var appointmentIndex = calendarTapDetails.appointments!.first;
    selectedAppointmentId = appointmentIndex.id;
  }

  void bookApointment() async {
    FirebaseAuth auth = FirebaseAuth.instance;
    User? user = auth.currentUser;

    if (user != null) {
      var patient = {
        "id": user.uid,
        "name": user.displayName,
        "email": user.email,
        "dateOfBirth": "bogus date"
      };
      var payload = {"patient": patient, "timeSlotId": selectedAppointmentId};
      String json = jsonEncode(payload);

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

      bool success = await Request.sendBookingRequest();
      if (context.mounted) Navigator.pop(dialogContext);

      if (success && context.mounted) {
        showDialog(
          context: context,
          builder: (BuildContext context) {
            return showSuc(
                "Booking successful",
                "Your booking was successfully completed",
                const Icon(Icons.check, color: Colors.green),
                success);
          },
        );
      } else if (context.mounted) {
        showDialog(
          context: context,
          builder: (BuildContext context) {
            return showSuc(
                "Booking failed",
                "Your booking was not completed, please try again later",
                const Icon(Icons.error, color: Colors.red),
                success);
          },
        );
      }
    }
  }

  AlertDialog showSuc(
      String titleMessage, String subtitleMessage, Icon icon, bool success) {
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
                Navigator.of(context).pushReplacement(
                    MaterialPageRoute(builder: (context) => const MapPage()));
              }
            },
          ),
        ),
      ],
    );
  }

  void expandTile() {
    setState(() {
      expansionTileController.expand();
    });
  }

  Padding createListCalendars(BuildContext context, DentistOffice office) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16),
      //wrapped with theme to remove annoying needless borders
      child: Theme(
        data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
        child: ExpansionTile(
          key: tileKey,
          title: WidgetUtil.createText(
            Theme.of(context).colorScheme.onPrimaryContainer,
            20,
            office.name,
            context,
          ),
          controller: expansionTileController,
          subtitle: WidgetUtil.createText(
            Theme.of(context).colorScheme.onPrimary,
            20,
            office.address,
            context,
          ),
          children: [
            SfCalendar(
              onTap: (calendarTapDetails) {
                onDateSelected(calendarTapDetails);
              },
              appointmentBuilder: (context, calendarAppointmentDetails) =>
                  Container(
                decoration: BoxDecoration(
                    color: calendarAppointmentDetails
                        .appointments.first.background,
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withOpacity(0.2),
                        blurRadius: 20,
                        spreadRadius: 2,
                        offset: const Offset(0, 10),
                      )
                    ]),
                child: Center(
                  child: Text(
                      calendarAppointmentDetails.appointments.first.eventName,
                      style: TextStyle(
                          fontFamily: "balsamiq",
                          color: Theme.of(context).colorScheme.background)),
                ),
              ),
              cellEndPadding: 0,
              dataSource: DentistAppointmentDataSource(office.timeSlots),
              allowAppointmentResize: false,
              allowDragAndDrop: false,
              showCurrentTimeIndicator: false,
              monthViewSettings: const MonthViewSettings(
                  appointmentDisplayMode:
                      MonthAppointmentDisplayMode.appointment),
              view: CalendarView.week,
              headerStyle: CalendarHeaderStyle(
                textAlign: TextAlign.center,
                textStyle: TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.w500,
                  color: Theme.of(context).colorScheme.onPrimary,
                ),
              ),
            ),
            SizedBox(height: MediaQuery.of(context).size.height * 0.02),
            TextButton(
              style: TextButton.styleFrom(
                shadowColor: Colors.black,
                backgroundColor:
                    Theme.of(context).colorScheme.secondaryContainer,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
                padding:
                    const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
              ),
              onPressed: bookApointment,
              child: WidgetUtil.createText(
                Theme.of(context).colorScheme.surface,
                20,
                "Confirm",
                context,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
