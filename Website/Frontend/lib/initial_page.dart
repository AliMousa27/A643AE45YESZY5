import 'package:flutter/material.dart';
import 'package:flutter_application/authentication.dart';
import 'initial_pages_background.dart';
import 'widget_util.dart';

class InitialPage extends StatelessWidget {
  const InitialPage({super.key});

  Material createBTN(String prompt, double txtSize, BuildContext context,
      String buttonId, String text) {
    double shadowElevation = 7;
    double btnWidth = 230;
    double btnHeight = 74;
    double cornerRadius = 30.0;
    return Material(
        elevation: shadowElevation,
        borderRadius: BorderRadius.circular(cornerRadius),
        child: WidgetUtil.createBTN(
            context,
            Theme.of(context).colorScheme.onPrimaryContainer,
            Size(btnWidth, btnHeight),
            Theme.of(context).colorScheme.primaryContainer,
            () => onPressed(context, buttonId),
            text));
  }

  @override
  Widget build(BuildContext context) {
    //can use screensize.width and height to scale widgets
    Size screenSize = MediaQuery.of(context).size;
    //The 2 following numbers are scaling factors defined by layout trials
    return Scaffold(
      body: Stack(
        alignment: Alignment.topCenter,
        // Scale the stack
        children: [
          const InitialPageBackground(),
          Column(
            children: [
              //sizedbox now takes 27% of screen height
              SizedBox(height: screenSize.height * 0.27),
              createBTN(
                  "Log in with email", 23, context, "Log in BTN", "Log in"),
              //sizedbox now takes 4% of screen height
              SizedBox(
                height: screenSize.height * 0.04,
              ),
              createBTN(
                  "Sign up with email", 22, context, "Sign up BTN", "Sign up"),
              //sizedbox now takes 4% of screen height
              SizedBox(height: screenSize.height * 0.02),
              Container(
                height: 1,
                //width is 70% of screen width
                width: screenSize.width * 0.7,
                color: Theme.of(context).colorScheme.shadow,
              ),
              SizedBox(height: screenSize.height * 0.02),

              //depending on screen size, the height of the sizedbox is different for
              //desktop and mobile designs

              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () => onPressed(context, "Google BTN"),
                    child: Material(
                      elevation: 7.0,
                      borderRadius: BorderRadius.circular(30.0),
                      color: Theme.of(context).colorScheme.primaryContainer,
                      child: Container(
                        height: 73,
                        width: 230,
                        alignment: Alignment.centerLeft,
                        child: Flexible(
                          // Add this
                          child: Row(
                            children: [
                              const SizedBox(
                                width: 20,
                              ),
                              Flexible(
                                // And this
                                child: Image.asset(
                                  "assets/google_logo.png",
                                  fit: BoxFit.cover,
                                ),
                              ),
                              const SizedBox(
                                width: 27,
                              ),
                              WidgetUtil.createText(
                                  Theme.of(context)
                                      .colorScheme
                                      .onPrimaryContainer,
                                  23,
                                  "google"),
                            ],
                          ),
                        ),
                      ),
                    ),
                  ),
                ],
              )
            ],
          )
        ],
      ),
    );
  }

  void onPressed(BuildContext context, String buttonId) {
    bool isSigningUp = buttonId == "Sign up BTN";

    if (buttonId == "Google BTN") {
      //TODO implement band id here
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
