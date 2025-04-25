package com.slay.user.service.mail;

import com.slay.user.dto.response.user.ForgotPasswordDTO;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.ResourceBundle;


@Service
public class EmailService {

    private final KafkaTemplate<String, ForgotPasswordDTO> forgotPasswordKafkaTemplate;
    private final String TOPIC = "forgot-password-notifications";

    public EmailService(KafkaTemplate<String, ForgotPasswordDTO> forgotPasswordKafkaTemplate) {
        this.forgotPasswordKafkaTemplate = forgotPasswordKafkaTemplate;
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetUrl = "https://slaygym.ru/reset-password?token=" + token;

        Locale locale = LocaleContextHolder.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        String htmlContent = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Сброс пароля</title>
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f4f4f4;
                        margin: 0;
                        padding: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background-color: #ffffff;
                        padding: 20px;
                        border-radius: 8px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .diamond {
                        width: 150px;
                        height: 150px;
                        background-color: #007bff;
                        margin: 20px auto;
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        transform: rotate(45deg);
                        color: white;
                        font-size: 20px;
                        font-weight: bold;
                        border-radius: 8px;
                    }
                    .diamond span {
                        transform: rotate(-45deg);
                    }
                    .content {
                        text-align: center;
                        margin-bottom: 20px;
                    }
                    .button {
                        display: inline-block;
                        background-color: #007bff;
                        color: white;
                        text-decoration: none;
                        padding: 10px 20px;
                        border-radius: 5px;
                        font-size: 16px;
                        margin-top: 20px;
                    }
                    .button:hover {
                        background-color: #0056b3;
                    }
                </style>
            </head>
            <body>
                <div class="container">
            
                    <!-- <div class="diamond">
                        <span>Восстановление</span>
                    </div> -->

                    <!-- Текст -->
                    <div class="content">
                        <p>%s!</p>
                        <p>%s.</p>
                        <p>%s:</p>
                    </div>

                    <!-- Кнопка -->
                    <a href="%s">%s</a>

                    <!-- Дополнительный текст -->
                    <p style="text-align: center; margin-top: 20px;">
                        %s.
                    </p>
                </div>
            </body>
            </html>
            """.formatted(bundle.getString("HELLO"), bundle.getString("YOU_REQUESTED_RESET_PASSWORD"), bundle.getString("TO_RESET_PASSWORD"), resetUrl, bundle.getString("RESET_PASSWORD"), bundle.getString("IF_NOT_REQUEST"));

            forgotPasswordKafkaTemplate.send(TOPIC, ForgotPasswordDTO.builder()
                    .email(email)
                    .resetUrl(resetUrl)
                    .subject(bundle.getString("FORGOT_PASSWORD_REQUEST_SUBJECT"))
                    .htmlContent(htmlContent)
                    .build()
            );
    }

}