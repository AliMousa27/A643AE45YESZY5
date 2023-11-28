import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_application/Map/bottom_sheet.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/Map/side_drawer.dart';
import 'package:mapbox_gl/mapbox_gl.dart';
import 'map_page_navbar.dart';
import 'map_page_util.dart';

class MapPage extends StatefulWidget {
  const MapPage({super.key});

  @override
  MapPageState createState() => MapPageState();
}

class MapPageState extends State<MapPage> {
  bool sideBarIsCollapsed = true;
  late MapboxMapController mapController;
  double bottomSheetValue = 0.5;
  List<DentistOffice> dentistOffices = [];
  Future<List<DentistOffice>>? dentistOfficesFuture;

  @override
  void initState() {
    super.initState();
    DrawerState.registerObserver(this);
    BottomSheetState.registerObserver(this);
    dentistOfficesFuture = fetchDentistOffices();
  }

  Future<List<DentistOffice>> fetchDentistOffices() async {
    DentistOfficeController dentistOfficeController = DentistOfficeController();
    List<DentistOffice> offices =
        await dentistOfficeController.requestOffices();
    return offices;
  }

  @override
  Widget build(BuildContext context) {
    var screenWidth = MediaQuery.of(context).size.width;
    var screenHeight = MediaQuery.of(context).size.height;
    bool resizeProfilePic = screenWidth < 443;
    if (screenWidth > 668) {
      //reset bottom sheet value as screensize is desktop
      notify(0);
      return desktopLayout(screenHeight, screenWidth, resizeProfilePic);
    }
    //if the screen changes to from desktop to mobile make mark sidebar as collapsed to avoid map issues
    notify(true);
    return mobileLayout(screenHeight, screenWidth, resizeProfilePic);
  }

  Scaffold mobileLayout(
      double screenHeight, double screenWidth, bool resizeProfilePic) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          Row(
            //TODO define path in a dropdown menu for user navigation
            children: [
              Container(
                width: screenWidth * 0.5,
                height: double.infinity,
                color: Theme.of(context).colorScheme.secondary,
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    MapUtil.buildCircleAvatar(
                        "../assets/Fulltooth.png", false, screenWidth),
                    SizedBox(width: screenWidth * 0.05),
                    //TODO change to user profile picture
                    MapUtil.buildCircleAvatar(
                        "../assets/profile.png", false, screenWidth),
                  ],
                ),
              ),
            ],
          ),
          Container(
            padding: EdgeInsets.only(left: screenWidth * 0.07),
            height: double.infinity,
            width: screenWidth * 0.5,
            color: Theme.of(context).colorScheme.primary,
            child: MapUtil.createDentistOfficesText(context, screenWidth),
          )
        ],
      ),
      body: Stack(fit: StackFit.expand, children: [
        createMap(
          "mapbox://styles/bigman360/clpa24zoi004g01p95rqx61hw",
          "pk.eyJ1IjoiYmlnbWFuMzYwIiwiYSI6ImNscDl5dmM5MzAyMHAyanBkYmw1a24yd2EifQ.L1FfrH4Als9i33KTf0wStw",
          screenWidth,
          screenHeight,
        ),
        Column(
          children: [
            SizedBox(height: screenHeight * 0.04),
            Center(
              child: SizedBox(
                  width: screenWidth * 0.8, child: MapUtil.buildSearchWidget()),
            ),
            //Spacer(),
            const Expanded(child: BottomSheetMenu()),
          ],
        ),
      ]),
    );
  }

  Scaffold desktopLayout(
      double screenHeight, double screenWidth, bool resizeProfilePic) {
    return Scaffold(
      body: FutureBuilder<List<DentistOffice>>(
        future: dentistOfficesFuture,
        builder: (BuildContext context,
            AsyncSnapshot<List<DentistOffice>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return CircularProgressIndicator(); // or some other loading widget
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            return Stack(
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
                          createMap(
                            "mapbox://styles/bigman360/clpa24zoi004g01p95rqx61hw",
                            "pk.eyJ1IjoiYmlnbWFuMzYwIiwiYSI6ImNscDl5dmM5MzAyMHAyanBkYmw1a24yd2EifQ.L1FfrH4Als9i33KTf0wStw",
                            screenWidth,
                            screenHeight,
                          )
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
                    child: SideDrawer(offices: snapshot.data!),
                  ),
                ),
              ],
            );
          }
        },
      ),
    );
  }

  Widget createMap(
      String styleURL, String apiKey, double screenWidth, double screenHeight) {
    return AnimatedContainer(
      duration: const Duration(milliseconds: 100),
      width: sideBarIsCollapsed ? screenWidth : screenWidth * 0.65,
      height: screenHeight * (1 - bottomSheetValue),
      child: MapboxMap(
          //onStyleLoadedCallback: func,
          //onMapCreated: onMapCreated,
          //styleString: styleURL,
          initialCameraPosition: const CameraPosition(
              //gothenburg coordiantes
              target: LatLng(57.7089, 11.9746),
              zoom: 14),
          accessToken: apiKey,
          scrollGesturesEnabled: bottomSheetValue <= 0.11,
          zoomGesturesEnabled: bottomSheetValue <= 0.11),
    );
  }

  void func() async {
    Uint8List bytes = await loadMarkerImage();
    await mapController.addImage("testImage", bytes);
  }

  void notify(Object newValue) {
    setState(() {
      //bool for sidebar collapsed notifcations
      if (newValue is bool) {
        sideBarIsCollapsed = newValue;
        //double for bottom sheet notifications
      } else if (newValue is double) {
        bottomSheetValue = newValue;
      }
    });
  }

  void onMapCreated(MapboxMapController controller) async {
    mapController = controller;
    await addIcon();
  }

  Future<void> addIcon() async {
    var options = const SymbolOptions(
      geometry: LatLng(57.7089, 11.9746),
      iconSize: 0.1,
      iconImage: "testImage",
    );
    //the library is just slow so i have to delya the addSymbol function :)))))
    Timer(const Duration(seconds: 2), () => mapController.addSymbol(options));
  }

  Future<Uint8List> loadMarkerImage() async {
    var byteData = await rootBundle.load("assets/pin.png");
    return byteData.buffer.asUint8List();
  }
}
