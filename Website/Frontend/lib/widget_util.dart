import 'package:flutter/material.dart';

class WidgetUtil {
  static ElevatedButton createBTN(BuildContext context, Color textColor,
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
        child: createText(textColor, 23, text));
  }

  static Text createText(Color textColor, double fontSize, String text) {
    return Text(
      text,
      style: TextStyle(
        color: textColor,
        fontSize: fontSize,
        fontFamily: "suezone",
      ),
    );
  }
}
