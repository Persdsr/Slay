package com.slay.course.config;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    public static void load() {
        Dotenv dotenv = Dotenv.configure()
                .load();

        System.setProperty("DOMAIN", dotenv.get("DOMAIN"));
        System.setProperty("HTTP_PROTOCOL", dotenv.get("HTTP_PROTOCOL"));
        System.setProperty("SHOP_ID", dotenv.get("SHOP_ID"));
        System.setProperty("SHOP_SECRET_KEY", dotenv.get("SHOP_SECRET_KEY"));

        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

        System.setProperty("REDIS_HOST", dotenv.get("REDIS_HOST"));
        System.setProperty("REDIS_PORT", dotenv.get("REDIS_PORT"));

        System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
        System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        System.setProperty("APP_SECRET", dotenv.get("APP_SECRET"));
    }
}