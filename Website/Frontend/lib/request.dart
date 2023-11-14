import 'package:http/http.dart' as http;
import 'dart:convert';

class Request {
  static Future<void> sendLoginRequest(String uid, String email) async {
    var url = Uri.http('127.0.0.1:3000', 'api/logs/logins');
    http.post(url,
        headers: {
          "Accept": "application/json",
          "content-type": "application/json"
        },
        body: jsonEncode({
          "uid": uid,
          "email": email,
          "timestamp": DateTime.now().millisecondsSinceEpoch
        }));
  }

  static Future<void> sendSignupRequest(String uid, String email) async {
    var url = Uri.http('127.0.0.1:3000', 'api/logs/registrations');
    http.post(url,
        headers: {
          "Accept": "application/json",
          "content-type": "application/json"
        },
        body: jsonEncode({
          "uid": uid,
          "email": email,
          "timestamp": DateTime.now().millisecondsSinceEpoch
        }));
  }
}
