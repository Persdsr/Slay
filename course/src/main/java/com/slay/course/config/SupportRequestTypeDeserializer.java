package com.slay.course.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.slay.course.enums.SupportRequestType;
import org.springframework.context.i18n.LocaleContextHolder;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SupportRequestTypeDeserializer extends JsonDeserializer<SupportRequestType> {

    @Override
    public SupportRequestType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String localizedValue = parser.getText();

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        for (SupportRequestType type : SupportRequestType.values()) {
            if (localizedValue.equals(bundle.getString(type.name()))) {
                return type;
            }
        }

        throw new IllegalArgumentException("Wrong value: " + localizedValue);
    }
}