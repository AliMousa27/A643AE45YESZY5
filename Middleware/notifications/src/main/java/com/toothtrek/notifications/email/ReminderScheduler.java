package com.toothtrek.notifications.email;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.toothtrek.notifications.repository.NotificationRepository;
import com.toothtrek.notifications.entity.Notification;

@Component
public class ReminderScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 10 * * ?")
    public void sendReminder() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfNextDay = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfNextDay = startOfNextDay.plusDays(1);
        Timestamp start = Timestamp.valueOf(startOfNextDay);
        Timestamp end = Timestamp.valueOf(endOfNextDay);
        List<Notification> notifications = notificationRepository.findByTime(start, end);
        for (Notification notification : notifications) {
            emailService.sendNotificationEmail(notification.getEmail(), notification.getTitle(), notification.getMessage());
        }
    }
}
