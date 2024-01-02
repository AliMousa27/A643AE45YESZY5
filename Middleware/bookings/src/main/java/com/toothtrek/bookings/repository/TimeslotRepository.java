package com.toothtrek.bookings.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.toothtrek.bookings.entity.Timeslot;
import com.toothtrek.bookings.view.Timeslot.TimeslotDentist;

public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {
    @Query("SELECT t FROM Timeslot t WHERE t.dentist.id = :dentistId AND t.office.id = :officeId AND t.dateAndTime = :dateAndTime")
    Timeslot findByDentistIdAndOfficeIdAndDateAndTime(Long dentistId, Long officeId, Timestamp dateAndTime);

    List<Timeslot> findByOfficeId(Long officeId);

    /**
     * Checks if a timeslot is booked. Returns true if it is, false if it isn't.
     * <p>
     * A timeslot is booked when a booking is in one of the following states:
     * "booked", "confirmed" or "completed".
     * <p>
     * Actual query uses != for "rejected" and "cancelled" states.
     * This is because it is more efficient to check for the NOT cases. 
     * (There are less of them.)
     * 
     * @param timeslotId The timeslot ID to check.
     * @return boolean
     */
    @Query("SELECT CASE WHEN (EXISTS (SELECT 1 FROM Booking b WHERE b.timeslot.id = :timeslotId AND (b.state != 'rejected' AND b.state != 'cancelled'))) THEN true ELSE false END")
    boolean isBooked(@Param("timeslotId") Long timeslotId);

    /**
     * Searches for timeslots based on the officeID.
     * <p>
     * Returns a list of TimeslotDentist objects.
     * <p>
     * TimeslotDentist is a view of a timeslot and its associated dentist.
     * 
     * @param officeId The office ID to search for.
     * @return List of TimeslotDentist
     * @see com.toothtrek.bookings.repository.view.TimeslotDentist
     */
    @Query("SELECT t as timeslot, d as dentist FROM Timeslot t JOIN Dentist d ON t.dentist.id = d.id WHERE t.office.id = :officeId")
    List<TimeslotDentist> findTimeslotsWithDentistsByOffice(@Param("officeId") Long officeId);
}