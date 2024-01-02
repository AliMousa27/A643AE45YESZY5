import 'package:flutter/material.dart';
import 'dentist_apointment.dart';
import 'expansion_tile.dart';

class Menu extends StatefulWidget {
  final List<DentistOffice> offices;
  late List<GlobalKey<CustomExpansionTileState>> keys;
  final ScrollController scrollController;
  Menu({Key? key, required this.offices, required this.scrollController})
      : super(key: key);

  @override
  State<Menu> createState() => MenuState();
}

class MenuState extends State<Menu> with SingleTickerProviderStateMixin {
  static const _initialDelayTime = Duration(milliseconds: 50);
  static const _itemSlideTime = Duration(milliseconds: 250);
  static const _staggerTime = Duration(milliseconds: 50);
  static const _buttonDelayTime = Duration(milliseconds: 150);
  static const _buttonTime = Duration(milliseconds: 500);
  late Duration _animationDuration;

  late AnimationController _staggeredController;
  final List<Interval> _itemSlideIntervals = [];

  @override
  void initState() {
    super.initState();
    _animationDuration = _initialDelayTime +
        (_staggerTime * widget.offices.length) +
        _buttonDelayTime +
        _buttonTime;
    _createAnimationIntervals();

    _staggeredController = AnimationController(
      vsync: this,
      duration: _animationDuration,
    )..forward();
  }

  void _createAnimationIntervals() {
    for (var i = 0; i < widget.offices.length; ++i) {
      final startTime = _initialDelayTime + (_staggerTime * i);
      final endTime = startTime + _itemSlideTime;
      _itemSlideIntervals.add(
        Interval(
          startTime.inMilliseconds / _animationDuration.inMilliseconds,
          endTime.inMilliseconds / _animationDuration.inMilliseconds,
        ),
      );
    }
  }

  @override
  void dispose() {
    _staggeredController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Expanded(
          child: ListView(
            controller: widget.scrollController,
            children: [
              Container(
                height: MediaQuery.of(context).size.height,
                color: Theme.of(context).colorScheme.primaryContainer,
                child: _buildContent(),
              ),
            ],
          ),
        ),
      ],
    );
  }

  Widget _buildContent() {
    return SingleChildScrollView(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: _buildListItems(),
      ),
    );
  }

  List<Widget> _buildListItems() {
    //creating keys to be assigned to tiles
    widget.keys = List.generate(
        widget.offices.length, (index) => GlobalKey<CustomExpansionTileState>(),
        growable: false);
    final listItems = <Widget>[];

    for (var i = 0; i < widget.offices.length; ++i) {
      listItems.add(
        Container(
          width: MediaQuery.of(context).size.width * 0.54,
          decoration: BoxDecoration(
            border: Border.all(
              color: Colors.grey,
              width: 1,
            ),
          ),
          child: AnimatedBuilder(
            animation: _staggeredController,
            builder: (context, child) {
              final animationPercent = Curves.easeOut.transform(
                _itemSlideIntervals[i].transform(_staggeredController.value),
              );
              final opacity = animationPercent;
              final slideDistance = (1.0 - animationPercent) * 150;

              return Opacity(
                opacity: opacity,
                child: Transform.translate(
                  offset: Offset(slideDistance, 0),
                  child: child,
                ),
              );
            },
            child: CustomExpansionTile(
              key: widget.keys[i],
              index: i,
              expanded: false,
              office: widget.offices[i],
              scrollController: widget.scrollController,
            ),
          ),
        ),
      );
    }
    return listItems;
  }

  void scrollToAndExpandTile(int index) {
    GlobalKey<CustomExpansionTileState> key = widget.keys[index];
    widget.scrollController.animateTo(
      100.0 * index,
      duration: const Duration(milliseconds: 500),
      curve: Curves.easeInOut,
    );
    key.currentState?.expandTile();
  }
}
