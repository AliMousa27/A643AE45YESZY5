import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Request {
  static Future<bool> sendBookingRequest(String body) async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', 'bookings/${user!.uid}');
      return http
          .post(url, headers: {"Content-Type": "application/json"}, body: body)
          .then((response) {
        Map<String, dynamic> data = jsonDecode(response.body);
        return data["status"] == "success";
      });
    } catch (error) {
      print(error);
      return false;
    }
  }

  //json contains the booking id and patient id
  //so endpoint will be delete/bookings/patientID/bookingID
  static Future<bool> cancelBookingRequest(String json) async {
    try {
      String userId = jsonDecode(json)["patientId"];
      String bookingId = jsonDecode(json)["bookingId"];
      print("the user id is $userId and the booking id is $bookingId");
      var url = Uri.http('127.0.0.1:3000', 'bookings/$userId/$bookingId');
      return http.delete(url,
          headers: {"Content-Type": "application/json"}).then((response) {
        Map<String, dynamic> data = jsonDecode(response.body);
        return data["status"] == "success";
      });
    } catch (error) {
      return false;
    }
  }

  static Future<List<DentistOffice>> getOffices() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', 'offices/${user!.uid}');

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
      print(error);
      return [];
    }
  }

  static Future<bool> setNotificationPreference(bool value) async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', 'patients/${user!.uid}');
      return http
          .patch(url,
              headers: {"Content-Type": "application/json"},
              body: jsonEncode({"id": user.uid, "notified": value.toString()}))
          .then((response) {
        var data = jsonDecode(response.body);
        print("the data is $data");
        return data["status"] == "success";
      });
    } catch (error) {
      print(error);
      return false;
    }
  }

  static Future<bool> getNotificationPreference() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', '/patients/${user!.uid}');
      return http.get(url).then((response) {
        var data = jsonDecode(response.body);
        print("the data is $data");
        return data["content"]["notified"];
      });
    } catch (error) {
      print(error);
      return false;
    }
  }

  static Future<bool> createPatient() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', '/patients/${user!.uid}');
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
        print("the data is $data");
        return data["status"] == "success";
      });
    } catch (error) {
      print(error);
      return false;
    }
  }

  static Future<List<DentistAppointment>> getAppointmentsByUID() async {
    try {
      final FirebaseAuth auth = FirebaseAuth.instance;
      final User? user = auth.currentUser;
      var url = Uri.http('127.0.0.1:3000', 'bookings/${user!.uid}');

      return http.get(url).then((response) {
        var data = jsonDecode(response.body);
        data = data["content"];
        List<DentistAppointment> appointments = [];
        for (var appointment in data) {
          appointments.add(DentistAppointment.fromJson(appointment));
        }
        return appointments;
      });
    } catch (e) {
      print("the error is");
      print(e.toString());
      return [];
    }
  }
}
