import 'dart:convert';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/request.dart';
import 'package:flutter_application/widget_util.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class MyBookings extends StatefulWidget {
  const MyBookings({super.key});

  @override
  State<MyBookings> createState() => _MyBookingsState();
}

class _MyBookingsState extends State<MyBookings> {
  List<bool> hovering = List<bool>.filled(3, false);
  final List<String> monthAbbreviation = [
    'Jan',
    'Feb',
    'Mar',
    'Apr',
    'May',
    'Jun',
    'Jul',
    'Aug',
    'Sept',
    'Oct',
    'Nov',
    'Dec'
  ];
  late Future _appointmentsFuture;
  @override
  void initState() {
    super.initState();
    _appointmentsFuture = Request.getAppointmentsByUID();
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width;
    double screenHeight = MediaQuery.of(context).size.height;
    bool isMobile = screenWidth < 800;
    return Scaffold(
      appBar: WidgetUtil.buildNavBar(
          context,
          screenWidth,
          Theme.of(context).colorScheme.onPrimary,
          Theme.of(context).colorScheme.primaryContainer,
          Theme.of(context).colorScheme.primary),
      body: FutureBuilder(
        future: _appointmentsFuture,
        builder: (BuildContext context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text("Error: ${snapshot.error}"));
          } else if (snapshot.data.length == 0) {
            return const Center(child: Text("You have no appointments"));
          } else {
            return Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              if (!isMobile)
                Expanded(
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: SizedBox(
                      height: 550,
                      child: Image.asset(
                        'assets/LeftHalfTooth.png',
                      ),
                    ),
                  ),
                ),
              Center(
                child: Container(
                  height: screenHeight * 0.87,
                  width: !isMobile ? screenWidth * 0.4 : screenWidth * 0.9,
                  decoration: BoxDecoration(
                    boxShadow: [
                      BoxShadow(
                        color: Colors.black.withOpacity(0.2),
                        spreadRadius: 5,
                        blurRadius: 7,
                        offset: const Offset(0, 10),
                      ),
                    ],
                    borderRadius: BorderRadius.circular(20),
                    color: Colors.white,
                    border: Border.all(
                      color: Colors.black,
                      width: 0.3,
                    ),
                  ),
                  child: SingleChildScrollView(
                      child: Column(
                    children: buildAppointmentCards(snapshot.data),
                  )),
                ),
              ),
              if (!isMobile)
                Expanded(
                  child: SizedBox(
                    height: 550,
                    child: Align(
                      alignment: Alignment.centerRight,
                      child: Image.asset(
                        'assets/RightHalfTooth.png',
                      ),
                    ),
                  ),
                )
            ]);
          }
        },
      ),
    );
  }

  List<Widget> buildAppointmentCards(future) {
    List<Widget> cards = [];
    cards.add(const SizedBox(height: 50));

    for (int i = 0; i < future.length; i++) {
      DentistAppointment appointment = future[i];
      cards.add(createTimeSlot(appointment.eventName, appointment.from,
          appointment.id.toString(), context, appointment.id));
      cards.add(const SizedBox(height: 50));
    }
    return cards;
  }

  Container createTimeSlot(String address, DateTime date, String bookingId,
      BuildContext context, int apointmentId) {
    DateTime to = date.add(const Duration(minutes: 30));
    int monthIndex = date.month - 1;

    return Container(
      height: 200,
      width: 400,
      decoration: const BoxDecoration(
        image: DecorationImage(
            image: AssetImage('assets/MyBookingsContainer.png'),
            fit: BoxFit.fill),
      ),
      child: Padding(
          padding: const EdgeInsets.only(left: 25, top: 15),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  WidgetUtil.createText(Colors.grey, 15,
                      AppLocalizations.of(context)!.my_bookings_address),
                  const SizedBox(height: 5),
                  WidgetUtil.createText(Colors.white, 18, address),
                  const SizedBox(height: 5),
                  WidgetUtil.createText(Colors.grey, 15,
                      AppLocalizations.of(context)!.my_bookings_time),
                  const SizedBox(height: 5),
                  WidgetUtil.createText(Colors.white, 18,
                      "${date.hour}:${date.minute} - ${to.hour}:${to.minute}"),
                  const SizedBox(height: 5),
                  WidgetUtil.createText(Colors.grey, 15,
                      AppLocalizations.of(context)!.my_bookings_bookingid),
                  const SizedBox(height: 5),
                  WidgetUtil.createText(Colors.white, 18, bookingId),
                ],
              ),
              Padding(
                padding: const EdgeInsets.only(right: 15, top: 10),
                child: Align(
                  alignment: Alignment.centerRight,
                  child: Column(
                    children: [
                      WidgetUtil.createText(
                          Theme.of(context).colorScheme.onPrimary,
                          22,
                          monthAbbreviation[monthIndex]),
                      WidgetUtil.createText(
                          Theme.of(context).colorScheme.shadow,
                          30,
                          "${date.day}"),
                      WidgetUtil.createText(
                          Theme.of(context).colorScheme.onPrimary,
                          22,
                          "${date.year}"),
                      const SizedBox(
                        height: 10,
                      ),
                      Align(
                        alignment: Alignment.center,
                        child: ElevatedButton(
                            style: ButtonStyle(
                              backgroundColor: MaterialStateProperty.all<Color>(
                                  Theme.of(context).colorScheme.error),
                              fixedSize: MaterialStateProperty.all<Size>(
                                  const Size(85, 20)),
                            ),
                            onPressed: () =>
                                {cancelApointment(apointmentId.toString())},
                            child: WidgetUtil.createText(
                                Theme.of(context).colorScheme.onPrimary,
                                10,
                                AppLocalizations.of(context)!
                                    .my_bookings_cancel)),
                      )
                    ],
                  ),
                ),
              )
            ],
          )),
    );
  }

  void cancelApointment(String bookingID) async {
    FirebaseAuth auth = FirebaseAuth.instance;
    User? user = auth.currentUser;

    if (true) {
      var payload = {
        "patientId": user!.uid,
        "bookingId": bookingID,
      };
      String json = jsonEncode(payload);
      WidgetUtil.proccessARequest(
          context,
          "Cancelled apointment",
          "Your booking was successfully cancelled",
          (json) => Request.cancelBookingRequest(json),
          json);
    }
  }
}
