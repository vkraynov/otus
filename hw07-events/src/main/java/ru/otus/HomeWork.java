package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.ProcessorChangeFields;
import ru.otus.processor.homework.ProcessorThrowsExceptionAtEvenSeconds;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new ProcessorChangeFields(), new ProcessorThrowsExceptionAtEvenSeconds(LocalDateTime::now));

        var complexProcessor = new ComplexProcessor(processors, ex -> {
        });
        complexProcessor.addListener(new HistoryListener());

        var data = "data";
        var field13 = new ObjectForMessage();
        var field13Data = new ArrayList<String>();
        field13Data.add(data);
        field13.setData(field13Data);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        System.out.println("initial:" + message);

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

    }
}
