import 'package:flutter/material.dart';

class InitialPageBackground extends StatelessWidget {
  const InitialPageBackground({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      title: 'Flutter Demo',
      home: Scaffold(
        resizeToAvoidBottomInset: false,
        backgroundColor: Color.fromARGB(255, 234, 255, 253),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: <Widget>[
            Center(
              child: Image(
                image: AssetImage("assets/BottomTooth.png"),
                height: 130,
              ),
            ),
            Center(
              child: Image(
                image: AssetImage("assets/TopTooth.png"),
                height: 150,
              ),
            )
          ],
        ),
      ),
    );
  }
}
