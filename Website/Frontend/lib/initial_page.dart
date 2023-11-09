import 'package:flutter/material.dart';
import 'initial_pages_background.dart';

class InitialPage extends StatelessWidget {
  const InitialPage({super.key});

  Material createBTN(String prompt, double txtSize) {
    return Material(
      elevation: 7,
      borderRadius: BorderRadius.circular(30.0),
      shadowColor: Colors.black,
      child: OutlinedButton(
          onPressed: onPressed,
          style: ButtonStyle(
              fixedSize: MaterialStateProperty.all<Size>(const Size(230, 74)),
              shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                  RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(30.0),
              )),
              backgroundColor: MaterialStateProperty.all<Color>(Colors.white)),
          child: Text(
            prompt,
            style: TextStyle(
              color: const Color.fromRGBO(117, 116, 116, 1),
              fontSize: txtSize,
              fontFamily: "suezone",
            ),
          )),
    );
  }

  @override
  Widget build(BuildContext context) {
    //can use screensize.width and height to scale widgets
    Size screenSize = MediaQuery.of(context).size;
    bool isDesktop = screenSize.width > 800;
    double bankIDScaleFactor = isDesktop ? 0.2 : 0.57;
    return Scaffold(
      body: Stack(
        alignment: Alignment.topCenter,
        // Scale the stack
        children: [
          const InitialPageBackground(),
          Column(
            children: [
              //sizedbox now takes 17% of screen height
              SizedBox(height: screenSize.height * 0.27),
              createBTN("Log in with email", 23),
              SizedBox(height: screenSize.height * 0.04),
              createBTN("Sign up with email", 22),
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
              Container(
                decoration: BoxDecoration(
                  boxShadow: [
                    BoxShadow(
                        color: Colors.black.withOpacity(0.2),
                        spreadRadius: 2,
                        blurRadius: 7,
                        offset: const Offset(0, 10))
                  ],
                  borderRadius: BorderRadius.circular(30.0),
                  color: Colors.white,
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
            ],
          )
        ],
      ),
    );
  }

//TODO switch screens
  void onPressed() {}
}
