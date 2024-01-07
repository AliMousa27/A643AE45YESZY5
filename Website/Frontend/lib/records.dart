class Records {
  String notes;
  String date;
  String doctorName;

  Records(this.notes, this.date, this.doctorName);

  factory Records.fromJson(Map<String, dynamic> json) {
    String notes = json["notes"];
    String date = json["timeslot"]["date_and_time"];
    String doctorName = json["timeslot"]["dentist"]["name"];
    return Records(notes, date, doctorName);
  }
}
