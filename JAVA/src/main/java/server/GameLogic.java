package server;

import java.util.Random;

public class GameLogic {

    private static final int SECRET_CODE = 1111;
    private final Random random = new Random();


    // Validates the user's guess input and returns the parsed number
    public static int validateGuess(String input) {
        
        // Check if input is exactly 4 digits
        if (!input.matches("\\d{4}")) {
            throw new IllegalArgumentException("Invalid input: guess must be exactly 4 digits");
        }

        int guess = Integer.parseInt(input);

        // Ensure the number is in valid range (1000-9999)
        if (guess < 1000 || guess > 9999) {
            throw new IllegalArgumentException("Invalid input: guess must be between 1000 and 9999");
        }

        return guess;
    }

    // Generates a random 4-digit code and applies transformation rules
    public int generateSecretCode() {
        // Generate random 4-digit number (1000-9999)
        int number = 1000 + random.nextInt(9000);
        
        // Calculate sum of digits and transform
        int sumOfDigits = sumDigits(number);
        
        int modified;
        // If even
        if (sumOfDigits % 2 == 0) {
            modified = reverseNumber(number);
        // If odd
        } else {
            modified = incrementDigits(number);
        }
        
        // If palindrome, replace all digits with 7s
        if (isPalindrome(modified)) {
            modified = 7777;
        }
        
        System.out.println("Secret code generated: " + modified);
        
        return modified;
    }

    // Helper functions

    // Returns the sum of all digits in a number
    static int sumDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    // Reverses the digits of a number 
    static int reverseNumber(int number) {
        int reversed = 0;
        while (number > 0) {
            reversed = reversed * 10 + (number % 10);
            number /= 10;
        }
        return reversed;
    }

    // Increments each digit by 1, wrapping 9 -> 0 
    static int incrementDigits(int number) {
        int[] digits = new int[4];
        for (int i = 3; i >= 0; i--) {
            digits[i] = (number % 10 + 1) % 10;
            number /= 10;
        }
        return digits[0] * 1000 + digits[1] * 100 + digits[2] * 10 + digits[3];
    }

    // Checks if number reads the same forwards and backwards 
    static boolean isPalindrome(int number) {
        return number == reverseNumber(number);
    }

    // GenerateTimestampPrefix generates a textual prefix containing the current time
    public static String generateTimestampPrefix() {
        long timestamp = System.currentTimeMillis() / 1000; // Convert to seconds
        String prefix = "TIME: " + timestamp;
        return prefix;
    }
}
