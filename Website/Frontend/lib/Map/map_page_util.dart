import 'package:flutter/material.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';

import '../widget_util.dart';
import 'dentist_apointment.dart';

class MapUtil {
  static Widget buildCircleAvatar(
      String assetName, bool resizeProfilePic, double screenWidth) {
    return CircleAvatar(
      backgroundColor: Colors.transparent,
      radius: resizeProfilePic ? screenWidth * 0.05 : 30,
      backgroundImage: AssetImage(assetName),
    );
  }

  static Widget createDentistOfficesText(
      BuildContext context, double screenWidth, int dentistOfficesCount) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.center,
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        SizedBox(width: screenWidth * 0.02),
        WidgetUtil.createText(
          Theme.of(context).colorScheme.onSurface,
          20,
          "Dentist offices: ",
          context,
        ),
        SizedBox(width: screenWidth * 0.01),
        Expanded(
          child: WidgetUtil.createText(
            Theme.of(context).colorScheme.onSurface,
            20,
            dentistOfficesCount.toString(),
            context,
          ),
        ),
      ],
    );
  }

  static Widget buildSearchWidget() {
    return SearchAnchor(
      builder: (BuildContext context, SearchController controller) {
        return SearchBar(
          controller: controller,
          onTap: () {
            //controller.openView();
          },
          onChanged: (_) {
            //controller.openView();
          },
          leading: const Icon(Icons.search),
        );
      },
      suggestionsBuilder: (context, pattern) {
        return [];
      },
    );
  }
}
