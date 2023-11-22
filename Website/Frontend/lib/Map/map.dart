import 'package:flutter/material.dart';
import 'package:flutter_application/Map/side_drawer.dart';
import 'package:mapbox_gl/mapbox_gl.dart';
import 'map_page_navbar.dart';

class HomePage extends StatefulWidget {
  const HomePage({super.key});

  @override
  HomePageState createState() => HomePageState();
}

class HomePageState extends State<HomePage> {
  bool sideBarIsCollapsed = true;
  @override
  void initState() {
    super.initState();
    DrawerState.registerObserver(this);
  }

  @override
  Widget build(BuildContext context) {
    var screenWidth = MediaQuery.of(context).size.width;
    var screenHeight = MediaQuery.of(context).size.height;
    bool resizeProfilePic = screenWidth < 443;
    return Scaffold(
      body: Stack(
        children: [
          Column(
            mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              NavBar(
                screenHeight: screenHeight,
                screenWidth: screenWidth,
                resizeProfilePic: resizeProfilePic,
              ),
              Expanded(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    SizedBox(
                      width: screenWidth,
                      height: screenHeight * 0.89,
                      child: MapboxMap(
                        // only allow zooming when sidebar is collapsed otherwise it will interfere with the sidebar
                        zoomGesturesEnabled: sideBarIsCollapsed,
                        initialCameraPosition: const CameraPosition(
                            //gothenburg coordiantes
                            target: LatLng(57.7089, 11.9746),
                            zoom: 14),
                        accessToken:
                            "pk.eyJ1IjoibWVobWV0YXNpbTM1IiwiYSI6ImNsb3I2YzNxeDBzZTgyaW82am9tazF2azAifQ.QTpwJIy1HytUC2w0vagkWQ",
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ),
          Align(
            alignment: Alignment.topRight,
            child: SizedBox(
              //controls how much it "slides" in and out
              width: screenWidth * 0.35,
              height: screenHeight,
              child: const SideDrawer(),
            ),
          ),
        ],
      ),
    );
  }

  void notify(bool newValue) {
    setState(() {
      sideBarIsCollapsed = newValue;
    });
  }
}
