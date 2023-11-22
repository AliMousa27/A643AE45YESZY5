import 'dart:math';

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

  static double textScaleFactor(BuildContext context,
      {double maxTextScaleFactor = 1.2}) {
    final width = MediaQuery.of(context).size.width;
    double val = (width / 1400) * maxTextScaleFactor;
    return max(1, min(val, maxTextScaleFactor));
  }

  static Text createText(Color textColor, double fontSize, String text,
      [BuildContext? context]) {
    double scale = context == null ? 1 : textScaleFactor(context);
    return Text(
      text,
      overflow: TextOverflow.ellipsis,
      textScaleFactor: scale,
      style: TextStyle(
        color: textColor,
        fontSize: fontSize,
        fontFamily: "suezone",
      ),
    );
  }
}
