package com.crking7.datn.services.impl;

import com.crking7.datn.config.Constants;
import com.crking7.datn.mapper.ProductMapper;
import com.crking7.datn.models.*;
import com.crking7.datn.models.dtos.TopUserDto;
import com.crking7.datn.repositories.*;
import com.crking7.datn.services.NotificationService;
import com.crking7.datn.utils.EmailUtils;
import com.crking7.datn.utils.Utils;
import com.crking7.datn.web.dto.request.*;
import com.crking7.datn.web.dto.response.OrdersResponse;
import com.crking7.datn.mapper.OrderItemMapper;
import com.crking7.datn.mapper.OrdersMapper;
import com.crking7.datn.services.OrdersService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class OrdersServiceImpl implements OrdersService {
    private final OrdersRepository ordersRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final NotificationService notificationService;
    private final OrdersMapper ordersMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final EmailUtils emailUtils;

    public OrdersServiceImpl(OrdersRepository ordersRepository,
                             OrderItemRepository orderItemRepository,
                             UserRepository userRepository,
                             SizeRepository sizeRepository, ColorRepository colorRepository, OrdersMapper ordersMapper,
                             OrderItemMapper orderItemMapper, ProductMapper productMapper, ProductRepository productRepository,
                             NotificationService notificationService,
                             EmailUtils emailUtils) {
        this.ordersRepository = ordersRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.ordersMapper = ordersMapper;
        this.orderItemMapper = orderItemMapper;
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.notificationService = notificationService;
        this.emailUtils = emailUtils;
    }

    //lấy đơn hàng đã đặt
    @Override
    @Transactional
    public OrdersResponse getOrderByType(long userId, int type) {
        if (type != 0 && type != 1) {
            type = 1;
        }
        User user = userRepository.findById(userId).orElseThrow();
        Orders orders = ordersRepository.findByUserAndType(user, type);
        return ordersMapper.mapToResponse(orders);
    }

    @Override
    @Transactional
    public List<OrdersResponse> getOrders(long userId, int type) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Orders> orders = ordersRepository.findALlByUserAndType(user, type);
        return orders.stream()
                .map(ordersMapper::mapToResponse)
                .toList();
    }

    @Override
    public OrdersResponse getOrder(long orderId) {
        Orders orders = ordersRepository.findByIdAndType(orderId, Constants.ORDERS_TYPE);
        return ordersMapper.mapToResponse(orders);
    }

    //thêm vào giỏ hàng
    @Override
    @Transactional
    public Object addItemToCart(long userId, OrderItemRequest orderItemRequest) {
        User user = userRepository.findById(userId).orElseThrow();

        if (user != null) {
            Orders checkOrders = ordersRepository.findByUserAndType(user, Constants.CART_TYPE);
            Product product = productRepository.findByName(orderItemRequest.getProductName());
            Color color = colorRepository.findByValueAndProductId(orderItemRequest.getValueColor(), product.getId());
            Size size = sizeRepository.findByValueAndColorId(orderItemRequest.getValueSize(), color.getId());

            if (orderItemRequest.getQuantity() > size.getTotal()) {
                return String.format(
                        "Size %s không còn đủ số lượng. Vui lòng chọn ít hơn %d sản phẩm",
                        size.getValue(),
                        size.getTotal()
                );
            } else if (size.getTotal() == 0) {
                return "Size này đang hết hàng vui lòng chọn sản phẩm khác";
            } else {
                orderItemRequest.setSellPrice(product.getSalePrice());
                OrderItem orderItem = orderItemMapper.mapToModel(orderItemRequest);

                if (checkOrders == null) {
                    // Chưa có giỏ hàng, tạo giỏ hàng mới và lưu thông tin
                    Orders orders = new Orders();
                    initializeOrderAndSave(orders, user);
                    orderItem.setOrders(orders);
                    orderItemRepository.save(orderItem);
                    List<OrderItem> orderItem1 = orderItemRepository.findByOrders(orders);
                    orders.setOrderItems(orderItem1);
                    updateOrderAndShippingFee(orders);
                    return ordersMapper.mapToResponse(orders);
                } else {
                    OrderItem checkOrderItem = orderItemRepository.findByProductNameAndOrders(orderItemRequest.getProductName(), checkOrders);
                    if (checkOrderItem == null) {
                        // Có giỏ hàng nhưng chưa có sản phẩm đó
                        orderItem.setOrders(checkOrders);
                        orderItemRepository.save(orderItem);
                    } else {
                        // Có giỏ hàng và sản phẩm đó đã có
                        if (!checkOrderItem.getValueColor().equals(color.getValue()) || !checkOrderItem.getValueSize().equals(size.getValue())) {
                            // Nếu sản phẩm đã có nhưng màu sắc hoặc kích thước khác, thêm sản phẩm mới
                            OrderItem newOrderItem = new OrderItem();
                            newOrderItem.setProductName(orderItemRequest.getProductName());
                            newOrderItem.setQuantity(orderItemRequest.getQuantity());
                            newOrderItem.setSellPrice(orderItemRequest.getSellPrice());
                            newOrderItem.setOrders(checkOrders);
                            newOrderItem.setValueColor(orderItemRequest.getValueColor());
                            newOrderItem.setValueSize(orderItemRequest.getValueSize());
                            orderItemRepository.save(newOrderItem);
                        } else {
                            // Có giỏ hàng và sản phẩm đó đã có với màu sắc và kích thước giống nhau
                            checkOrderItem.setQuantity(orderItemRequest.getQuantity() + checkOrderItem.getQuantity());
                            if (checkOrderItem.getSellPrice() != orderItemRequest.getSellPrice()) {
                                checkOrderItem.setSellPrice(orderItemRequest.getSellPrice());
                            }
                            orderItemRepository.save(checkOrderItem);
                        }
                    }
                    List<OrderItem> orderItems = orderItemRepository.findByOrders(checkOrders);
                    checkOrders.setOrderItems(orderItems);
                    updateOrderAndShippingFee(checkOrders);
                    ordersRepository.save(checkOrders);
                    return ordersMapper.mapToResponse(checkOrders);
                }
            }
        } else {
            return "Bạn phải đăng nhập để mua hàng.";
        }
    }

    //xóa sản phẩm khỏi giỏ hàng
    @Override
    @Transactional
    public Object deleteItemFromCart(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow();
        Orders orders = ordersRepository.findById(orderItem.getOrders().getId()).orElseThrow();
        if (orderItem.getOrders() != null && orderItem.getOrders().equals(orders)) {
            orderItem.setOrders(null);
            orderItemRepository.save(orderItem);
            orderItemRepository.delete(orderItem);
            ordersRepository.save(orders);

            List<OrderItem> orderItemList = orderItemRepository.findByOrders(orders);
            if (orderItemList.isEmpty()) {
                ordersRepository.delete(orders);
                return "Giỏ hàng của bạn đang trống";
            } else {
                updateOrderAndShippingFee(orders, orderItemList);
                return ordersMapper.mapToResponse(orders);
            }
        } else {
            return "Không tìm thấy mặt hàng trong đơn hàng.";
        }
    }

    //trừ 1 sản phẩm
    @Override
    @Transactional
    public Object delete1Item(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow();
        Orders orders = ordersRepository.findById(orderItem.getOrders().getId()).orElseThrow();

        if (orderItem.getOrders() != null && orderItem.getOrders().equals(orders)) {
            if (orderItem.getQuantity() > 1) {
                orderItem.setQuantity(orderItem.getQuantity() - 1);
                orderItemRepository.save(orderItem);
            } else {
                orderItem.setOrders(null);
                orderItemRepository.save(orderItem);
                orderItemRepository.delete(orderItem);
            }

            updateOrderAndShippingFee(orders);
            ordersRepository.save(orders);
            List<OrderItem> orderItemList = orderItemRepository.findByOrders(orders);
            if (orderItemList.isEmpty()) {
                ordersRepository.delete(orders);
                return "Giỏ hàng của bạn đang trống";
            } else {
                orders.setOrderItems(orderItemList);
                return ordersMapper.mapToResponse(orders);
            }
        } else {
            return "Không tìm thấy mặt hàng trong đơn hàng.";
        }
    }

    //cộng 1 sản phẩm
    @Override
    @Transactional
    public Object plus1Item(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow();
        Orders orders = ordersRepository.findById(orderItem.getOrders().getId()).orElseThrow();
        Product product = productRepository.findByName(orderItem.getProductName());
        Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
        Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
        orderItem.setQuantity(orderItem.getQuantity() + 1);
        orderItemRepository.save(orderItem);
        if (orderItem.getQuantity() > size.getTotal()) {
            String errorMessage = String.format(
                    "Size %s không còn đủ số lượng. Vui lòng chọn ít hơn %d sản phẩm",
                    size.getValue(),
                    size.getTotal()
            );
            return errorMessage;
        } else if (size.getTotal() == 0) {
            return "Size này đang hết hàng vui lòng chọn sản phẩm khác";
        } else {
            orderItem.setSellPrice(product.getSalePrice());
            List<OrderItem> orderItems = orderItemRepository.findByOrders(orders);
            orders.setOrderItems(orderItems);
            updateOrderAndShippingFee(orders);
            ordersRepository.save(orders);
            return ordersMapper.mapToResponse(orders);
        }
    }

    @Override
    @Transactional
    public Object checkCreateOrder(Long orderId) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow();
        List<OrderItem> orderItems = orders.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            Product product = productRepository.findByName(orderItem.getProductName());
            Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
            Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
            int availableQuantity = size.getTotal();
            if (orderItem.getQuantity() > availableQuantity) {
                return String.format(
                        "Size %s của sản phẩm %s không còn đủ số lượng. Vui lòng chọn ít hơn %d sản phẩm",
                        size.getValue(),
                        orderItem.getProductName(),
                        availableQuantity
                );
            } else if (availableQuantity == 0) {
                return String.format(
                        "Size %s của sản phẩm %s không còn đủ số lượng. Vui lòng chọn sản phẩm khác",
                        size.getValue(),
                        orderItem.getProductName()
                );
            } else {
                return ordersMapper.mapToResponse(orders);
            }
        }
        return "Có thể thanh toán";
    }

    //đặt hàng
    @Override
    public Object createOrder(Long orderId, OrdersRequest ordersRequest) {
        Orders orders = ordersRepository.findById(orderId).orElseThrow();
        User user = userRepository.findById(orders.getUser().getId()).orElseThrow();
        List<OrderItem> orderItemList = orders.getOrderItems();
        if (orders.getType() == 0) {
            Date date = new Date();
            ordersMapper.updateModel(orders, ordersRequest);
            orders.setOrderItems(orderItemList);

            orders.setCreateDate(date);
            orders.setOrderDate(date);
            orders.setModifiedDate(date);
            if (orders.getCodeOrders() == null) {
                orders.setCodeOrders(Utils.getRandomNumber(8));
            }
            //đã thanh toán online
            if (orders.getPaymentMethod() != null) {
                //nếu đã thanh toán online thì satus là 2 chưa thì là 1
                orders.setIsCheckout(true);
                orders.setStatus(Constants.APPROVED_STATUS);
                orders.setType(Constants.ORDERS_TYPE);//xét là đơn hàng
                ordersRepository.save(orders);
            } else {
                orders.setStatus(Constants.PENDING_STATUS);
                orders.setType(Constants.ORDERS_TYPE);//xét là đơn hàng
                orders.setIsCheckout(false);
                orders.setPaymentMethod("COD");
                ordersRepository.save(orders);
            }
            List<OrderItem> orderItems = orders.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findByName(orderItem.getProductName());
                Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
                Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
                int availableQuantity = size.getTotal();
                if (orderItem.getQuantity() > availableQuantity) {
                    return String.format(
                            "Size %s của sản phẩm %s không còn đủ số lượng. Vui lòng chọn ít hơn %d sản phẩm",
                            size.getValue(),
                            orderItem.getProductName(),
                            availableQuantity
                    );
                } else if (availableQuantity == 0) {
                    return String.format(
                            "Size %s của sản phẩm %s không còn đủ số lượng. Vui lòng chọn sản phẩm khác",
                            size.getValue(),
                            orderItem.getProductName()
                    );
                } else {
                    size.setTotal(size.getTotal() - orderItem.getQuantity());
                    size.setSold(orderItem.getQuantity() + size.getSold());
                    sizeRepository.save(size);
                    orderItem.setOrders(orders);
                    orderItemRepository.save(orderItem);
                }
            }
            //lưu lại order
            ordersRepository.save(orders);
            //tạo thông báo
            Notification notification = new Notification();
            notification.setIsRead(false);
            notification.setDeliverStatus(false);
            notification.setContent(String.format("Đơn hàng %s vừa được tạo, xác nhận ngay nào", orders.getCodeOrders()));
            notification.setOrders(orders);
            notification.setType(1);
            notificationService.createNotification(notification);
            //gửi email
            try {
                emailUtils.sendMailOrder(user.getEmail(), orders);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return ordersMapper.mapToResponse(orders);
        } else {
            return "Đơn hàng của bạn đã được gửi đi để xác nhận";
        }
    }

    //khách hàng tự hủy
    @Override
    @Transactional
    public Object cancelOrder(CancelOrdersRequest cancelOrdersRequest) {
        Orders orders = ordersRepository.findById(cancelOrdersRequest.getOrderId()).orElseThrow();
        List<OrderItem> orderItems = orders.getOrderItems();
        Date date = new Date();
        if (orders.getStatus() == Constants.APPROVED_STATUS && orders.getIsCheckout()) {
            return "Đơn hàng của bạn đã được xác nhận và đã thanh toán";
        } else if (orders.getStatus() == Constants.SHIPPING_STATUS) {
            return "Đơn hàng của bạn đang trên đường vận chuyển đến bạn";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "Đơn hàng của bạn đã được giao thành công";
        } else if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "Đơn hàng của bạn đã được hủy";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "Bạn đã không nhận hàng";
        } else {
            orders.setModifiedDate(date);
            orders.setStatus(Constants.CANCEL_STATUS);
            orders.setNote(cancelOrdersRequest.getNote());
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findByName(orderItem.getProductName());
                Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
                Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
                size.setTotal(orderItem.getQuantity() + size.getTotal());
                size.setSold(orderItem.getQuantity() + size.getSold());
                sizeRepository.save(size);
            }
            //lưu lại order
            ordersRepository.save(orders);
            //tạo thông báo
            Notification notification = new Notification();
            notification.setIsRead(false);
            notification.setDeliverStatus(false);
            notification.setContent(String.format("Đơn hàng %s vừa được hủy, kiểm tra xem nào", orders.getId()));
            notification.setOrders(orders);
            notification.setType(2);
            notificationService.createNotification(notification);
            //gửi email
