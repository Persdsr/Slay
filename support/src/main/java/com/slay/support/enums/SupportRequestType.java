package com.slay.support.enums;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.slay.support.config.SupportRequestTypeDeserializer;

@JsonDeserialize(using = SupportRequestTypeDeserializer.class)
public enum SupportRequestType {
        TECHNICAL_SUPPORT, INFORMATION_REQUEST, BILLING_ISSUE
    }