package com.enterprise.automation.reports;

import io.qameta.allure.Attachment;

public final class AllureAttachment {
    private AllureAttachment() {
    }

    @Attachment(value = "Failure screenshot", type = "image/png")
    public static byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "Failure details", type = "text/plain")
    public static String attachText(String message) {
        return message;
    }

    @Attachment(value = "API payload", type = "application/json")
    public static String attachJson(String json) {
        return json;
    }
}
