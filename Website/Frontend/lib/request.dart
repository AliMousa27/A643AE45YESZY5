import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class Request {
  static Future<void> sendLoginRequest(String uid, String email) async {
    var url = Uri.http('127.0.0.1:3000', 'logs/logins');
    http.post(url,
        headers: {
          "Accept": "application/json",
          "content-type": "application/json"
        },
        body: jsonEncode({"uid": uid, "email": email}));
  }

  static Future<void> sendSignupRequest(String uid, String email) async {
    var url = Uri.http('127.0.0.1:3000', 'logs/registrations');
    http.post(url,
        headers: {
          "Accept": "application/json",
          "content-type": "application/json"
        },
        body: jsonEncode({
          "uid": uid,
          "email": email,
        }));
  }

  static Future<List<DentistOffice>> getOffices() async {
    try {
      var url = Uri.http('127.0.0.1:3000', 'offices');
      return http
          .get(url, headers: {"Accept": "application/json"}).then((response) {
        var data = jsonDecode(response.body);
        print("data type is ${data.runtimeType}");
        List<DentistOffice> offices = [];
        for (var office in data) {
          offices.add(DentistOffice.fromJson(office));
          print(offices[0]);
        }
        return offices;
      });
    } catch (error) {
      print(error);
      return [];
    }
  }
}
