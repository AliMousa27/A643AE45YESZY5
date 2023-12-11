import 'package:flutter/material.dart';
import 'package:flutter_application/l10n/l10n.dart';
import 'initial_page.dart';
import 'color_schemes.g.dart';
import 'package:firebase_core/firebase_core.dart';
import 'firebase_options.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'Map/map.dart';
import 'package:provider/provider.dart';
import 'setting_provider.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp(
    options: DefaultFirebaseOptions.currentPlatform,
  );

  FirebaseAuth.instance.authStateChanges().listen((User? user) {
    if (user == null) {
      runApp(const MyApp(home: InitialPage()));
    } else {
      runApp(const MyApp(home: MapPage()));
    }
  });
}

class MyApp extends StatelessWidget {
  final Widget home;

  const MyApp({super.key, required this.home});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (context) => SettingProvider(),
      child: Consumer<SettingProvider>(
        builder: (context, settingProvider, child) {
          return MaterialApp(
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
