package ru.netology.api;

import org.junit.jupiter.api.Test;
import ru.netology.api.data.Transaction;
import ru.netology.api.data.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.api.data.DataGenerator.*;
import static ru.netology.api.data.User.Card.getCard;

public class MoneyTransferTest {
    private User.UserLogin existUser = User.UserLogin.getUser();
//    private String token = User.getToken();
    private String token = loginVerifyAndGetToken();


    public String loginVerifyAndGetToken() {
        loginUser(existUser);
        String code = getVerificationCodeFor(existUser.getLogin());
        String token = verifyUser(new User.UserVerify(existUser.getLogin(), code));
        return token;
    }

    public void refresh(String token) {
        int diff = getCardBalance(token, 1) - 10000;
        if (diff != 0) {
            if (diff < 0) {
                transfer(token,
                        new Transaction("5559 0000 0000 0001", "5559 0000 0000 0002", Math.abs(diff)));
            } else {
                transfer(token,
                        new Transaction("5559 0000 0000 0002", "5559 0000 0000 0001", Math.abs(diff)));
            }
        }
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
        transfer(token, new Transaction(getCard(1).getCardNumber(), "5559 0000 0000 0008", amount));
        int actualFirstCard = getCardBalance(token, 1);
        assertEquals(currentBalanceCardOne - amount, actualFirstCard);
    }

    @Test
    void loginAndTransferMoneyIfNotEnoughBalance() {
        refresh(token);
        transferWrong(token, new Transaction(getCard(2).getCardNumber(), getCard(1).getCardNumber(), 15000));
    }
}
