package com.toothtrek.notifications.email;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.toothtrek.notifications.repository.BookingRepository;
import com.toothtrek.notifications.entity.Booking;

@Component
public class ReminderScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TemplateEngine templateEngine;


    @Scheduled(cron = "0 39 2 * * ?")
    public void sendReminder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfNextDay = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfNextDay = startOfNextDay.plusDays(1);
        Timestamp start = Timestamp.valueOf(startOfNextDay);
        Timestamp end = Timestamp.valueOf(endOfNextDay);
        List<Booking> bookings = bookingRepository.findBookingsByTimePeriod(start, end);
        for (Booking booking : bookings) {
            
            Context context = new Context();
            context.setVariable("patientName", booking.getPatient().getName());
            context.setVariable("dentistName", booking.getTimeslot().getDentist().getName());
            context.setVariable("officeName", booking.getTimeslot().getOffice().getName());
            context.setVariable("officeAddress", booking.getTimeslot().getOffice().getAddress());
            context.setVariable("dateAndTime", booking.getTimeslot().getDateAndTime().toString());

            String processedTemplate = templateEngine.process("reminderTemplate", context);
            emailService.sendNotificationEmail(booking.getPatient().getEmail(), "Dental Appointment Reminder",
                    processedTemplate);
        }
    }
}
