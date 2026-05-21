package app.util;

public class Validator {

    public static void validatePassword(String password, int min, int max) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        int len = password.length();

        if (len < min || len > max) {
            throw new IllegalArgumentException(
                    "Password must be between " + min + " and " + max + " characters"
            );
        }
    }
}
