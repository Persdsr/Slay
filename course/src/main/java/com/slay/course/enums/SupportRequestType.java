package com.slay.course.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.slay.course.config.SupportRequestTypeDeserializer;

@JsonDeserialize(using = SupportRequestTypeDeserializer.class)
public enum SupportRequestType {
        TECHNICAL_SUPPORT, INFORMATION_REQUEST, BILLING_ISSUE
    }