package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class GameLogicTest {

    private final GameLogic gameLogic = new GameLogic();

    @Test
    void testValidateGuess_validInput() {
        assertEquals(1234, GameLogic.validateGuess("1234"));
        assertEquals(1000, GameLogic.validateGuess("1000"));
        assertEquals(9999, GameLogic.validateGuess("9999"));
    }

    @Test
    void testValidateGuess_invalidInputs() {
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("$"));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("-15"));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess(" "));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess(""));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("abc"));
    }

    @Test
    void testValidateGuess_wrongLength() {
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("123"));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("12345"));
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("007123"));
    }

    @Test
    void testValidateGuess_leadingZeros() {
        assertThrows(IllegalArgumentException.class, () -> GameLogic.validateGuess("0071"));
    }

    // sumDigits adds all digits together 
    @Test
    void testSumDigits() {
        assertEquals(10, GameLogic.sumDigits(1234));
        assertEquals(4, GameLogic.sumDigits(1111));
        assertEquals(36, GameLogic.sumDigits(9999));
        assertEquals(1, GameLogic.sumDigits(1000));
    }

    // reverseNumber flips the digits
    @Test
    void testReverseNumber() {
        assertEquals(4321, GameLogic.reverseNumber(1234));
        assertEquals(1111, GameLogic.reverseNumber(1111));
        assertEquals(1, GameLogic.reverseNumber(1000));
        assertEquals(9999, GameLogic.reverseNumber(9999));
    }

    // incrementDigits adds 1 to each digit, 9 wraps to 0 
    @Test
    void testIncrementDigits() {
        assertEquals(2345, GameLogic.incrementDigits(1234));
        assertEquals(2222, GameLogic.incrementDigits(1111));
        assertEquals(0, GameLogic.incrementDigits(9999));
        assertEquals(2111, GameLogic.incrementDigits(1000));
    }

    // isPalindrome checks if number reads same forwards and backwards
    @Test
    void testIsPalindrome() {
        assertTrue(GameLogic.isPalindrome(1221));
        assertTrue(GameLogic.isPalindrome(1111));
        assertTrue(GameLogic.isPalindrome(9999));
        assertFalse(GameLogic.isPalindrome(1234));
        assertFalse(GameLogic.isPalindrome(1000));
    }

    // Even sum -> reverse -> not palindrome (1234 -> 4321)
    @Test
    void testSecretCode_evenSum_notPalindrome() {
        int number = 1234;
        assertEquals(10, GameLogic.sumDigits(number));          // sum is even
        assertEquals(4321, GameLogic.reverseNumber(number));    // reversed
        assertFalse(GameLogic.isPalindrome(4321));              // not palindrome, so stays 4321
    }

    // Even sum -> reverse -> palindrome -> becomes 7777 (1111 -> 1111 -> 7777)
    @Test
    void testSecretCode_evenSum_palindrome() {
        int number = 1111;
        assertEquals(4, GameLogic.sumDigits(number));           // sum is even
        assertEquals(1111, GameLogic.reverseNumber(number));    // reversed (same)
        assertTrue(GameLogic.isPalindrome(1111));               // is palindrome, so becomes 7777
    }

    // Odd sum -> increment -> not palindrome (1112 -> 2223)
    @Test
    void testSecretCode_oddSum_notPalindrome() {
        int number = 1112;
        assertEquals(5, GameLogic.sumDigits(number));           // sum is odd
        assertEquals(2223, GameLogic.incrementDigits(number));  // incremented
        assertFalse(GameLogic.isPalindrome(2223));              // not palindrome, so stays 2223
    }

    // Make sure generateSecretCode always returns a valid number
    @Test
    void testSecretCode_alwaysValidRange() {
        for (int i = 0; i < 100; i++) {
            int code = gameLogic.generateSecretCode();
            assertTrue(code >= 0 && code <= 9999);
        }
    }

    // generateTimestampPrefix returns a string starting with "TIME: "
    @Test
    void testGenerateTimestampPrefix() {
        String prefix = GameLogic.generateTimestampPrefix();
        assertTrue(prefix.startsWith("TIME: "));
    }
}
