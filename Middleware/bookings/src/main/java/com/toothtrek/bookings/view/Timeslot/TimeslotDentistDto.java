package com.toothtrek.bookings.view.Timeslot;

import com.toothtrek.bookings.entity.Dentist;
import com.toothtrek.bookings.entity.Timeslot;

/**
 * Data Transfer Object (DTO) for TimeslotDentist.
 * Used to serialize a TimeslotDentist view to JSON.
 * 
 * @see com.com.toothtrek.bookings.view.Timeslot.TimeslotDentist
 * @see com.toothtrek.bookings.repository.TimeslotRepository
 */
public class TimeslotDentistDto {
    private Timeslot timeslot;
    private Dentist dentist;

    /**
     * Converts a TimeslotDentist to a TimeslotDentistDto.
     * 
     * @param timeslotDentist The TimeslotDentist received from the repository.
     * @return TimeslotDentistDto object
     * @see com.com.toothtrek.bookings.view.Timeslot.TimeslotDentist
     * @see com.toothtrek.bookings.repository.TimeslotRepository
     */
    public static TimeslotDentistDto toDto(TimeslotDentist timeslotDentist) {
        TimeslotDentistDto dto = new TimeslotDentistDto();
        dto.setTimeslot(timeslotDentist.getTimeslot());
        dto.setDentist(timeslotDentist.getDentist());
        return dto;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public Dentist getDentist() {
        return dentist;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }

    public void setDentist(Dentist dentist) {
        this.dentist = dentist;
    }
}
