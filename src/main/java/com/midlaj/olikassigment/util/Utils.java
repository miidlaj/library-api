package com.midlaj.olikassigment.util;

import java.util.Random;

public class Utils {

    /**
     * A static method to generate valid Isbn13 number for testing purpose
     *
     * @return isbn13 number as string
     */
    public static String generateRandomIsbn13() {
        StringBuilder isbn = new StringBuilder("978");
        Random random = new Random();

        /**
         * Generate and append 9 characters
         */
        for (int i = 0; i < 9; i++) {
            isbn.append(random.nextInt(10));
        }

        /**
         * Calculate the check digit
         */
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = (10 - (sum % 10)) % 10;
        isbn.append(checkDigit);

        return isbn.toString();
    }
}
