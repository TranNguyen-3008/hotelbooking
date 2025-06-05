package com.KLTN.nguyen.hotelbooking.service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.KLTN.nguyen.hotelbooking.dto.request.MomoPaymentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class MomoService {

    private final String endpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
    final String partnerCode = "MOMO";
    private final String accessKey = "F8BBA842ECF85";
    private final String secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
    private final String redirectUrl = "http://localhost:5173/booking-history";
    private final String ipnUrl = "https://a2ea-2405-4802-60e4-2550-217b-35e2-657f-d850.ngrok-free.app/api/momo/ipn";

    public String createPaymentUrl(MomoPaymentRequest request) throws Exception {
        String orderId = request.getOrderId();
        String orderInfo = request.getOrderInfo();
        BigDecimal amount = request.getPrice();
        String amountStr = String.valueOf(amount.setScale(0, RoundingMode.HALF_UP).intValue());
        String requestId = orderId;
        String extraData = "";

        String rawSignature =
                "accessKey=" + accessKey +
                        "&amount=" + amountStr +
                        "&extraData=" + extraData +
                        "&ipnUrl=" + ipnUrl +
                        "&orderId=" + orderId +
                        "&orderInfo=" + orderInfo +
                        "&partnerCode=" + partnerCode +
                        "&redirectUrl=" + redirectUrl +
                        "&requestId=" + requestId +
                        "&requestType=captureWallet";

        // Tạo chữ ký HMAC SHA256
        String signature = hmacSHA256(rawSignature, secretKey);

        // Tạo JSON gửi đi
        Map<String, Object> payload = new HashMap<>();
        payload.put("partnerCode", partnerCode);
        payload.put("accessKey", accessKey);
        payload.put("requestId", requestId);
        payload.put("amount", amountStr);
        payload.put("orderId", orderId);
        payload.put("orderInfo", orderInfo);
        payload.put("redirectUrl", redirectUrl);
        payload.put("ipnUrl", ipnUrl);
        payload.put("extraData", extraData);
        payload.put("requestType", "captureWallet");
        payload.put("signature", signature);
        payload.put("lang", "vi");

        // Gửi request POST
        ObjectMapper mapper = new ObjectMapper();
        String jsonPayload = mapper.writeValueAsString(payload);

        URL url = new URL(endpoint);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int code = con.getResponseCode();
        BufferedReader br;
        if (code == 200) {
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        } else {
            br = new BufferedReader(new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8));
        }
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        br.close();
        System.out.println("===== MoMo Request Payload =====");
        System.out.println(jsonPayload);
        // Parse JSON trả về lấy payUrl
        System.out.println("===== MoMo Response =====");
        System.out.println(response.toString());
        Map<String, Object> respMap = mapper.readValue(response.toString(), Map.class);
        if (respMap.containsKey("payUrl")) {
            return (String) respMap.get("payUrl");
        } else {
            throw new Exception("Không nhận được payUrl từ MoMo: " + response);
        }
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hmacData = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hmacData) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}