//        try {
//            emailUtils.sendMailOrderCancel(user.getEmail(), orders);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
            return "Hủy đơn hàng thành công";
        }
    }

    //xác nhận đơn hàng
    @Override
    @Transactional
    public Object confirmOrder(UDOrdersRequest udOrdersRequest) {
        Orders orders = ordersRepository.findById(udOrdersRequest.getOrderId()).orElseThrow();
        Date date = new Date();
        if (orders.getStatus() == Constants.APPROVED_STATUS && orders.getPaymentMethod() != null) {
            return "1";
        } else if (orders.getStatus() == Constants.SHIPPING_STATUS) {
            return "2";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "3";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "4";
        } else if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "5";
        } else {
            User user = userRepository.findByUsername(udOrdersRequest.getUserNameEmp());
            if (user != null) {
                orders.setUserNameEmp(user.getUsername());
            }
            orders.setStatus(Constants.APPROVED_STATUS);
            orders.setModifiedDate(date);
            ordersRepository.save(orders);
            return ordersMapper.mapToResponse(orders);
        }
    }

    //vận chuyển
    @Override
    @Transactional
    public Object shipOrder(UDOrdersRequest udOrdersRequest) {
        Orders orders = ordersRepository.findById(udOrdersRequest.getOrderId()).orElseThrow();
        User user = userRepository.findById(orders.getUser().getId()).orElseThrow();
        Date date = new Date();
        if (orders.getStatus() == Constants.PENDING_STATUS) {
            return "Đơn hàng này chưa được xác nhận! Hãy kiểm tra";
        } else if (orders.getStatus() == Constants.SHIPPING_STATUS) {
            return "Đơn hàng này đang được vận chuyển";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "Đơn hàng này đã được giao thành công";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "Đơn hàng này khách hàng đã từ chối nhận";
        } else if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "Đơn hàng này đã được hủy bời khách hàng";
        } else {
            User useremp = userRepository.findByUsername(udOrdersRequest.getUserNameEmp());
            if (useremp != null) {
                orders.setUserNameEmp(useremp.getUsername());
            }
            orders.setStatus(Constants.SHIPPING_STATUS);
            orders.setModifiedDate(date);
            ordersRepository.save(orders);

            //gửi email
            try {
                emailUtils.sendMailOrderShipping(user.getEmail(), orders);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return ordersMapper.mapToResponse(orders);
        }
    }

    //giao thành công
    @Override
    @Transactional
    public Object successOrder(UDOrdersRequest udOrdersRequest) {
        Orders orders = ordersRepository.findById(udOrdersRequest.getOrderId()).orElseThrow();
        User user = userRepository.findById(orders.getUser().getId()).orElseThrow();
        Date date = new Date();
        if (orders.getStatus() == Constants.APPROVED_STATUS) {
            return "Đơn hàng này chưa được giao đi! Hãy kiểm tra lại";
        } else if (orders.getStatus() == Constants.PENDING_STATUS) {
            return "Đơn hàng này chưa được xác nhận";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "Đơn hàng này đã được giao thành công";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "Đơn hàng này khách hàng đã từ chối nhận";
        } else if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "Đơn hàng này đã được hủy bời khách hàng";
        } else {
            User useremp = userRepository.findByUsername(udOrdersRequest.getUserNameEmp());
            if (useremp != null) {
                orders.setUserNameEmp(useremp.getUsername());
            }
            orders.setIsCheckout(true);
            orders.setStatus(Constants.DELIVERED_STATUS);
            orders.setModifiedDate(date);
            orders.setShipDate(udOrdersRequest.getShipDate());
            ordersRepository.save(orders);
            //gửi email
            try {
                emailUtils.sendMailOrderSuccess(user.getEmail(), orders);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            return ordersMapper.mapToResponse(orders);
        }
    }

    //giao ko thành công
    @Override
    @Transactional
    public Object cancelOrder(UDOrdersRequest udOrdersRequest) {
        Orders orders = ordersRepository.findById(udOrdersRequest.getOrderId()).orElseThrow();
        List<OrderItem> orderItems = orders.getOrderItems();
        Date date = new Date();
        User user = userRepository.findByUsername(udOrdersRequest.getUserNameEmp());
        if (user != null) {
            orders.setUserNameEmp(user.getUsername());
        }
        if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "Đơn hàng này đã được hủy";
        } else if (orders.getStatus() == Constants.APPROVED_STATUS && orders.getIsCheckout()) {
            return "Đơn hàng này đã được thanh toán!";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "Đơn hàng này khách hàng đã từ chối nhận";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "Đơn hàng này đã được giao thành công";
        } else if (orders.getStatus() == Constants.PENDING_STATUS || orders.getStatus() == Constants.APPROVED_STATUS) {
            orders.setStatus(Constants.CANCEL_STATUS);
            orders.setModifiedDate(date);
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findByName(orderItem.getProductName());
                Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
                Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
                size.setTotal(orderItem.getQuantity() + size.getTotal());
                size.setSold(orderItem.getQuantity() + size.getSold());
                sizeRepository.save(size);
            }
            ordersRepository.save(orders);
            return ordersMapper.mapToResponse(orders);
        } else {
            orders.setStatus(Constants.REFUSE_STATUS);
            orders.setModifiedDate(date);
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findByName(orderItem.getProductName());
                Color color = colorRepository.findByValueAndProductId(orderItem.getValueColor(), product.getId());
                Size size = sizeRepository.findByValueAndColorId(orderItem.getValueSize(), color.getId());
                size.setTotal(orderItem.getQuantity() + size.getTotal());
                size.setSold(orderItem.getQuantity() + size.getSold());
                sizeRepository.save(size);
            }
            ordersRepository.save(orders);
            return ordersMapper.mapToResponse(orders);
