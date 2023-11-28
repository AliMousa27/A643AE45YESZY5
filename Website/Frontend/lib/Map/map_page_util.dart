import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';

import '../widget_util.dart';
import 'dentist_apointment.dart';

class MapUtil {
  static Widget buildCircleAvatar(
      String assetName, bool resizeProfilePic, double screenWidth) {
    return CircleAvatar(
      backgroundColor: Colors.transparent,
      radius: resizeProfilePic ? screenWidth * 0.05 : 30,
      backgroundImage: AssetImage(assetName),
    );
  }

  static Widget createDentistOfficesText(
      BuildContext context, double screenWidth) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        SizedBox(width: screenWidth * 0.02),
        WidgetUtil.createText(
          Theme.of(context).colorScheme.onSurface,
          20,
          "Dentist offices: ",
          context,
        ),
        SizedBox(width: screenWidth * 0.01),
        Expanded(
          child: WidgetUtil.createText(
            Theme.of(context).colorScheme.onSurface,
            20,
            "45",
            context,
          ),
        ),
      ],
    );
  }

  static Widget buildSearchWidget() {
    return SearchAnchor(
      builder: (BuildContext context, SearchController controller) {
        return SearchBar(
          controller: controller,
          onTap: () {
            //controller.openView();
          },
          onChanged: (_) {
            //controller.openView();
          },
          leading: const Icon(Icons.search),
        );
      },
      suggestionsBuilder: (context, pattern) {
        return [];
      },
    );
  }

  static Padding createListCalendars(
      BuildContext context, office, onDateSelected, bookApoinment) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 16),
      //wrapped with theme to remove annoying needless borders
      child: Theme(
        data: Theme.of(context).copyWith(dividerColor: Colors.transparent),
        child: ExpansionTile(
          title: WidgetUtil.createText(
            Theme.of(context).colorScheme.onPrimaryContainer,
            20,
            office.name,
            context,
          ),
          //TODO change to the actual address or whatever we will be using
          subtitle: WidgetUtil.createText(
            Theme.of(context).colorScheme.onPrimary,
            20,
            office.address,
            context,
          ),
          children: [
            SfCalendar(
              onTap: (calendarTapDetails) {
                onDateSelected(calendarTapDetails.date);
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
              onPressed: bookApoinment,
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
