package ru.otus.homework;

import ru.otus.homework.tests.TestResult;
import ru.otus.homework.tests.TestResultPrinter;
import ru.otus.homework.tests.TestRunner;

import java.util.EnumMap;

/**
 * Main class.
 *
 * @author Vadim_Kraynov
 * @date 25.10.2022
 */
public class Main {
    public static void main(String[] args) throws Exception {
        TestRunner runner = new TestRunner();
        TestResultPrinter testResultPrinter = new TestResultPrinter();

        EnumMap<TestResult, Integer> resultMap = runner.runTests("ru.otus.homework.tests.TestClass");
        testResultPrinter.print(resultMap);
    }
}
