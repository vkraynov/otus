package ru.homework;

import java.util.Map;

/**
 * ResultPrinter.
 *
 * @author Vadim_Kraynov
 * @since 20.11.2022
 */
public class ResultPrinter {

    public void printResult(Map<Bill, Integer> result) {
        int counter = 0;
        System.out.println("Выданная сумма:");
        for (var entry : result.entrySet()) {
            System.out.println("Купюра номиналом " + entry.getKey().getNominal().getValue() + ": " + entry.getValue() + "шт.");
            counter += entry.getKey().getNominal().getValue() * entry.getValue();
        }
        System.out.println("Всего: " + counter);
    }
}
