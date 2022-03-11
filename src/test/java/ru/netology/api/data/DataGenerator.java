package ru.netology.api.data;

import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DataGenerator {

    static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost/api")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @SneakyThrows
    public static String getVerificationCodeFor(String login) {
        QueryRunner runner = new QueryRunner();
        var codeSQL = "SELECT code" +
                " FROM auth_codes ac JOIN users u ON ac.user_id = u.id" +
                " WHERE login='" + login + "'" +
                " ORDER BY created DESC" +
                " LIMIT 1;";
        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471"
                );
        ) {
            var code = runner.query(conn, codeSQL, new ScalarHandler<>()).toString();
            return code;
        }
    }

    public static void loginUser(User.UserLogin userLogin) {
        given() // "дано"

                .spec(requestSpec)// указываем, какую спецификацию используем
                .body(new Gson().toJson(userLogin))
                .when() // "когда"
                .post("/auth") // на какой путь, относительно BaseUri отправляем запрос
                .then()
                .statusCode(200);
    }

    public static String verifyUser(User.UserVerify user) {
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

//    public static int getNumberCard(){
//
//    }
}
