package ru.netology.api.utils;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.api.data.DataGenerator;
import ru.netology.api.data.Transaction;

import static io.restassured.RestAssured.given;

public class ApiRequest {
    static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void loginUser(DataGenerator.UserLogin userLogin) {
        given() // "дано"
                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(userLogin))
                .when() // "когда"
                .post("/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }

    public static String verifyUser(DataGenerator.UserVerify user) {
        String token = given() // "дано"
                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(user))
                .when() // "когда"
                .post("/auth/verification") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200)
                .extract()
                .path("token");

        return token;
    }

    public static void transfer(String token, Transaction info) {
        given() // "дано"
                .headers("Authorization",
                        "Bearer " + token)
                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(info))
                .when() // "когда"
                .post("/transfer") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }

    public static void transferWrong(String token, Transaction info) {
        given() // "дано"
                .headers("Authorization",
                        "Bearer " + token)
                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(info))
                .when() // "когда"
                .post("/transfer") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(400);
    }

    public static int getCardBalance(String token, int number) {
        int balance = given()
                .headers("Authorization",
                        "Bearer " + token)// "дано"
                .spec(requestSpec)// указываем, какую спецификацию используем
                .when() // "когда"
                .get("/cards") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200)
                .extract()
                .path("[" + (number - 1) + "].balance");
        return balance;
    }
}
