package app.util;

public class Validator {

    private static final int MIN_PASSWORD = 10;
    private static final int MAX_PASSWORD = 20;

    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }

    public static void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        int len = password.length();

        if (len < MIN_PASSWORD || len > MAX_PASSWORD) {
            throw new IllegalArgumentException(
                    "Password must be between 10 and 20 characters"
            );
        }
    }
}
