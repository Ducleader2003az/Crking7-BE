package com.crking7.datn.web.admin;

import com.crking7.datn.helper.ApiResponse;
import com.crking7.datn.models.Notification;
import com.crking7.datn.services.ArticleService;
import com.crking7.datn.services.NotificationService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.NotificationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class ANotificationRest {
    private final NotificationService notificationService;

    public ANotificationRest(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/load-notification")
    public ResponseEntity<?> loadNotification() {
        List<NotificationResponse> notifications = notificationService.loadNotification();
        if(notifications != null){
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", notifications), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(ApiResponse.build(200, false, "Thất bại", "Không có thông báo nào"), HttpStatus.OK);
        }
    }

    @GetMapping("/read-notification")
    public ResponseEntity<?> readNotification(@RequestParam("id") Long id) {

        NotificationResponse notification = notificationService.modifyNotification(id);
        return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", notification), HttpStatus.OK);

    }

//    @GetMapping("/push-notification")
//    public ResponseEntity<?> pushNotification() {
//        List<NotificationResponse> notifications = notificationService.loadNotification(false, false);
//        for (NotificationResponse n : notifications) {
//            n.setDeliverStatus(true);
//            notificationService.updateNotification(n.getId());
//        }
//        return new ResponseEntity<>(ApiResponse.build(201, true, "thành công", notifications), HttpStatus.OK);
//
//    }
    @GetMapping("/push-notification")
    public ResponseEntity<?> pushNotification() {
        List<NotificationResponse> notifications = notificationService.pushNotification();
        if(notifications != null){
            return new ResponseEntity<>(ApiResponse.build(200, true, "thành công", notifications), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(ApiResponse.build(200, false, "Thất bại", "Không có thông báo nào"), HttpStatus.OK);
        }
    }
}
