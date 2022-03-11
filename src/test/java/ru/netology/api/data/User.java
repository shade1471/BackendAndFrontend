package ru.netology.api.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

public class User {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLogin {
        private String login;
        private String password;

        public static UserLogin getUser() {
            return new UserLogin("vasya", "qwerty123");
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserVerify {
        private String login;
        private String code;
    }

    @Value
    public static class Card {
        private String cardId;
        private String cardNumber;

        private static Card[] cards =
                {new Card("0f3f5c2a-249e-4c3d-8287-09f7a039391d", "5559 0000 0000 0002"),
                        new Card("92df3f1c-a033-48e6-8390-206f6b1f56c0", "5559 0000 0000 0001")};

        public static Card getCard(int number) {
            return cards[number - 1];
        }
    }

    public static String getToken(){
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbiI6InZhc3lhIn0.JmhHh8NXwfqktXSFbzkPohUb90gnc3yZ9tiXa0uUpRY";
    }


}


