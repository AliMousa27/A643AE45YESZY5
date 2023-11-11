import 'package:flutter/material.dart';

class WidgetUtil {
  static ElevatedButton newMethod(BuildContext context, Color textColor,
      Size btnSize, Color btnColor, VoidCallback onPressed, String text) {
    return ElevatedButton(
        //assign the button to a passed in callback function
        onPressed: onPressed,
        style: ButtonStyle(
            fixedSize: MaterialStateProperty.all<Size>(btnSize),
            shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(30.0),
            )),
            backgroundColor: MaterialStateProperty.all<Color>(btnColor)),
        child: Text(
          text,
          style: TextStyle(
            color: textColor,
            fontSize: 20,
            fontFamily: "suezone",
          ),
        ));
  }
}
