import 'package:flutter/material.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/Map/map.dart';

import 'menu.dart';

class SideDrawer extends StatefulWidget {
  final List<DentistOffice> offices;
  ScrollController s = ScrollController();
  SideDrawer({Key? key, required this.offices}) : super(key: key);

  @override
  SideDrawerState createState() => SideDrawerState();
}

class SideDrawerState extends State<SideDrawer>
    with SingleTickerProviderStateMixin {
  late AnimationController drawerSlideController;
  GlobalKey<MenuState> menuKey = GlobalKey<MenuState>();

  @override
  void initState() {
    super.initState();
    drawerSlideController = AnimationController(
      vsync: this,
      duration: const Duration(milliseconds: 150),
    );
  }

  @override
  void dispose() {
    drawerSlideController.dispose();
    super.dispose();
  }

  bool _isDrawerOpen() {
    return drawerSlideController.value == 1.0;
  }

  bool isDrawerOpening() {
    return drawerSlideController.status == AnimationStatus.forward;
  }

  bool isDrawerClosed() {
    return drawerSlideController.value == 0.0;
  }

  void toggleDrawer() {
    if (_isDrawerOpen() || isDrawerOpening()) {
      drawerSlideController.reverse();
      DrawerState.notifyObserver(true);
    } else {
      drawerSlideController.forward();
      DrawerState.notifyObserver(false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.transparent,
      body: Stack(
        children: [
          buildDrawer(),
          buildButton(context),
        ],
      ),
    );
  }

  Widget buildButton(BuildContext context) {
    return Align(
      alignment: Alignment.topRight,
      child: AnimatedBuilder(
        animation: drawerSlideController,
        builder: (context, child) {
          return FloatingActionButton(
            backgroundColor: Colors.transparent,
            elevation: 0.0,
            hoverElevation: 0,
            onPressed: toggleDrawer,
            child: _isDrawerOpen() || isDrawerOpening()
                ? Icon(
                    Icons.clear,
                    color: Theme.of(context).colorScheme.onSurface,
                  )
                : Icon(
                    Icons.menu,
                    color: Theme.of(context).colorScheme.onSurface,
                  ),
          );
        },
      ),
    );
  }

  Widget buildDrawer() {
    return AnimatedBuilder(
      animation: drawerSlideController,
      builder: (context, child) {
        final isDrawerOpen = _isDrawerOpen();
        final translation =
            isDrawerOpen ? 0.0 : 1.0 - drawerSlideController.value;

        return FractionalTranslation(
          translation: Offset(translation, 0.0),
          child: Container(
            color: Colors.transparent,
            width: MediaQuery.of(context).size.width * 0.35,
            child: Stack(
              children: [
                if (!isDrawerClosed())
                  Menu(
                    key: menuKey,
                    offices: widget.offices,
                    scrollController: widget.s,
                  ),
                Positioned(
                  top: 0,
                  right: 0,
                  child: buildButton(context),
                ),
              ],
            ),
          ),
        );
      },
    );
  }

  void scrollToAndExpand(int officeIndex) {
    //open the menu and wait for 5 seconds
    drawerSlideController.forward();
    DrawerState.notifyObserver(false);
    //slight delay to build the menu widget
    Future.delayed(const Duration(milliseconds: 300), () {
      menuKey.currentState?.scrollToAndExpandTile(officeIndex);
    });
  }
}

class DrawerState extends ChangeNotifier {
  static late MapPageState observer;

  static void registerObserver(MapPageState mapPage) {
    observer = mapPage;
  }

  static void notifyObserver(bool newValue) {
    observer.notify(newValue);
  }
}
