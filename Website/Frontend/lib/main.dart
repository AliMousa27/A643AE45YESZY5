import 'package:flutter/material.dart';
import 'package:flutter_application/l10n/l10n.dart';
import 'Authentication/initial_page.dart';
import 'Utils/color_schemes.g.dart';
import 'package:firebase_core/firebase_core.dart';
import 'Utils/firebase_options.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'Map/map.dart';
import 'package:provider/provider.dart';
import 'Settings/setting_provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  final settingProvider = SettingProvider();

  FirebaseAuth.instance.authStateChanges().listen((User? user) {
    if (user == null) {
      runApp(
          MyApp(home: const InitialPage(), settingProvider: settingProvider));
    } else {
      runApp(MyApp(home: const MapPage(), settingProvider: settingProvider));
    }
  });
}

class MyApp extends StatelessWidget {
  final Widget home;
  final SettingProvider settingProvider;

  const MyApp({super.key, required this.home, required this.settingProvider});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider.value(
      value: settingProvider,
      child: Consumer<SettingProvider>(
        builder: (context, settingProvider, child) {
          return MaterialApp(
            debugShowCheckedModeBanner: false,
            title: 'App',
            theme: settingProvider.isDarkMode
                ? ThemeData(useMaterial3: true, colorScheme: darkColorScheme)
                : ThemeData(useMaterial3: true, colorScheme: lightColorScheme),
            supportedLocales: L10n.all,
            locale: Locale(settingProvider.language),
            localizationsDelegates: const [
              AppLocalizations.delegate,
              GlobalMaterialLocalizations.delegate,
              GlobalWidgetsLocalizations.delegate,
              GlobalCupertinoLocalizations.delegate,
            ],
            home: home,
          );
        },
      ),
    );
  }
}
