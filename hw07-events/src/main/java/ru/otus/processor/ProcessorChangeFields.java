package ru.otus.processor;

import ru.otus.model.Message;

/**
 * ProcessorChangeFields.
 *
 * @author Vadim_Kraynov
 * @since 11.12.2022
 */
public class ProcessorChangeFields implements Processor {

    @Override
    public Message process(Message message) {
        String tmp = message.getField12();

        return message.toBuilder().field12(message.getField11()).field11(tmp).build();
    }
}
