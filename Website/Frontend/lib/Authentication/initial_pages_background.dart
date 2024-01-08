import 'package:flutter/material.dart';

class InitialPageBackground extends StatelessWidget {
  const InitialPageBackground({super.key});

  @override
  Widget build(BuildContext context) {
    double bottomToothSpace = 130;
    double topToothSpace = 150;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: Theme.of(context).colorScheme.background,
      body: Column(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Center(
            child: Image(
              image: const AssetImage("assets/BottomTooth.png"),
              height: bottomToothSpace,
            ),
          ),
          Center(
            child: Image(
              image: const AssetImage("assets/TopTooth.png"),
              height: topToothSpace,
            ),
          )
        ],
      ),
    );
  }
}
