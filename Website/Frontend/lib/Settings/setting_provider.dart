import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

class SettingProvider extends ChangeNotifier {
  late SharedPreferences _prefs;
  String _language = 'en';
  bool _isDarkMode = false;

  SettingProvider() {
    _loadPreferences();
  }

  Future<void> _loadPreferences() async {
    _prefs = await SharedPreferences.getInstance();
    _language = _prefs.getString('language') ?? 'en';
    _isDarkMode = _prefs.getBool('isDarkMode') ?? false;
    notifyListeners();
  }

  String get language => _language;
  bool get isDarkMode => _isDarkMode;

  // get languages from localization
  Map<String, String> get languages =>
      {'en': 'English', 'sv': 'Svenska', 'de': 'Deutsch', 'bg': 'Български'};

  void setLanguage(String newLanguage) {
    _language = newLanguage;
    _prefs.setString('language', newLanguage);
    notifyListeners();
  }

  void toggleTheme() {
    _isDarkMode = !_isDarkMode;
    _prefs.setBool('isDarkMode', _isDarkMode);
    notifyListeners();
  }
}
