package ru.otus.homework.tests;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;

/**
 * Class for testing.
 *
 * @author Vadim_Kraynov
 * @date 25.10.2022
 */
public class TestClass {

    @Before
    public void before() {
        System.out.println("Before");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
    }

    @Test
    public void testWithException() {
        throw new RuntimeException("Oooooooooops!");
    }

    @Test
    public void test2() {
        System.out.println("Test2");
    }

    @Test
    public void test3() {
        System.out.println("Test3");
    }

    @After
    public void after() {
        System.out.println("After");
    }
}
