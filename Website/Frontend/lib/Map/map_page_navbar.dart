import 'package:flutter/material.dart';
import 'package:flutter_application/Map/map_page_util.dart';

class NavBar extends StatefulWidget {
  final double screenHeight;
  final double screenWidth;
  final bool resizeProfilePic;

  const NavBar({
    super.key,
    required this.screenHeight,
    required this.screenWidth,
    required this.resizeProfilePic,
  });

  @override
  NavBarState createState() => NavBarState();
}

class NavBarState extends State<NavBar> {
  String _selectedFilter1 = "Filter1";
  String _selectedFilter2 = "Filter1";

  Widget buildDropdownButton(String selectedFilter, Function(String?) onChanged,
      BuildContext context) {
    return DropdownButton<String>(
      value: selectedFilter,
      icon: Icon(Icons.arrow_drop_down,
          weight: 20, color: Theme.of(context).colorScheme.onSecondary),
      style: TextStyle(color: Theme.of(context).colorScheme.onSecondary),
      onChanged: onChanged,
      items: <String>['Filter1', 'Filter2', 'Filter3']
          .map<DropdownMenuItem<String>>((String value) {
        return DropdownMenuItem<String>(
          value: value,
          child: Text(
            value,
            style: TextStyle(
              color: value == selectedFilter ? Colors.white : Colors.black,
            ),
          ),
        );
      }).toList(),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Row(
      mainAxisAlignment: MainAxisAlignment.start,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Container(
          //didnt use screen height because i only want the last container to resize not any other
          width: 170,
          height: 66,
          color: Theme.of(context).colorScheme.secondary,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(width: widget.screenWidth * 0.01),
              MapUtil.buildCircleAvatar("../assets/Fulltooth.png",
                  widget.resizeProfilePic, widget.screenWidth),
              const SizedBox(width: 20),
              //TODO change to user profile picture
              MapUtil.buildCircleAvatar("../assets/profile.png",
                  widget.resizeProfilePic, widget.screenWidth),
              const Spacer(),
            ],
          ),
        ),
        Expanded(
          child: Container(
            height: 66,
            color: Theme.of(context).colorScheme.secondaryContainer,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.start,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                SizedBox(width: widget.screenWidth * 0.01),
                Expanded(
                  child: Center(child: MapUtil.buildSearchWidget()),
                ),
                SizedBox(width: widget.screenWidth * 0.01),
                Container(
                  width: 2,
                  height: widget.screenHeight * 200,
                  color: Theme.of(context).colorScheme.surface,
                ),
                SizedBox(width: widget.screenWidth * 0.01),
                Center(
                  child:
                      buildDropdownButton(_selectedFilter1, (String? newValue) {
                    setState(() {
                      _selectedFilter1 = newValue!;
                    });
                  }, context),
                ),
                VerticalDivider(
                  color: Theme.of(context).colorScheme.onSecondary,
                  thickness: 2,
                ),
                Center(
                  child:
                      buildDropdownButton(_selectedFilter2, (String? newValue) {
                    setState(() {
                      _selectedFilter2 = newValue!;
                    });
                  }, context),
                ),
              ],
            ),
          ),
        ),
        Container(
          width: widget.screenWidth * 0.35,
          height: 66,
          color: Theme.of(context).colorScheme.primary,
          child: MapUtil.createDentistOfficesText(context, widget.screenWidth),
        ),
      ],
    );
  }
}
