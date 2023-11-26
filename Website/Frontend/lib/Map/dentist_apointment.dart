import 'dart:ui';

import 'package:syncfusion_flutter_calendar/calendar.dart';

class DentistAppointmentDataSource extends CalendarDataSource {
  /// Creates a DentistAppointment data source, which used to set the appointment
  /// collection to the calendar
  DentistAppointmentDataSource(List<DentistAppointment> source) {
    appointments = source;
  }

  @override
  DateTime getStartTime(int index) {
    return _getDentistAppointmentData(index).from;
  }

  @override
  DateTime getEndTime(int index) {
    return _getDentistAppointmentData(index).to;
  }

  @override
  Color getColor(int index) {
    return _getDentistAppointmentData(index).background;
  }

  @override
  String getSubject(int index) {
    return _getDentistAppointmentData(index).eventName;
  }

  DentistAppointment _getDentistAppointmentData(int index) {
    final dynamic appointment = appointments![index];
    late final DentistAppointment dentistAppointmentData;
    if (appointment is DentistAppointment) {
      dentistAppointmentData = appointment;
    }

    return dentistAppointmentData;
  }
}

class DentistAppointment {
  /// Creates a DentistAppointment class with required details.
  DentistAppointment(this.from, this.to, this.background, this.eventName);

  /// Event name which is equivalent to subject property of [Appointment].

  /// From which is equivalent to start time property of [Appointment].
  DateTime from;

  /// To which is equivalent to end time property of [Appointment].
  DateTime to;

  /// Background which is equivalent to color property of [Appointment].
  Color background;

  String eventName;
}
