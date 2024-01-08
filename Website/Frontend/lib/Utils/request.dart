import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/Records/records.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

import 'package:logging/logging.dart';

class Request {
  static final Logger _logger = Logger('Requests');

  static Future<bool> sendBookingRequest(String body) async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', 'bookings/${user!.uid}');
      return http
          .post(url, headers: {"Content-Type": "application/json"}, body: body)
          .then((response) {
        Map<String, dynamic> data = jsonDecode(response.body);
        return data["status"] == "success";
      }).catchError((onError) {
        return false;
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return false;
    }
  }

  //json contains the booking id and patient id
  //so endpoint will be delete/bookings/patientID/bookingID
  static Future<bool> cancelBookingRequest(String json) async {
    try {
      String userId = jsonDecode(json)["patientId"];
      String bookingId = jsonDecode(json)["bookingId"];
      var url = Uri.http('127.0.0.1:3001', 'bookings/$userId/$bookingId');
      return http.delete(url,
          headers: {"Content-Type": "application/json"}).then((response) {
        Map<String, dynamic> data = jsonDecode(response.body);
        return data["status"] == "success";
      }).catchError((onError) {
        return false;
      });
    } catch (error) {
      return false;
    }
  }

  static Future<List<DentistOffice>> getOffices() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', 'offices/${user!.uid}');
      return http
          .get(url, headers: {"Accept": "application/json"}).then((response) {
        var data = jsonDecode(response.body);

        data = data["content"];
        List<DentistOffice> offices = [];
        for (var office in data) {
          offices.add(DentistOffice.fromJson(office));
        }
        return offices;
      });
    } catch (error) {
      _logger.warning("caught error which is $error");

      return [];
    }
  }

  static Future<bool> setNotificationPreference(bool value) async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', 'patients/${user!.uid}');
      return http
          .patch(url,
              headers: {"Content-Type": "application/json"},
              body: jsonEncode({"id": user.uid, "notified": value.toString()}))
          .then((response) {
        var data = jsonDecode(response.body);
        return data["status"] == "success";
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return false;
    }
  }

  static Future<bool> getNotificationPreference() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', '/patients/${user!.uid}');
      return http.get(url).then((response) {
        var data = jsonDecode(response.body);
        return data["content"]["notified"];
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return false;
    }
  }

  static Future<bool> createPatient() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', '/patients/${user!.uid}');
      return http
          .post(url,
              headers: {"Content-Type": "application/json"},
              body: jsonEncode({
                "id": user.uid,
                "name": user.displayName,
                "email": user.email
              }))
          .then((response) {
        var data = jsonDecode(response.body);
        return data["status"] == "success";
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return false;
    }
  }

  static Future<List<DentistAppointment>> getAppointmentsByUID() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', 'bookings/${user!.uid}');

      return http.get(url).then((response) {
        var data = jsonDecode(response.body);
        data = data["content"];
        List<DentistAppointment> appointments = [];
        for (var appointment in data) {
          appointments.add(DentistAppointment.fromJson(appointment));
        }
        return appointments;
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return [];
    }
  }

  static Future<List<Records>> getRecordsbyPatientId() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', 'records/${user!.uid}');

      return http
          .get(url, headers: {"Accept": "application/json"}).then((response) {
        var data = jsonDecode(response.body);
        data = data["content"];
        List<Records> records = [];
        for (var record in data) {
          records.add(Records.fromJson(record));
        }
        return records;
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return [];
    }
  }

  // get patient name
  static Future<String> getPatientName() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3001', '/patients/${user!.uid}');
      return http.get(url).then((response) {
        var data = jsonDecode(response.body);
        return data["content"]["name"];
      });
    } catch (error) {
      _logger.warning("caught error which is $error");
      return "";
    }
  }
}
