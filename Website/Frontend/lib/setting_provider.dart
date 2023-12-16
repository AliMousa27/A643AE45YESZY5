import 'package:flutter/material.dart';

class SettingProvider extends ChangeNotifier {
  String _language = 'en';

  String get language => _language;

  // get languages from localization
  Map<String, String> get languages =>
      {'en': 'English', 'sv': 'Svenska', 'de': 'Deutsch', 'bg': 'Български'};

  void setLanguage(String newLanguage) {
    _language = newLanguage;
    notifyListeners();
  }

  bool _isDarkMode = false;

  bool get isDarkMode => _isDarkMode;

  void toggleTheme() {
    _isDarkMode = !_isDarkMode;
    notifyListeners();
  }
}
