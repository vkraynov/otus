package ru.otus.homework.tests;

import ru.otus.homework.utils.ConsoleColors;

import java.util.EnumMap;

/**
 * Printer for test result.
 *
 * @author Vadim_Kraynov
 * @date 26.10.2022
 */
public class TestResultPrinter {
    public void print(EnumMap<TestResult, Integer> resultIntegerEnumMap) {
        System.out.println("\n-------------------------------------------------------------------------------------\n");
        System.out.printf("Всего тестов запущено: %d\n", resultIntegerEnumMap.get(TestResult.TOTAL));
        System.out.printf(ConsoleColors.GREEN_BOLD + "Прошло успешно: %d\n" + ConsoleColors.RESET,  resultIntegerEnumMap.get(TestResult.SUCCESS));
        System.out.printf(ConsoleColors.RED_BOLD + "Завершились с ошибкой: %d\n" + ConsoleColors.RESET, resultIntegerEnumMap.get(TestResult.FAILED));
        System.out.println("\n-------------------------------------------------------------------------------------\n");
    }
}
