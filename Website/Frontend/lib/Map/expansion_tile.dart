import 'dart:convert';

import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/request.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';
import '../widget_util.dart';
import 'dentist_apointment.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

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
        "dateOfBirth": "2000-12-12"
      };
      var payload = {"patient": patient, "timeslotId": selectedAppointmentId};
      String json = jsonEncode(payload);
      WidgetUtil.proccessARequest(
          context,
          "Booking successful",
          "Your booking was successfully completed",
          "Booking failed",
          "Your booking was not completed. Try again later",
          (json) => Request.sendBookingRequest(json),
          json);
    }
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
                AppLocalizations.of(context)!.authentication_confirmButton,
                context,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
