import 'package:flutter/material.dart';

import 'map.dart';
import 'map_page_util.dart';

class BottomSheetMenu extends StatefulWidget {
  const BottomSheetMenu({super.key});

  @override
  State<BottomSheetMenu> createState() => BottomSheetMenuState();
}

class BottomSheetMenuState extends State<BottomSheetMenu> {
  final _sheet = GlobalKey();
  DateTime? selectedDate;
  final _controller = DraggableScrollableController();
  static const _menuTitles = [
    'Declarative style',
    'Premade widgets',
    'Stateful hot reload',
    'Native performance',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
    'Great community',
  ];
  @override
  void initState() {
    super.initState();
    _controller.addListener(_onChanged);
  }

  void _onChanged() {
    final currentSize = _controller.size;
    BottomSheetState.notifyObserver(currentSize);
    if (currentSize <= 0.05) _collapse();
  }

  void _collapse() => _animateSheet(sheet.snapSizes!.first);

  void anchor() => _animateSheet(sheet.snapSizes!.last);

  void expand() => _animateSheet(sheet.maxChildSize);

  void hide() => _animateSheet(sheet.minChildSize);

  void _animateSheet(double size) {
    _controller.animateTo(
      size,
      duration: const Duration(milliseconds: 50),
      curve: Curves.easeInOut,
    );
  }

  @override
  void dispose() {
    super.dispose();
    _controller.dispose();
  }

  DraggableScrollableSheet get sheet =>
      (_sheet.currentWidget as DraggableScrollableSheet);

  @override
  Widget build(BuildContext context) {
    return createBottomsheet(context, MediaQuery.of(context).size.width,
        MediaQuery.of(context).size.height);
  }

  DraggableScrollableSheet createBottomsheet(
      BuildContext context, double screenWidth, double screenHeight) {
    final controller = DraggableScrollableController();

    controller.addListener(() {
      final currentSize = controller.size;
      BottomSheetState.notifyObserver(currentSize);
      if (currentSize <= 0.05) _collapse();
    });

    return DraggableScrollableSheet(
      key: _sheet, // Use the existing key
      initialChildSize: 0.5,
      maxChildSize: 0.75,
      minChildSize: 0.1,
      expand: true,
      snap: true,
      snapSizes: [
        67 / screenHeight,
      ],

      controller: controller,
      builder: (BuildContext context, ScrollController scrollController) {
        return DecoratedBox(
          decoration: BoxDecoration(
            color: Theme.of(context).colorScheme.primaryContainer,
          ),
          child: SingleChildScrollView(
              controller: scrollController,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: _buildListItems(),
              )),
        );
      },
    );
  }

  List<Widget> _buildListItems() {
    final listItems = <Widget>[];
    for (var i = 0; i < _menuTitles.length; ++i) {
      listItems.add(
        Container(
            width: MediaQuery.of(context).size.width,
            decoration: BoxDecoration(
              border: Border.all(
                color: Colors.grey,
                width: 1,
              ),
            ),
            child: MapUtil.createListCalendars(context, _menuTitles[i],
                (DateTime date) {
              selectedDate = date;
            }, bookApoinment)),
      );
    }
    return listItems;
  }

  void bookApoinment() {
    //TODO: implement bookApoinment
    print("selected date is $selectedDate");
  }
}

class BottomSheetState extends ChangeNotifier {
  static late MapPageState observer;

  static void registerObserver(MapPageState bottomSheet) {
    observer = bottomSheet;
  }

  static void notifyObserver(double newValue) {
    observer.notify(newValue);
  }
}
