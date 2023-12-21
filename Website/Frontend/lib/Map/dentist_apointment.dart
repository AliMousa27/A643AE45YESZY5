import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:mapbox_gl/mapbox_gl.dart';
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
  DentistAppointment(
      this.from, this.to, this.background, this.eventName, this.id);

  /// Event name which is equivalent to subject property of [Appointment].

  /// From which is equivalent to start time property of [Appointment].
  DateTime from;

  /// To which is equivalent to end time property of [Appointment].
  DateTime to;

  /// Background which is equivalent to color property of [Appointment].
  Color background;
  int id;

  String eventName;
  factory DentistAppointment.fromJson(Map<String, dynamic> json) {
    DateTime from;

    if (json["date_and_time"] != null) {
      from = DateTime.parse(json["date_and_time"]);
    } else {
      from = DateTime.parse(json["timeslot"]["date_and_time"]);
    }
    DateTime to = from.add(const Duration(hours: 2));
    late String eventName;
    //if the office is not null then we assign the event name to the office address
    try {
      if (json["timeslot"]["office"]["address"] != null) {
        eventName = json["timeslot"]["office"]["address"];
      } else {
        eventName = "${from.hour} : ${from.minute}";
      }
    } catch (e) {
      //otherwise we access a null field then the attribute doesnt exist and we just take it as the time
      eventName = "${from.hour} : ${from.minute}";
    }

    int id = json["id"];
    return DentistAppointment(from, to, const Color(0xFFEEC4B8), eventName, id);
  }
  Map<String, dynamic> toJson() {
    return {
      'from': from.toIso8601String(),
      'to': to.toIso8601String(),
      'background': background.value.toString(),
      'eventName': eventName,
    };
  }
}

//class to parse json. Used to get dentist office data to display appointments at an office
//wheter they are booked or not
class DentistOffice {
  String address;
  String name;
  List<DentistAppointment> timeSlots;
  LatLng location;

  DentistOffice(this.address, this.name, this.timeSlots, this.location);

  factory DentistOffice.fromJson(Map<String, dynamic> json) {
    List<DentistAppointment> timeSlots = [];
    for (var slot in json["timeslots"]) {
      timeSlots.add(DentistAppointment.fromJson(slot["timeslot"]));
    }
    return DentistOffice(
      json["office"]["address"],
      json["office"]["name"],
      timeSlots,
      LatLng(json["office"]["latitude"], json["office"]["longitude"]),
    );
  }
}
