import 'package:flutter/material.dart';
import 'package:flutter_application/Map/side_drawer.dart';
import 'package:mapbox_gl/mapbox_gl.dart';
import 'map_page_navbar.dart';
import 'package:flutter/services.dart' show rootBundle;

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
    Future<String> loadStyle() async {
      return await rootBundle.loadString('assets/style.json');
    }

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
                        styleString:
                            "mapbox://styles/bigman360/clpa24zoi004g01p95rqx61hw",
                        // only allow zooming when sidebar is collapsed otherwise it will interfere with the sidebar
                        zoomGesturesEnabled: sideBarIsCollapsed,
                        initialCameraPosition: const CameraPosition(
                            //gothenburg coordiantes
                            target: LatLng(57.7089, 11.9746),
                            zoom: 14),
                        accessToken:
                            "pk.eyJ1IjoiYmlnbWFuMzYwIiwiYSI6ImNscDl5dmM5MzAyMHAyanBkYmw1a24yd2EifQ.L1FfrH4Als9i33KTf0wStw",
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
