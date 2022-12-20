package ru.otus.processor;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorThrowsExceptionAtEvenSeconds;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ProcessorThrowExceptionTest.
 *
 * @author Vadim_Kraynov
 * @since 11.12.2022
 */
class ProcessorThrowExceptionTest {

    @Test
    void shouldThrowException() {
        LocalDateTime evenSeconds = LocalDateTime.parse("2018-05-05T11:50:56");

        Exception exception = assertThrows(RuntimeException.class,
                () -> new ProcessorThrowsExceptionAtEvenSeconds(() -> evenSeconds).process(new Message.Builder(1L).build()));

        String expectedMessage = "Четная секунда!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldNotThrowException() {
        LocalDateTime oddSeconds = LocalDateTime.parse("2018-05-05T11:50:55");

        assertDoesNotThrow(() -> new ProcessorThrowsExceptionAtEvenSeconds(() -> oddSeconds).process(new Message.Builder(1L).build()));
    }
}