//            return "Khách hàng bom hàng";
        }
    }

    //cập nhật đơn hàng admin
    @Override
    public String updateOrder(UDOrdersRequestAdmin udOrdersRequest) {
        Orders orders = ordersRepository.findById(udOrdersRequest.getOrderId()).orElseThrow();
        Date date = new Date();
        User user = userRepository.findByUsername(udOrdersRequest.getUserNameEmp());
        if (user != null) {
            orders.setUserNameEmp(user.getUsername());
        }
        if (orders.getStatus() == Constants.CANCEL_STATUS) {
            return "1";
        } else if (orders.getStatus() == Constants.SHIPPING_STATUS) {
            return "2";
        } else if (orders.getStatus() == Constants.DELIVERED_STATUS) {
            return "3";
        } else if (orders.getStatus() == Constants.REFUSE_STATUS) {
            return "4";
        } else {
            orders.setStatus(Constants.APPROVED_STATUS);
            orders.setFullName(udOrdersRequest.getFullName());
            orders.setNote(udOrdersRequest.getNote());
            orders.setAddressDetail(udOrdersRequest.getAddressDetail());
            orders.setWards(udOrdersRequest.getWards());
            orders.setDistrict(udOrdersRequest.getDistrict());
            orders.setProvince(udOrdersRequest.getProvince());
            orders.setPhone(udOrdersRequest.getPhone());
            orders.setModifiedDate(date);
            ordersRepository.save(orders);
            return "Cập nhật đơn hàng thành công";
        }
    }

    //lấy tất cả đơn hàng theo status
    @Override
    public Pair<List<OrdersResponse>, Integer> getOrder(String keyword, Integer status, Boolean isCheckout, String startDate, String endDate, int pageNo, int pageSize, String sortBy, boolean asc) {
        Sort.Direction sortDirection = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sortDirection, sortBy);
        Date parsedStartDate = null;
        Date parsedEndDate = null;
        try {
            if (startDate != null) {
                parsedStartDate = parseDate("00:00:00 " + startDate);
            }
            if (endDate != null) {
                parsedEndDate = parseDate("23:59:59 " + endDate);
            }
        } catch (ParseException e) {
            // Xử lý exception nếu ngày tháng không hợp lệ
        }
        Page<Orders> ordersList = ordersRepository.getAllByStatus(keyword, status, isCheckout, parsedStartDate, parsedEndDate, pageable);
        int total = (int) ordersList.getTotalElements();
        List<OrdersResponse> ordersResponses = ordersList.getContent().stream()
                .map(ordersMapper::mapToResponse)
                .toList();
        return Pair.of(ordersResponses, total);
    }

    //Đếm số đơn hàng
    @Override
    public Long countOrders(Integer status) {
        return ordersRepository.countOrdersByStatus(status);
    }

    //tổng số sản phẩm đã bán
    @Override
    public Long getTotalSoldProducts() {
        return ordersRepository.totalSoldProducts();
    }

    //Tổng thu nhập
    @Override
    public Long totalInCome() {
        return ordersRepository.totalInCome();
    }

    //Tổng đơn hàng chưa được xử lý
    @Override
    public Long totalOrderNoProcess() {
        return ordersRepository.totalOrderNoProcess();
    }

    //Thống kê tỷ lệ chuyển đổi giữa giỏ hàng và đơn hàng
    @Override
    public double conversionRateType() {
        // Tính tỉ lệ chuyển đổi giữa giỏ hàng và đơn hàng đã đặt
        long totalCarts = ordersRepository.countByType(0);
        long totalPlacedOrders = ordersRepository.countByType(1);
        if (totalCarts == 0) {
            return 0.0;
        }
        return (double) totalPlacedOrders / totalCarts;
    }

    //thống kê thời gian xử lý đơn hàng
    @Override
    public long averageProcessingTime(int status) {
        return ordersRepository.calculateAverageProcessingTime(status);
    }

    //lấy đơn hàng theo status và theo tháng
    @Override
    public List<Map<String, Object>> getOrderByMonth(int status) {
        List<Object[]> objects = ordersRepository.findByMonth(status);
        List<Map<String, Object>> orders = new ArrayList<>();

        for (Object[] result : objects) {
            Map<String, Object> orderInfo = new HashMap<>();
            orderInfo.put("month", result[0]);
            orderInfo.put("status", result[1]);
            orderInfo.put("orderCount", result[2]);
            orders.add(orderInfo);
        }
        return orders;
    }

    //lấy tất cả đơn hàng
    @Override
    public List<OrdersResponse> getAllOrder(String keyword, int pageNo, int pageSize, String sortBy) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).descending());
        Page<Orders> orders = ordersRepository.findAllByKeyword(keyword, pageable);
        return orders.getContent().stream()
                .map(ordersMapper::mapToResponse)
                .toList();
    }

    //------------------Thêm sửa xóa giỏ hàng ---------------//
    private void initializeOrderAndSave(Orders orders, User user) {
        Date date = new Date();
        orders.setCreateDate(date);
        orders.setModifiedDate(date);
        orders.setUser(user);
        orders.setType(0);
        orders.setIsCheckout(false);
        ordersRepository.save(orders);
    }

    private void updateOrderAndShippingFee(Orders orders) {
        Date date = new Date();
        int totalOrderAmount = totalOrderAmount(orders);
        int shippingFee = (totalOrderAmount >= 500000) ? 0 : 25000;
        orders.setModifiedDate(date);
        orders.setShippingFee(shippingFee);
        ordersRepository.save(orders);
    }

    private int totalOrderAmount(Orders orders) {
        int totalAmount = 0;
        for (OrderItem item : orders.getOrderItems()) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }

    private void updateOrderAndShippingFee(Orders orders, List<OrderItem> orderItemList) {
        Date date = new Date();
        int totalOrderAmount = totalOrderAmount(orderItemList);
        int shippingFee = (totalOrderAmount >= 500000) ? 0 : 25000;

        orders.setModifiedDate(date);
        orders.setShippingFee(shippingFee);
        orders.setOrderItems(orderItemList);
        ordersRepository.save(orders);
    }

    private int totalOrderAmount(List<OrderItem> orderItemList) {
        int totalAmount = 0;
        for (OrderItem item : orderItemList) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }

    public Date parseDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.forLanguageTag("vi-VN"));
        return formatter.parse(date);
    }
}
