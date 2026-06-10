package com.enterprise.automation.base;

import com.enterprise.automation.config.ConfigManager;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public abstract class BaseApiTest {
    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        RestAssured.baseURI = ConfigManager.getRequired("api.base.url");
    }
}
