class Records {
  String notes;
  String date;
  String doctorName;

  Records(this.notes, this.date, this.doctorName);

  factory Records.fromJson(Map<String, dynamic> json) {
    String notes = json["notes"];
    String date = json["date"];
    String doctorName = json["doctor_name"];
    return Records(notes, date, doctorName);
  }
}
