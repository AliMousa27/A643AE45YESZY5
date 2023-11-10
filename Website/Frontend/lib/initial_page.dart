import 'package:flutter/material.dart';
import 'package:flutter_application/authentication.dart';
import 'initial_pages_background.dart';
import 'widget_util.dart';

class InitialPage extends StatelessWidget {
  const InitialPage({super.key});

  Material createBTN(String prompt, double txtSize, BuildContext context,
      String buttonId, String text) {
    return Material(
        elevation: 7,
        borderRadius: BorderRadius.circular(30.0),
        shadowColor: Colors.black,
        child: WidgetUtil.newMethod(
            context,
            const Color.fromRGBO(117, 116, 116, 1),
            const Size(230, 74),
            const Color.fromRGBO(176, 218, 243, 1),
            () => onPressed(context, buttonId),
            text));
  }

  @override
  Widget build(BuildContext context) {
    //can use screensize.width and height to scale widgets
    Size screenSize = MediaQuery.of(context).size;
    bool isDesktop = screenSize.width > 800;
    double bankIDScaleFactor = isDesktop ? 0.2 : 0.57;
    return MaterialApp(
      home: Scaffold(
        body: Stack(
          alignment: Alignment.topCenter,
          // Scale the stack
          children: [
            const InitialPageBackground(),
            Column(
              children: [
                //sizedbox now takes 17% of screen height
                SizedBox(height: screenSize.height * 0.27),
                createBTN(
                    "Log in with email", 23, context, "Log in BTN", "Log in"),
                SizedBox(
                  height: screenSize.height * 0.04,
                ),
                createBTN("Sign up with email", 22, context, "Sign up BTN",
                    "Sign up"),
                SizedBox(height: screenSize.height * 0.04),
                Container(
                  height: 1,
                  width: screenSize.width * 0.7,
                  color: Colors.black,
                ),
                SizedBox(
                    height: isDesktop
                        ? screenSize.height * 0.02
                        : screenSize.height * 0.05),
                GestureDetector(
                  onTap: () => onPressed(context, "BankID BTN"),
                  child: Container(
                    decoration: BoxDecoration(
                      boxShadow: [
                        BoxShadow(
                            color: Colors.black.withOpacity(0.2),
                            spreadRadius: 2,
                            blurRadius: 7,
                            offset: const Offset(0, 10))
                      ],
                      borderRadius: BorderRadius.circular(30.0),
                      color: const Color.fromRGBO(176, 218, 243, 1),
                    ),
                    height: screenSize.height * 0.1,
                    width: screenSize.width * bankIDScaleFactor,
                    child: Center(
                      child: Image.asset(
                        "assets/BankID.png",
                        height: screenSize.height * 0.08,
                        width: screenSize.width * 0.2,
                        fit: BoxFit.contain,
                      ),
                    ),
                  ),
                ),
              ],
            )
          ],
        ),
      ),
    );
  }

  void onPressed(BuildContext context, String buttonId) {
    bool isSigningUp = buttonId == "Sign up BTN";

    if (buttonId == "BankID BTN") {
      //TODO implement band id here
      print("BankID pressed");
    } else {
      Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => Authentication(
                  isSigningUp: isSigningUp,
                )),
      );
    }
  }
}
