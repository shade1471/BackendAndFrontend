package ru.netology.api.utils;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLRequest {
    private static QueryRunner runner = new QueryRunner();

    @SneakyThrows
    public static Connection connection() {
        var conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "shade1471", "shade1471");
        return conn;
    }

    @SneakyThrows
    public static String getVerificationCodeFor(String login) {
        var codeSQL = "SELECT code" +
                " FROM auth_codes ac JOIN users u ON ac.user_id = u.id" +
                " WHERE login='" + login + "'" +
                " ORDER BY created DESC" +
                " LIMIT 1;";

        var code = runner.query(connection(), codeSQL, new ScalarHandler<>()).toString();
        return code;
    }

    @SneakyThrows
    public static void clearAll() {
        var cardsClearSQL = "DELETE FROM cards";
        var auth_codesClearSQL = "DELETE FROM auth_codes";
        var usersClearSQL = "DELETE FROM users";

        runner.update(connection(), cardsClearSQL);
        runner.update(connection(), auth_codesClearSQL);
        runner.update(connection(), usersClearSQL);
    }
}
