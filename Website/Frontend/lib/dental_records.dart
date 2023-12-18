import 'package:flutter/material.dart';

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
    return Scaffold(
        body: Container(
            alignment: Alignment.center,
            decoration: const BoxDecoration(
              image: DecorationImage(
                image: AssetImage('assets/records-background-dark.png'),
                fit: BoxFit.cover,
              ),
            ),
            child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const SizedBox(
                    height: 50,
                  ),
                  const Text("Dental Record",
                      style: TextStyle(
                        fontSize: 30,
                        fontWeight: FontWeight.bold,
                      )),
                  const Text("*Patient Name*",
                      style: TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.bold,
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
          title: Text(date),
          children: [
            ListTile(
              title: Text(
                "Notes:",
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                  fontWeight: FontWeight.bold,
                ),
              ),
              subtitle: Text(
                "Random Scribbles",
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                ),
              ),
            ),
            ListTile(
              title: Text(
                "- Doctor",
                style: TextStyle(
                  color: Theme.of(context).colorScheme.onPrimary,
                ),
              ),
            ),
          ],
        ),
      );
    }).toList();
  }
}
