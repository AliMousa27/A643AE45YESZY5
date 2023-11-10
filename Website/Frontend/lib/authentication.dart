import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'initial_pages_background.dart';

class Authentication extends StatelessWidget {
  final bool isSigningUp;
  const Authentication({Key? key, required this.isSigningUp}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;
    bool isDesktop = screenSize.width > 800;
    double textFieldWidthFactor = isDesktop ? 0.4 : 0.7;
    return MaterialApp(
        home: Scaffold(
      resizeToAvoidBottomInset: false,
      body: Stack(
        alignment: Alignment.topCenter,
        children: [
          const InitialPageBackground(),
          //wrapped a scroll view with a container to make it scrollable
          Container(
            margin: EdgeInsets.only(
                //add margin equal to the size of keyboard to allow scrolling
                bottom: MediaQuery.of(context).viewInsets.bottom),
            child: SingleChildScrollView(
              child: Column(
                children: [
                  SizedBox(
                    height: screenSize.height * 0.2,
                  ),
                  Text(isSigningUp ? "Sign Up" : "Log In",
                      style: const TextStyle(
                          color: Color.fromRGBO(117, 116, 116, 1),
                          fontSize: 50,
                          fontFamily: "suezone")),
                  SizedBox(
                    height: screenSize.height * 0.04,
                  ),
                  createTextField(screenSize.width * textFieldWidthFactor,
                      screenSize.height * 0.07, "Enter email"),
                  SizedBox(
                    height: screenSize.height * 0.04,
                  ),
                  createTextField(screenSize.width * textFieldWidthFactor,
                      screenSize.height * 0.07, "Enter password"),
                  SizedBox(
                    height: screenSize.height * 0.04,
                  ),
                  if (isSigningUp)
                    createTextField(screenSize.width * textFieldWidthFactor,
                        screenSize.height * 0.07, "Confirm password"),
                  SizedBox(
                    height: screenSize.height * 0.04,
                  ),
                  Material(
                    elevation: 7,
                    borderRadius: BorderRadius.circular(30.0),
                    shadowColor: Colors.black,
                    child: OutlinedButton(
                        onPressed: () => onPressed(context),
                        style: ButtonStyle(
                            fixedSize: MaterialStateProperty.all<Size>(
                                const Size(230, 60)),
                            shape: MaterialStateProperty.all<
                                RoundedRectangleBorder>(RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(30.0),
                            )),
                            backgroundColor: MaterialStateProperty.all<Color>(
                                const Color.fromRGBO(176, 218, 243, 1))),
                        child: const Text(
                          "Confirm",
                          style: TextStyle(
                            color: Color.fromRGBO(88, 109, 121, 1),
                            fontSize: 20,
                            fontFamily: "suezone",
                          ),
                        )),
                  )
                ],
              ),
            ),
          )
        ],
      ),
    ));
  }

  TextField createTextField(double width, double height, String prompt) {
    return TextField(
        obscureText: prompt.contains("password") ? true : false,
        decoration: InputDecoration(
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(20)),
          fillColor: Colors.white,
          constraints: BoxConstraints(maxWidth: width, maxHeight: height),
          filled: true,
          labelText: prompt,
        ));
  }

  //TODO send request to express server
  onPressed(BuildContext context) {}
}
