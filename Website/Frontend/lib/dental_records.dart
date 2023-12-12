import 'package:flutter/material.dart';

class DentalRecordsPage extends StatefulWidget {
  const DentalRecordsPage({Key? key}) : super(key: key);

  @override
  DentalRecordsPageState createState() => DentalRecordsPageState();
}

class DentalRecordsPageState extends State<DentalRecordsPage> {
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
          Container(
              width: 600,
              alignment: Alignment.center,
              child: ExpansionTile(
                collapsedBackgroundColor:
                    Theme.of(context).colorScheme.primaryContainer,
                collapsedTextColor:
                    Theme.of(context).colorScheme.onPrimaryContainer,
                backgroundColor: Theme.of(context).colorScheme.primary,
                textColor: Theme.of(context).colorScheme.onPrimary,
                title: const Text("2023-12-11"),
                children: [
                  ListTile(
                      title: Text("Notes:",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary,
                              fontWeight: FontWeight.bold)),
                      subtitle: Text("Random Scribbles",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary))),
                  ListTile(
                    title: Text("- Doctor",
                        style: TextStyle(
                            color: Theme.of(context).colorScheme.onPrimary)),
                  )
                ],
              )),
          Container(
              width: 600,
              alignment: Alignment.center,
              child: ExpansionTile(
                collapsedBackgroundColor:
                    Theme.of(context).colorScheme.primaryContainer,
                collapsedTextColor:
                    Theme.of(context).colorScheme.onPrimaryContainer,
                backgroundColor: Theme.of(context).colorScheme.primary,
                textColor: Theme.of(context).colorScheme.onPrimary,
                title: const Text("2023-05-23"),
                children: [
                  ListTile(
                      title: Text("Notes:",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary,
                              fontWeight: FontWeight.bold)),
                      subtitle: Text("Random Scribbles",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary))),
                  ListTile(
                    title: Text("- Doctor",
                        style: TextStyle(
                            color: Theme.of(context).colorScheme.onPrimary)),
                  )
                ],
              )),
          Container(
              width: 600,
              alignment: Alignment.center,
              child: ExpansionTile(
                collapsedBackgroundColor:
                    Theme.of(context).colorScheme.primaryContainer,
                collapsedTextColor:
                    Theme.of(context).colorScheme.onPrimaryContainer,
                backgroundColor: Theme.of(context).colorScheme.primary,
                textColor: Theme.of(context).colorScheme.onPrimary,
                title: const Text("2022-12-11"),
                children: [
                  ListTile(
                      title: Text("Notes:",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary,
                              fontWeight: FontWeight.bold)),
                      subtitle: Text("Random Scribbles",
                          style: TextStyle(
                              color: Theme.of(context).colorScheme.onPrimary))),
                  ListTile(
                    title: Text("- Doctor",
                        style: TextStyle(
                            color: Theme.of(context).colorScheme.onPrimary)),
                  ),
                ],
              ))
        ],
      ),
    ));
  }
}
