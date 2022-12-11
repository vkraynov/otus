package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * ProcessorThrowException.
 *
 * @author Vadim_Kraynov
 * @since 11.12.2022
 */
public class ProcessorThrowsExceptionAtEvenSeconds implements Processor {
    private final DateTimeProvider dateTimeProvider;

    public ProcessorThrowsExceptionAtEvenSeconds(DateTimeProvider dateTimeProvider) {
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public Message process(Message message) {
        if (dateTimeProvider.getDate().getSecond() % 2 == 0) {
            throw new RuntimeException("Четная секунда!");
        }
        return message;
    }
}
