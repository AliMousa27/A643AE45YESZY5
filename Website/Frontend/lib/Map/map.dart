import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_application/Map/bottom_sheet.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/Map/side_drawer.dart';
import 'package:flutter_application/Utils/widget_util.dart';
import 'package:mapbox_gl/mapbox_gl.dart';
import '../Utils/request.dart';

class MapPage extends StatefulWidget {
  const MapPage({super.key});

  @override
  MapPageState createState() => MapPageState();
}

class MapPageState extends State<MapPage> {
  bool sideBarIsCollapsed = true;
  late MapboxMapController mapController;
  Future<List<DentistOffice>>? dentistOfficesFuture;
  GlobalKey<SideDrawerState> sideDrawerKey = GlobalKey<SideDrawerState>();
  GlobalKey<BottomSheetMenuState> bottomSheetKey =
      GlobalKey<BottomSheetMenuState>();
  @override
  void initState() {
    super.initState();
    DrawerState.registerObserver(this);
    dentistOfficesFuture = Request.getOffices();
  }

  @override
  Widget build(BuildContext context) {
    var screenWidth = MediaQuery.of(context).size.width;
    var screenHeight = MediaQuery.of(context).size.height;
    bool resizeProfilePic = screenWidth < 443;
    if (screenWidth > 668) {
      //reset bottom sheet value as screensize is desktop
      return desktopLayout(screenHeight, screenWidth, resizeProfilePic);
    }
    //if the screen changes to from desktop to mobile make mark sidebar as collapsed to avoid map issues
    notify(true);
    return mobileLayout(screenHeight, screenWidth, resizeProfilePic);
  }

  Widget mobileLayout(
      double screenHeight, double screenWidth, bool resizeProfilePic) {
    return FutureBuilder(
        future: dentistOfficesFuture,
        builder: (BuildContext context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Text("Error: ${snapshot.error}");
          } else {
            return Scaffold(
              appBar: WidgetUtil.buildNavBar(
                  context,
                  screenWidth,
                  Theme.of(context).colorScheme.onPrimary,
                  Theme.of(context).colorScheme.primaryContainer,
                  Theme.of(context).colorScheme.primary),
              body: Stack(children: [
                createMap(
                  "mapbox://styles/bigman360/clpa24zoi004g01p95rqx61hw",
                  "pk.eyJ1IjoiYmlnbWFuMzYwIiwiYSI6ImNscDl5dmM5MzAyMHAyanBkYmw1a24yd2EifQ.L1FfrH4Als9i33KTf0wStw",
                  screenWidth,
                  screenHeight,
                ),
                Column(
                  children: [
                    const Spacer(),
                    BottomSheetMenu(
                      key: bottomSheetKey,
                      offices: snapshot.data!,
                    ),
                  ],
                ),
              ]),
            );
          }
        });
  }

  Scaffold desktopLayout(
      double screenHeight, double screenWidth, bool resizeProfilePic) {
    return Scaffold(
      body: FutureBuilder<List<DentistOffice>>(
        future: dentistOfficesFuture,
        builder: (BuildContext context,
            AsyncSnapshot<List<DentistOffice>> snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
                child:
                    CircularProgressIndicator()); // or some other loading widget
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            return Stack(
              children: [
                Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    WidgetUtil.buildNavBar(
                        context,
                        screenWidth,
                        Theme.of(context).colorScheme.onPrimary,
                        Theme.of(context).colorScheme.primaryContainer,
                        Theme.of(context).colorScheme.primary),
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
                Positioned(
                  top: 0,
                  right: 0,
                  child: SizedBox(
                    //controls how much it "slides" in and out
                    width: screenWidth * 0.35,
                    height: screenHeight,
                    child:
                        SideDrawer(offices: snapshot.data!, key: sideDrawerKey),
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
      //Note the container resizes nomrally and the red borders show it but the library is broken.
      // https://github.com/flutter-mapbox-gl/maps/issues/795 claimed to have fixed it in patch 2.7  but they didnt
      /*decoration: BoxDecoration(
        border: Border.all(
          color: Colors.red, // Border color
          width: 3.0, // Border width
        ),
      ),*/
      duration: const Duration(milliseconds: 100),
      width: sideBarIsCollapsed ? screenWidth : screenWidth * 0.65,
      height: screenWidth < 668
          ? screenHeight * (0.52 * (screenHeight * 0.00001 + 1))
          : screenHeight,
      child: MapboxMap(
          onStyleLoadedCallback: addMarkers,
          onMapCreated: onMapCreated,
          //styleString: styleURL,
          initialCameraPosition: const CameraPosition(
              //gothenburg coordiantes
              target: LatLng(57.7089, 11.9746),
              zoom: 14),
          accessToken: apiKey,
          zoomGesturesEnabled: sideBarIsCollapsed),
    );
  }

  void addMarkers() async {
    Uint8List bytes = await loadMarkerImage();
    await mapController.addImage("marker", bytes);
    addIcon();
  }

  void notify(bool newValue) {
    setState(() {
      //bool for sidebar collapsed notifcations
      sideBarIsCollapsed = newValue;
    });
  }

  void onMapCreated(MapboxMapController controller) async {
    mapController = controller;
  }

  void addIcon() async {
    List<DentistOffice>? dentists = await dentistOfficesFuture;
    for (int i = 0; i < dentists!.length; i++) {
      DentistOffice office = dentists[i];
      SymbolOptions options = SymbolOptions(
        geometry: office.location,
        iconSize: 0.1,
        iconImage: "marker",
      );

      mapController.addSymbol(options);
    }

    mapController.onSymbolTapped.add((argument) {
      onSymbolClick(argument);
    });
  }

  void onSymbolClick(Symbol s) async {
    List<DentistOffice>? dentists = await dentistOfficesFuture;
    //finding the index to send it to the menu to scroll and expand to the correct office
    int indexOfOffice = dentists!.indexWhere((element) =>
        element.location.latitude == s.options.geometry?.latitude &&
        element.location.longitude == s.options.geometry?.longitude);
    //depending on layout on of them will be attached to the widget tree so
    //only one of them will be not null
    sideDrawerKey.currentState?.scrollToAndExpand(indexOfOffice);
    bottomSheetKey.currentState?.scrollToAndExpandTile(indexOfOffice);
  }

  Future<Uint8List> loadMarkerImage() async {
    var byteData = await rootBundle.load("assets/pin.png");
    return byteData.buffer.asUint8List();
  }
}
