import 'package:flutter/material.dart';

import 'dentist_apointment.dart';
import 'expansion_tile.dart';
import 'map.dart';

class BottomSheetMenu extends StatefulWidget {
  final List<DentistOffice> offices;
  late final List<GlobalKey<CustomExpansionTileState>> keys;
  BottomSheetMenu({Key? key, required this.offices}) : super(key: key);

  @override
  State<BottomSheetMenu> createState() => BottomSheetMenuState();
}

class BottomSheetMenuState extends State<BottomSheetMenu> {
  final _sheet = GlobalKey();
  final ScrollController scrollController = ScrollController();
  DateTime? selectedDate;
  final _controller = DraggableScrollableController();

  @override
  void initState() {
    super.initState();
    _controller.addListener(_onChanged);
  }

  void _onChanged() {
    final currentSize = _controller.size;
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

  Widget createBottomsheet(
      BuildContext context, double screenWidth, double screenHeight) {
    return Container(
      height: screenHeight * 0.4, // Adjust this value as needed
      decoration: BoxDecoration(
        color: Theme.of(context).colorScheme.primaryContainer,
      ),
      child: SingleChildScrollView(
        controller: scrollController,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: _buildListItems(),
        ),
      ),
    );
  }

  List<Widget> _buildListItems() {
    widget.keys = List.generate(
        widget.offices.length, (index) => GlobalKey<CustomExpansionTileState>(),
        growable: false);
    final listItems = <Widget>[];
    for (var i = 0; i < widget.offices.length; ++i) {
      listItems.add(
        Container(
            width: MediaQuery.of(context).size.width,
            decoration: BoxDecoration(
              border: Border.all(
                color: Colors.grey,
                width: 1,
              ),
            ),
            child: CustomExpansionTile(
              key: widget.keys[i],
              index: i,
              expanded: false,
              office: widget.offices[i],
              scrollController: scrollController,
            )),
      );
    }
    return listItems;
  }

  void scrollToAndExpandTile(int index) {
    GlobalKey<CustomExpansionTileState> key = widget.keys[index];
    scrollController.animateTo(
      100.0 * index,
      duration: const Duration(milliseconds: 500),
      curve: Curves.easeInOut,
    );
    key.currentState?.expandTile();
  }

  void bookApoinment() {
    //TODO: implement bookApoinment
    print("selected date is $selectedDate");
  }
}
