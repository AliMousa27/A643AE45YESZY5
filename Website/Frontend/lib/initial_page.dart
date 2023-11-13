import 'package:flutter/material.dart';
import 'package:flutter_application/authentication.dart';
import 'initial_pages_background.dart';
import 'widget_util.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:google_sign_in/google_sign_in.dart';

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

              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  GestureDetector(
                    onTap: () => onPressed(context, "Google BTN"),
                    child: Material(
                      elevation: 7.0,
                      borderRadius: BorderRadius.circular(30.0),
                      color: Theme.of(context).colorScheme.primaryContainer,
                      child: SizedBox(
                        height: 73,
                        width: screenSize.width > 230 ? 230 : screenSize.width,
                        child: Stack(
                          children: [
                            Align(
                              alignment: Alignment.centerLeft,
                              child: Padding(
                                padding: const EdgeInsets.only(left: 10.0),
                                child: Image.asset(
                                  "assets/google_logo.png",
                                  height: 50,
                                  width: 50,
                                ),
                              ),
                            ),
                            Center(
                              child: WidgetUtil.createText(
                                Theme.of(context)
                                    .colorScheme
                                    .onPrimaryContainer,
                                23,
                                "Google",
                              ),
                            ),
                          ],
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

  Future<void> onPressed(BuildContext context, String buttonId) async {
    bool isSigningUp = buttonId == "Sign up BTN";

    if (buttonId == "Google BTN") {
      UserCredential? userCredential = await signInWithGoogle();
      if (userCredential != null) {
        print('User signed in: ${userCredential.user?.displayName}');
      } else {
        print('Sign-in process canceled.');
      }
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

  Future<UserCredential?> signInWithGoogle() async {
    try {
      final GoogleSignInAccount? googleSignInAccount =
          await GoogleSignIn().signIn();

      if (googleSignInAccount == null) {
        return null; // User canceled the sign-in process
      }

      final GoogleSignInAuthentication googleSignInAuthentication =
          await googleSignInAccount.authentication;

      final OAuthCredential credential = GoogleAuthProvider.credential(
        accessToken: googleSignInAuthentication.accessToken,
        idToken: googleSignInAuthentication.idToken,
      );

      return await FirebaseAuth.instance.signInWithCredential(credential);
    } catch (error) {
      print(error);
      return null;
    }
  }
}
