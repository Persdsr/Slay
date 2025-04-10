package com.slay.course.DTO.request.payment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentNotification {

    private PaymentObject object;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentObject {
        private String id;
        private String status;
        private Metadata metadata;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private String buyerUsername;
        private String courseId;
    }


}