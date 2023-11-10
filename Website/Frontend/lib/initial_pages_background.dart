import 'package:flutter/material.dart';

class InitialPageBackground extends StatelessWidget {
  const InitialPageBackground({super.key});

  @override
  Widget build(BuildContext context) {
    double bottomToothSpace = 130;
    double topToothSpace = 150;
    return MaterialApp(
      title: 'Flutter Demo',
      home: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: const Color.fromARGB(255, 234, 255, 253),
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
      ),
    );
  }
}
