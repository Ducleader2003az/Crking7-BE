package com.crking7.datn.services;


import com.crking7.datn.models.Notification;
import com.crking7.datn.web.dto.response.NotificationResponse;

import java.util.List;

public interface NotificationService {
    List<NotificationResponse> loadNotification();
    NotificationResponse modifyNotification(Long id);
    void updateNotification(Long id);
    List<NotificationResponse> pushNotification();
    void createNotification(Notification notification);
}
