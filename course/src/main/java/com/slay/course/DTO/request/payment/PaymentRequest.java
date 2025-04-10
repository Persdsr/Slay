package com.slay.course.DTO.request.payment;

import lombok.Data;

import java.util.Map;

@Data
public class PaymentRequest {
    private Amount amount;
    private boolean capture;
    private Confirmation confirmation;
    private String description;
    private Map<String, String> metadata;

    @Data
    public static class Amount {
        private String value;
        private String currency;
    }

    @Data
    public static class Confirmation {
        private String type;
        private String return_url;

    }
}
