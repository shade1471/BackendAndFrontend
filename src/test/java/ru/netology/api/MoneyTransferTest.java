package ru.netology.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.api.data.DataGenerator;
import ru.netology.api.data.Transaction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.api.utils.ApiRequest.*;
import static ru.netology.api.data.DataGenerator.Card.getCard;
import static ru.netology.api.data.DataGenerator.Card.getUnknownCard;
import static ru.netology.api.data.DataGenerator.UserLogin.getUser;
import static ru.netology.api.utils.SQLRequest.clearAll;
import static ru.netology.api.utils.SQLRequest.getVerificationCodeFor;

public class MoneyTransferTest {
    private DataGenerator.UserLogin existUser = getUser();
    private String tokenFinally = DataGenerator.getToken();
    private String token = loginVerifyAndGetToken();


    public String loginVerifyAndGetToken() {
        loginUser(existUser);
        String code = getVerificationCodeFor(existUser.getLogin());
        String token = verifyUser(new DataGenerator.UserVerify(existUser.getLogin(), code));
        return token;
    }

    public void refresh(String token) {
        int diff = getCardBalance(token, 1) - 10000;
        if (diff != 0) {
            if (diff < 0) {
                transfer(token,
                        new Transaction(getCard(2).getCardNumber(), getCard(1).getCardNumber(), Math.abs(diff)));
            } else {
                transfer(token,
                        new Transaction(getCard(1).getCardNumber(), getCard(2).getCardNumber(), Math.abs(diff)));
            }
        }
    }

    @AfterAll
    public static void clear() {
        clearAll();
    }

    @Test
    void loginAndTransferMoneyBetweenOwnCards() {
        refresh(token);
        int amount = 5000;
        int currentBalanceCardOne = getCardBalance(token, 1);
        int currentBalanceCardTwo = getCardBalance(token, 2);
        transfer(token, new Transaction(getCard(1).getCardNumber(), getCard(2).getCardNumber(), amount));
        int actualFirstCard = getCardBalance(token, 1);
        int actualSecondCard = getCardBalance(token, 2);
        assertEquals(currentBalanceCardOne - amount, actualFirstCard);
        assertEquals(currentBalanceCardTwo + amount, actualSecondCard);
    }

    @Test
    void loginAndTransferMoneyForDifferentUser() {
        refresh(token);
        int amount = 5000;
        int currentBalanceCardOne = getCardBalance(token, 1);
        transfer(token, new Transaction(getCard(1).getCardNumber(), getUnknownCard().getCardNumber(), amount));
        int actualFirstCard = getCardBalance(token, 1);
        assertEquals(currentBalanceCardOne - amount, actualFirstCard);
    }

    @Test
    void loginAndTransferMoneyIfNotEnoughBalance() {
        refresh(token);
        transferWrong(token, new Transaction(getCard(2).getCardNumber(), getCard(1).getCardNumber(), 15000));
    }
}
