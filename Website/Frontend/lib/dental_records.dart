import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'setting_provider.dart';
import 'package:flutter_gen/gen_l10n/app_localizations.dart';

class DentalRecordsPage extends StatefulWidget {
  const DentalRecordsPage({Key? key}) : super(key: key);

  @override
  DentalRecordsPageState createState() => DentalRecordsPageState();
}

class DentalRecordsPageState extends State<DentalRecordsPage> {
  //Dummy Data. Remove Later
  List<String> recordDates = [
    "2023-12-11",
    "2023-05-23",
    "2022-12-11",
    "2022-05-15",
    "2022-01-11",
    "2021-12-11",
    "2021-05-23",
    "2021-01-11",
    "2020-12-11",
    "2020-05-23",
    "2020-01-11",
    "2019-12-11",
  ];

  @override
  Widget build(BuildContext context) {
    return Consumer<SettingProvider>(
        builder: (context, settingProvider, child) {
      return Scaffold(
          body: Container(
              alignment: Alignment.center,
              decoration: BoxDecoration(
                image: DecorationImage(
                  image: AssetImage(settingProvider.isDarkMode
                      ? 'assets/setting-background-dark.jpg'
                      : 'assets/setting-background.jpg'),
                  fit: BoxFit.cover,
                ),
              ),
              child: Column(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: [
                    const SizedBox(
                      height: 50,
                    ),
                    Text(AppLocalizations.of(context)!.dental_records_title,
                        style: TextStyle(
                          fontSize: 30,
                          fontFamily: "suezone",
                          fontWeight: FontWeight.bold,
                          backgroundColor:
                              Theme.of(context).colorScheme.background,
                        )),
                    Text("*Patient Name*",
                        style: TextStyle(
                          fontSize: 20,
                          fontFamily: "suezone",
                          fontWeight: FontWeight.bold,
                          backgroundColor:
                              Theme.of(context).colorScheme.background,
                        )),
                    const SizedBox(
                      height: 20,
                    ),
                    Expanded(
                        child: SingleChildScrollView(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          ...generateExpansionTiles(),
                        ],
                      ),
                    ))
                  ])));
    });
  }

  List<Widget> generateExpansionTiles() {
    return recordDates.map((date) {
      return Container(
        width: 600,
        alignment: Alignment.center,
        child: ExpansionTile(
          iconColor: Theme.of(context).colorScheme.onPrimary,
          collapsedBackgroundColor:
              Theme.of(context).colorScheme.primaryContainer,
          collapsedTextColor: Theme.of(context).colorScheme.onPrimaryContainer,
          backgroundColor: Theme.of(context).colorScheme.primary,
          textColor: Theme.of(context).colorScheme.onPrimary,
          title: Text(date, style: const TextStyle(fontFamily: "suezone")),
          children: [
            ListTile(
              title: Text(
                AppLocalizations.of(context)!.dental_records_notes,
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                  fontFamily: "suezone",
                  fontWeight: FontWeight.bold,
                ),
              ),
              subtitle: Text(
                "Random Scribbles",
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                  fontFamily: "suezone",
                ),
              ),
            ),
            ListTile(
              title: Text(
                "- Doctor",
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                  fontFamily: "suezone",
                ),
              ),
            ),
          ],
        ),
      );
    }).toList();
  }
}
