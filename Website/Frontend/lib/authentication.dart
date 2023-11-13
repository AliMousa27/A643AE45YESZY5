import 'package:flutter/material.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter_application/home.dart';
import 'initial_pages_background.dart';
import 'widget_util.dart';

class Authentication extends StatefulWidget {
  final bool isSigningUp;
  Authentication({Key? key, required this.isSigningUp}) : super(key: key);

  @override
  _AuthenticationState createState() => _AuthenticationState();
}

class _AuthenticationState extends State<Authentication> {
  final emailController = TextEditingController();
  final passwordController = TextEditingController();
  final confirmPasswordController = TextEditingController();
  final _formKey = GlobalKey<FormState>();
  String? errorMsg;

  @override
  Widget build(BuildContext context) {
    Size screenSize = MediaQuery.of(context).size;
    bool isDesktop = screenSize.width > 800;
    double textFieldWidthFactor = isDesktop ? 0.4 : 0.7;
    return Scaffold(
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
                    // 20% of screen height
                    height: screenSize.height * 0.2,
                  ),
                  Text(widget.isSigningUp ? "Sign Up" : "Log In",
                      style: TextStyle(
                          color: Theme.of(context).colorScheme.onSurface,
                          fontSize: 50,
                          fontFamily: "suezone")),
                  SizedBox(
                    // 4% of screen height
                    height: screenSize.height * 0.04,
                  ),
                  Form(
                    key: _formKey,
                    child: Column(children: [
                      createTextField(
                          screenSize.width * textFieldWidthFactor,
                          screenSize.height * 0.07,
                          "Enter email",
                          emailController,
                          context),
                      SizedBox(
                        height: screenSize.height * 0.04,
                      ),
                      createTextField(
                          screenSize.width * textFieldWidthFactor,
                          screenSize.height * 0.07,
                          "Enter password",
                          passwordController,
                          context),
                      SizedBox(
                        height: screenSize.height * 0.04,
                      ),
                      if (widget.isSigningUp)
                        createTextField(
                            screenSize.width * textFieldWidthFactor,
                            screenSize.height * 0.07,
                            "Confirm password",
                            confirmPasswordController,
                            context),
                      SizedBox(
                        height: screenSize.height * 0.04,
                      ),
                      Material(
                        elevation: 7,
                        borderRadius: BorderRadius.circular(30.0),
                        shadowColor: Theme.of(context).colorScheme.shadow,
                        child: WidgetUtil.createBTN(
                            context,
                            Theme.of(context).colorScheme.onPrimaryContainer,
                            const Size(230, 74),
                            Theme.of(context).colorScheme.primaryContainer,
                            () => onPressed(context),
                            "confirm"),
                      ),
                    ]),
                  ),
                  SizedBox(
                    height: screenSize.height * 0.04,
                  ),
                  Text(errorMsg ?? "",
                      style: TextStyle(
                          color: Theme.of(context).colorScheme.error,
                          backgroundColor:
                              Theme.of(context).colorScheme.errorContainer,
                          fontSize: 20,
                          fontFamily: "suezone")),
                ],
              ),
            ),
          )
        ],
      ),
    );
  }

  TextFormField createTextField(double width, double height, String prompt,
      TextEditingController controller, BuildContext context) {
    return TextFormField(
        //check if the text field is the password one to deicde to blur or not
        obscureText: prompt.contains("password") ? true : false,
        controller: controller,
        validator: (value) {
          if (value == null || value.isEmpty) {
            return 'Please fill in the field';
          } else if (prompt.contains("Confirm")) {
            if (value != passwordController.text) {
              return "Passwords do not match";
            }
          }
          return null;
        },
        decoration: InputDecoration(
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(20)),
          fillColor: Theme.of(context).colorScheme.surface,
          constraints: BoxConstraints(maxWidth: width, maxHeight: height),
          filled: true,
          labelText: prompt,
        ));
  }

  onPressed(BuildContext context) async {
    if (_formKey.currentState!.validate()) {
      if (widget.isSigningUp) {
        await signUp(emailController.text, passwordController.text, context);
      } else {
        await logIn(emailController.text, passwordController.text, context);
      }
    }
  }

  Future<void> signUp(
      String email, String password, BuildContext context) async {
    try {
      await FirebaseAuth.instance
          .createUserWithEmailAndPassword(email: email, password: password);
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => const HomePage()));
    } on FirebaseAuthException catch (e) {
      if (e.code == 'weak-password') {
        errorMsg = 'The password provided is too weak.';
      } else if (e.code == 'email-already-in-use') {
        errorMsg = 'The account already exists for that email.';
      } else if (e.code == 'invalid-email') {
        errorMsg = 'Invalid email.';
      } else if (e.code == 'too-many-requests') {
        errorMsg = 'Too many requests.';
      } else {
        errorMsg = e.code;
      }
    } catch (e) {
      errorMsg = e.toString();
    }
    setState(() {});
  }

  Future<void> logIn(
      String email, String password, BuildContext context) async {
    try {
      await FirebaseAuth.instance
          .signInWithEmailAndPassword(email: email, password: password);
      Navigator.push(
          context, MaterialPageRoute(builder: (context) => const HomePage()));
    } on FirebaseAuthException catch (e) {
      if (e.code == 'user-not-found') {
        errorMsg = 'No user found for that email.';
      } else if (e.code == 'wrong-password') {
        errorMsg = 'Wrong password provided for that user.';
      } else if (e.code == 'invalid-email') {
        errorMsg = 'Invalid email.';
      } else if (e.code == 'too-many-requests') {
        errorMsg = 'Too many requests.';
      } else if (e.code == 'invalid-login-credentials') {
        errorMsg = 'Invalid credential.';
      } else {
        errorMsg = e.code;
      }
    } catch (e) {
      errorMsg = e.toString();
    }
    setState(() {});
  }
}
