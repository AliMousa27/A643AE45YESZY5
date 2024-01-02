package com.toothtrek.bookings.view.Timeslot;

import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.entity.Timeslot;

/**
 * Interface for a view of a timeslot and its dentist.
 * <p>
 * Returns the view from the Timeslot repository.
 * 
 * @see com.toothtrek.bookings.repository.TimeslotRepository
 */
public interface TimeslotDentist {
    Dentist getDentist();

    Timeslot getTimeslot();
}
