package ru.netology.api;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import ru.netology.api.data.DataGenerator;
import ru.netology.api.data.User;

import static io.restassured.RestAssured.given;

public class MoneyTransferTest {

    @Test
    void login(){
        DataGenerator.login(new User("vasya","qwerty123"));
    }
}
