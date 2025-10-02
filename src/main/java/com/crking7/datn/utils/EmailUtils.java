package com.crking7.datn.utils;

import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;


@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private OtpService otpService;
    private String Otp;

    @Autowired
    public EmailUtils(JavaMailSender javaMailSender, OtpUtil otpUtil, OtpService otpService) {
        this.javaMailSender = javaMailSender;
        this.otpUtil = otpUtil;
        this.otpService = otpService;
    }

    public void sendOtpEmail(String email) throws MessagingException {
        Otp = otpUtil.generateOtp();
        otpService.storeOtp(email, Otp);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Xác Minh OTP");
        mimeMessageHelper.setText("""
                <div style="font-family: Arial, sans-serif;">
                  <p>Sử dụng mã OTP này để xác thực tài khoản của bạn:</p>
                  <h2>%s</h2>
                  <p>OTP này có giá trị 5 phút.</p>
                </div>
                """.formatted(Otp), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendPassword(String email, String password) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Thay Đổi Mật Khẩu");
        mimeMessageHelper.setText("""
                <div style="font-family: Arial, sans-serif;">
                  <p>Mật khẩu đã được thay đổi:</p>
                  <h2>%s</h2>
                  <a href="http://localhost:8080/api/login" target="_blank">Quay lại trang đăng nhập</a>
                </div>
                """.formatted(password), true);
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }

    public void sendMailOrder(String email, Orders orders) throws MessagingException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String createdDate = dateFormatter.format(orders.getModifiedDate());
        int totalOrderAmount = totalOrderAmount(orders);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Thông Báo Đặt Hàng Thành Công");
        StringBuilder sb = new StringBuilder()
                .append("<h3>Chúc mừng bạn " + orders.getUser().getUsername() + " đã đặt hàng thành công </h3>").append("<br/>")
                .append("Mã đơn hàng: " + orders.getCodeOrders()).append("<br/>")
                .append("Tổng tiền: " + totalOrderAmount+ " VNĐ").append("<br/>")
                .append("Ngày tạo: " + createdDate).append("<br/>")
                .append("Người nhận: " + orders.getFullName()).append("<br/>")
                .append("Số điện thoại: " + orders.getPhone()).append("<br/>")
                .append("Địa chỉ: " + orders.getAddressDetail())
                .append(", " + orders.getWards())
                .append(", " + orders.getDistrict())
                .append(", " + orders.getProvince()).append("<br/>")
                .append("Theo dõi trạng thái đơn hàng tại đây: ")
                .append("http://localhost:3000/order/detail/").append(Base64.getUrlEncoder().encodeToString(String.valueOf(orders.getId()).getBytes()));
        mimeMessageHelper.setText(sb.toString(), true);
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
    public void sendMailOrderCancel(String email, Orders orders) throws MessagingException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String createdDate = dateFormatter.format(orders.getModifiedDate());
        int totalOrderAmount = totalOrderAmount(orders);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Thông Báo Hủy Đơn Hàng");
        StringBuilder sb = new StringBuilder()
                .append("<h3>Bạn " + orders.getUser().getUsername() + " vùa hủy đơn hàng </h3>").append("<br/>")
                .append("Mã đơn hàng: " + orders.getCodeOrders()).append("<br/>")
                .append("Tổng tiền: " + totalOrderAmount+ " VNĐ").append("<br/>")
                .append("Ngày tạo: " + createdDate).append("<br/>")
                .append("Người nhận: " + orders.getFullName()).append("<br/>")
                .append("Số điện thoại: " + orders.getPhone()).append("<br/>")
                .append("Địa chỉ: " + orders.getAddressDetail())
                .append(", " + orders.getWards())
                .append(", " + orders.getDistrict())
                .append(", " + orders.getProvince()).append("<br/>")
                .append("Tiếp tục mua sắm đơn hàng tại đây: ")
                .append("http://localhost:3000/").append(Base64.getUrlEncoder().encodeToString(String.valueOf(orders.getId()).getBytes()));
        mimeMessageHelper.setText(sb.toString(), true);
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
    public void sendMailOrderShipping(String email, Orders orders) throws MessagingException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String createdDate = dateFormatter.format(orders.getModifiedDate());
        int totalOrderAmount = totalOrderAmount(orders);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Đơn Hàng Đang Trên Đường Đến Với Bạn");
        StringBuilder sb = new StringBuilder()
                .append("<h3>Đơn hàng của bạn " + orders.getUser().getUsername() + " đang trên đường giao đến bạn</h3>").append("<br/>")
                .append("Mã đơn hàng: " + orders.getCodeOrders()).append("<br/>")
                .append("Tổng tiền: " + totalOrderAmount + " VNĐ").append("<br/>")
                .append("Ngày tạo: " + createdDate).append("<br/>")
                .append("Người nhận: " + orders.getFullName()).append("<br/>")
                .append("Số điện thoại: " + orders.getPhone()).append("<br/>")
                .append("Địa chỉ: " + orders.getAddressDetail())
                .append(", " + orders.getWards())
                .append(", " + orders.getDistrict())
                .append(", " + orders.getProvince()).append("<br/>")
                .append("Bạn hãy chú ý điện thoại để nhận hàng sớm nhất. Theo dõi trạng thái đơn hàng tại đây: ")
                .append("http://localhost:3000/order/detail/").append(Base64.getUrlEncoder().encodeToString(String.valueOf(orders.getId()).getBytes()));
        mimeMessageHelper.setText(sb.toString(), true);
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
    public void sendMailOrderSuccess(String email, Orders orders) throws MessagingException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
        String createdDate = dateFormatter.format(orders.getCreateDate());
        int totalOrderAmount = totalOrderAmount(orders);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Cản Ơn Bạn Đã Đặt Hàng");
        StringBuilder sb = new StringBuilder()
                .append("<h3> Cảm ơn bạn" + orders.getUser().getUsername() + " đã đặt hàng</h3>").append("<br/>")
                .append("Cảm ơn bạn " + orders.getFullName() + " đã đặt hàng bên chúng tôi").append("<br/>")
                .append("Bạn có thể đổi trả trong vòng 7 ngày nếu gặp lỗi từ nhà sản xuất.").append("<br/>")
                .append("Nếu có vấn đề cần thắc mắc hãy liên hệ lại cho chúng tôi 0966821574.").append("<br/>")
                .append("Tiếp tục mua sắm tại đây: ")
                .append("http://localhost:3000/").append(Base64.getUrlEncoder().encodeToString(String.valueOf(orders.getId()).getBytes()));
        mimeMessageHelper.setText(sb.toString(), true);
        mimeMessageHelper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
    private int totalOrderAmount(Orders orders) {
        int totalAmount = 0;
        for (OrderItem item : orders.getOrderItems()) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }
}
