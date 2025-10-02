package com.crking7.datn.mapper;

import com.crking7.datn.models.Article;
import com.crking7.datn.models.ArticleImage;
import com.crking7.datn.models.Notification;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.response.ArticleImageResponse;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.NotificationResponse;
import org.mapstruct.*;

@Mapper
public interface NotificationMapper {
    @Mapping(target = "orders", source = "orders.id")
    @Mapping(target = "product", source = "product.id")
    NotificationResponse mapToResponse(Notification notification);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateModel(@MappingTarget Notification notification, NotificationResponse notificationResponse);
}
