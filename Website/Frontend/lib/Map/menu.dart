//side menu code documentation
//https://docs.flutter.dev/cookbook/effects/staggered-menu-animation
import 'package:flutter/material.dart';
import 'package:flutter_application/Map/dentist_apointment.dart';
import 'package:flutter_application/Map/map_page_util.dart';

class Menu extends StatefulWidget {
  final List<DentistOffice> offices;
  const Menu({Key? key, required this.offices}) : super(key: key);

  @override
  State<Menu> createState() => _MenuState();
}

class _MenuState extends State<Menu> with SingleTickerProviderStateMixin {
  //Todo request the data and popualte it in init state probably
  DateTime? selectedDate;
  DentistOfficeController dentistOfficeController = DentistOfficeController();
  static const _menuTitles = [
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
    'Some Office',
  ];

  static const _initialDelayTime = Duration(milliseconds: 50);
  static const _itemSlideTime = Duration(milliseconds: 250);
  static const _staggerTime = Duration(milliseconds: 50);
  static const _buttonDelayTime = Duration(milliseconds: 150);
  static const _buttonTime = Duration(milliseconds: 500);
  final _animationDuration = _initialDelayTime +
      (_staggerTime * _menuTitles.length) +
      _buttonDelayTime +
      _buttonTime;

  late AnimationController _staggeredController;
  final List<Interval> _itemSlideIntervals = [];

  @override
  void initState() {
    super.initState();
    _createAnimationIntervals();

    _staggeredController = AnimationController(
      vsync: this,
      duration: _animationDuration,
    )..forward();
  }

  void _createAnimationIntervals() {
    for (var i = 0; i < _menuTitles.length; ++i) {
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
        const SizedBox(height: 65),
        Expanded(
          child: ListView(
            children: [
              Container(
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
    return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: _buildListItems());
  }

  List<Widget> _buildListItems() {
    print("the passed in offices are ${widget.offices}");
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
              child: MapUtil.createListCalendars(context, widget.offices[i],
                  (DateTime date) {
                selectedDate = date;
              }, bookApoinment)),
        ),
      );
    }
    return listItems;
  }

  void bookApoinment() {
    //TODO send request to server
    //TODO show confirmation
    print(selectedDate);
  }
}
