import 'package:flutter/material.dart';

class MyBookings extends StatefulWidget {
  const MyBookings({super.key});

  @override
  State<MyBookings> createState() => _MyBookingsState();
}

class _MyBookingsState extends State<MyBookings> {
  List<bool> hovering = List<bool>.filled(3, false);

  List<Widget> buildNavbarActions(double screenWidth, Color textColor,
      Color btnColor, Color btnHoverColor) {
    List<Widget> actions = [];
    int numActions = 3;
    List<String> actionPrompts = ['My Bookings', 'Map', 'Settings'];
    for (int i = 0; i < numActions; i++) {
      actions.add(
        ElevatedButton(
          style: ButtonStyle(
            backgroundColor: MaterialStateProperty.resolveWith<Color>(
              (Set<MaterialState> states) {
                if (states.contains(MaterialState.hovered)) {
                  return Colors.white.withOpacity(0.8);
                }
                return hovering[i] ? btnHoverColor : btnColor;
              },
            ),
            elevation: MaterialStateProperty.resolveWith<double>(
              (Set<MaterialState> states) {
                if (states.contains(MaterialState.hovered)) return 10;
                return 2;
              },
            ),
          ),
          onPressed: () {
            // TODO
          },
          child: Text(
            actionPrompts[i],
            style: TextStyle(color: textColor, fontSize: 14),
          ),
        ),
      );
      actions.add(SizedBox(width: screenWidth * 0.03));
    }
    return actions;
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.primary,
        leading: Image.asset(
          'assets/FullTooth.png',
        ),
        title: SizedBox(
          width: 200,
          child: Image.asset(
            'assets/ToothTrek.png',
            fit: BoxFit.scaleDown,
          ),
        ),
        actions: buildNavbarActions(
            screenWidth,
            Theme.of(context).colorScheme.onPrimary,
            Theme.of(context).colorScheme.primaryContainer,
            Theme.of(context).colorScheme.primary),
      ),
      body: Row(
        children: [
          Align(
            alignment: Alignment.centerLeft,
            child: Image.asset(
              'assets/FullTooth.png',
            ),
          ),
          Expanded(
            child: Container(
              color: Colors.green,
            ),
          ),
          Align(
            alignment: Alignment.centerRight,
            child: Image.asset(
              'assets/FullTooth.png',
            ),
          ),
        ],
      ),
    );
  }
}
