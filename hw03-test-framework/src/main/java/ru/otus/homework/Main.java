package ru.otus.homework;

import ru.otus.homework.tests.TestRunner;

/**
 * Main class.
 *
 * @author Vadim_Kraynov
 * @date 25.10.2022
 */
public class Main {
    public static void main(String[] args) throws Exception {
        TestRunner runner = new TestRunner();
        runner.runTests("ru.otus.homework.tests.TestClass");
    }
}
