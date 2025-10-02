package com.crking7.datn.controller;

import com.crking7.datn.config.Constants;
import com.crking7.datn.config.ZaloPayConfig;
import com.crking7.datn.models.OrderItem;
import com.crking7.datn.models.Orders;
import com.crking7.datn.repositories.OrdersRepository;
import com.crking7.datn.services.OrdersService;
import com.crking7.datn.utils.HMACUtil;
import com.crking7.datn.utils.Utils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api/zalopay")
public class ZaloPayPaymentController {

    private final OrdersRepository ordersRepository;

    private final OrdersService ordersService;

    public ZaloPayPaymentController(OrdersRepository ordersRepository, OrdersService ordersService) {
        this.ordersRepository = ordersRepository;
        this.ordersService = ordersService;
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    @PostMapping(value = "/create-order")
    public Map<String, Object> createPayment(@RequestParam(name = "orderId") Long orderId) throws Exception {

        Orders orders = ordersRepository.findById(orderId).orElseThrow();
        long amount = (totalOrderAmount(orders) + orders.getShippingFee());
        String codeOrders = Utils.getRandomNumber(8);
        orders.setCodeOrders(codeOrders);
        ordersRepository.save(orders);
        String appuser = orders.getUser().getUsername();
        Map<String, Object> zalopay_Params = new HashMap<>();
        zalopay_Params.put("appid", ZaloPayConfig.APP_ID);
        zalopay_Params.put("apptransid", getCurrentTimeString("yyMMdd") + "_" + codeOrders);
        zalopay_Params.put("apptime", System.currentTimeMillis());
        zalopay_Params.put("appuser", appuser);
        zalopay_Params.put("amount", amount);
        zalopay_Params.put("description", "Thanh toan don hang #" + orders.getCodeOrders());
        zalopay_Params.put("bankcode", "");
        String item = "[{\"itemid\":\"" + orders.getId() + "\",\"codeOrders\":" + orders.getCodeOrders() + "\"\",\"itemprice\": " + amount + ",\"itemquantity\":" + orders.getOrderItems().size() + "}]";
        zalopay_Params.put("item", item);

        // embeddata
        // Trong trường hợp Merchant muốn trang cổng thanh toán chỉ hiện thị danh sách
        // các ngân hàng ATM,
        // thì Merchant để bankcode="" và thêm bankgroup = ATM vào embeddata như ví dụ
        // bên dưới
        // embeddata={"bankgroup": "ATM"}
        // bankcode=""
        Map<String, String> embeddata = new HashMap<>();
        embeddata.put("merchantinfo", "crking7");
        embeddata.put("promotioninfo", "");
        embeddata.put("redirecturl", ZaloPayConfig.REDIRECT_URL);

        Map<String, String> columninfo = new HashMap<String, String>();
        columninfo.put("store_name", "CRKing7");
        embeddata.put("columninfo", new JSONObject(columninfo).toString());
        zalopay_Params.put("embeddata", new JSONObject(embeddata).toString());

        String data = zalopay_Params.get("appid") + "|" + zalopay_Params.get("apptransid") + "|"
                + zalopay_Params.get("appuser") + "|" + zalopay_Params.get("amount") + "|"
                + zalopay_Params.get("apptime") + "|" + zalopay_Params.get("embeddata") + "|"
                + zalopay_Params.get("item");
        zalopay_Params.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, ZaloPayConfig.KEY1, data));
//		zalopay_Params.put("phone", order.getPhone());
//		zalopay_Params.put("email", order.getEmail());
//		zalopay_Params.put("address", order.getAddress());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(ZaloPayConfig.CREATE_ORDER_URL);

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : zalopay_Params.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }
        post.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }
        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> kq = new HashMap<String, Object>();
        kq.put("returnmessage", result.get("returnmessage"));
        kq.put("orderurl", result.get("orderurl"));
        kq.put("returncode", result.get("returncode"));
        kq.put("zptranstoken", result.get("zptranstoken"));
        return kq;
    }

    @GetMapping(value = "/getstatusbyapptransid")
    public Map<String, Object> getStatusByApptransid(@RequestParam(name = "apptransid") String apptransid) throws Exception {
        String[] appTransIdParts = apptransid.split("_");
        String codeOrders = appTransIdParts[1];
        Orders orders = ordersRepository.findByCodeOrders(codeOrders);

        String appid = ZaloPayConfig.APP_ID;
        String key1 = ZaloPayConfig.KEY1;
        String data = appid + "|" + apptransid + "|" + key1; // appid|apptransid|key1
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, key1, data);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("appid", appid));
        params.add(new BasicNameValuePair("apptransid", apptransid));
        params.add(new BasicNameValuePair("mac", mac));

        URIBuilder uri = new URIBuilder(ZaloPayConfig.GET_STATUS_PAY_URL);
        uri.addParameters(params);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(uri.build());

        CloseableHttpResponse res = client.execute(get);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        JSONObject result = new JSONObject(resultJsonStr.toString());
        Map<String, Object> kq = new HashMap<String, Object>();
        kq.put("returncode", result.get("returncode"));
        kq.put("returnmessage", result.get("returnmessage"));
        kq.put("isprocessing", result.get("isprocessing"));
        kq.put("amount", result.get("amount"));
        kq.put("discountamount", result.get("discountamount"));
        kq.put("zptransid", result.get("zptransid"));
        // Cập nhật trạng thái isCheckout và hình thức thanh toán
        if ("1".equals(result.get("returncode").toString())) {
            // 1 là mã trả về cho thanh toán thành công
            orders.setPaymentMethod("ZaloPay");
            // Lưu thông tin vào cơ sở dữ liệu
            ordersRepository.save(orders);
        }
        return kq;
    }

    private int totalOrderAmount(Orders orders) {
        int totalAmount = 0;
        for (OrderItem item : orders.getOrderItems()) {
            totalAmount += item.getQuantity() * item.getSellPrice();
        }
        return totalAmount;
    }
}
