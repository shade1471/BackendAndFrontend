package ru.netology.api.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    public static void login(User user){
                given() // "дано"

                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(new User(user.getLogin(), user.getPassword()))) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }
}
