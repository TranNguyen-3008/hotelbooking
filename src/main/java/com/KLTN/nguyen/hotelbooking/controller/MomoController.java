package com.KLTN.nguyen.hotelbooking.controller;

import com.KLTN.nguyen.hotelbooking.dto.request.BookingRequest;
import com.KLTN.nguyen.hotelbooking.dto.request.MomoPaymentRequest;
import com.KLTN.nguyen.hotelbooking.dto.response.BookingResponse;
import com.KLTN.nguyen.hotelbooking.service.BookingService;
import com.KLTN.nguyen.hotelbooking.service.EmailService;
import com.KLTN.nguyen.hotelbooking.service.MomoService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/momo")
@AllArgsConstructor
public class MomoController {

    private MomoService momoService;
    private final BookingService bookingService;
    private final EmailService emailService;
    @PostMapping("/create")
    public ResponseEntity<?> createMomoPayment(@RequestBody MomoPaymentRequest request) {
        try {
            BookingRequest bookingRequest = BookingRequest.builder()
                    .email(request.getEmail())
                    .price(request.getPrice().doubleValue())
                    .startDate(LocalDate.parse(request.getStartDate()))
                    .endDate(LocalDate.parse(request.getEndDate()))
                    .roomId(request.getRoomId().intValue())
                    .build();
            BookingResponse bookingResponse = bookingService.MomoBooking(bookingRequest);
            request.setOrderId(bookingResponse.getId().toString() + request.getOrderId());
            String payUrl = momoService.createPaymentUrl(request);
            return ResponseEntity.ok().body(new PayUrlResponse(payUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new ErrorResponse("Không tạo được link thanh toán MoMo: " + e.getMessage()));
        }
    }
    @PostMapping("/ipn")
    @Transactional
    public ResponseEntity<String> handleMomoIPN(@RequestBody Map<String, Object> payload) {
        try {
            System.out.println("Received MoMo IPN payload: " + payload);
            String resultCode = String.valueOf(payload.get("resultCode"));
            String orderIdStr = String.valueOf(payload.get("orderId"));
            String numberPart = orderIdStr.replaceAll("^([0-9]+).*", "$1");
            Long orderId = Long.parseLong(numberPart);

            if ("0".equals(resultCode)) {
                BookingResponse bookingResponse = bookingService.updateBookingStatus(orderId.intValue(), "ĐÃ XÁC NHẬN");
                emailService.sendSimpleEmail(bookingResponse.getEmail(), "Xác nhận đặt phòng", "Đơn đặt phòng của bạn đã được tạo với id là " + orderId.intValue());

                return ResponseEntity.ok("IPN received and booking created.");
            }

            return ResponseEntity.ok("Payment failed or canceled.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error processing IPN: " + e.getMessage());
        }
    }
    static class PayUrlResponse {
        private String payUrl;
        public PayUrlResponse(String payUrl) { this.payUrl = payUrl; }
        public String getPayUrl() { return payUrl; }
        public void setPayUrl(String payUrl) { this.payUrl = payUrl; }
    }

    static class ErrorResponse {
        private String message;
        public ErrorResponse(String message) { this.message = message; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}