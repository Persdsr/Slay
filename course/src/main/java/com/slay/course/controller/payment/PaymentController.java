package com.slay.course.controller.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slay.course.DTO.request.payment.PaymentNotification;
import com.slay.course.DTO.request.payment.PaymentRequest;
import com.slay.course.service.training.TrainingCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@Slf4j
@Tag(name = "Training course")
public class PaymentController {

    private final TrainingCourseService trainingCourseService;


    @Value("${yookassa.shop-id}")
    private String shopId;

    @Value("${yookassa.secret-key}")
    private String secretKey;

    public PaymentController(TrainingCourseService trainingCourseService) {
        this.trainingCourseService = trainingCourseService;
    }

    @Operation(
            summary = "Создать платеж через YooKassa",
            description = "Создает платеж через API YooKassa для оплаты курса. Возвращает данные для перенаправления пользователя на страницу оплаты."
    )
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public ResponseEntity<?> handleCreate(@RequestBody Map<String, String> data) {
        String url = "https://api.yookassa.ru/v3/payments";
        String idempotenceKey = UUID.randomUUID().toString();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);

            PaymentRequest request = new PaymentRequest();
            PaymentRequest.Amount amount = new PaymentRequest.Amount();
            amount.setValue(data.get("price"));
            amount.setCurrency("RUB");
            request.setAmount(amount);

            request.setCapture(true);

            PaymentRequest.Confirmation confirmation = new PaymentRequest.Confirmation();
            confirmation.setType("redirect");
            confirmation.setReturn_url("https://slaygym.ru/course/" + data.get("courseId"));
            request.setConfirmation(confirmation);

            Map<String, String> metadata = new HashMap<>();
            metadata.put("courseId", data.get("courseId"));
            metadata.put("buyerUsername", data.get("buyerUsername"));
            request.setMetadata(metadata);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(request);

            StringEntity entity = new StringEntity(jsonBody);
            httpPost.setEntity(entity);

            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Idempotence-Key", idempotenceKey);

            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new org.apache.http.auth.AuthScope(
                            org.apache.http.auth.AuthScope.ANY_HOST,
                            org.apache.http.auth.AuthScope.ANY_PORT),
                    new UsernamePasswordCredentials(shopId, secretKey)
            );

            HttpContext context = HttpClientContext.create();
            ((HttpClientContext) context).setCredentialsProvider(credentialsProvider);

            HttpResponse response = httpClient.execute(httpPost, context);
            HttpEntity responseEntity = response.getEntity();
            String responseString = EntityUtils.toString(responseEntity);

            Map<String, Object> responseBody = objectMapper.readValue(responseString, Map.class);

            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ошибка при создании платежа"));
        }
    }


    @Operation(hidden = true)
    @PostMapping("/webhook")
    public HttpStatus handleWebhook(@RequestBody String rawNotification) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PaymentNotification notification = objectMapper.readValue(rawNotification, PaymentNotification.class);

            if ("succeeded".equals(notification.getObject().getStatus())) {

                int courseId = Integer.parseInt(notification.getObject().getMetadata().getCourseId());
                String buyerUsername = notification.getObject().getMetadata().getBuyerUsername();

                trainingCourseService.handleBuyTrainingCourse(courseId, buyerUsername);
                return HttpStatus.OK;
            }
        } catch (Exception e) {
            log.error("Error processing webhook", e);
        }
        return HttpStatus.BAD_REQUEST;
    }

}