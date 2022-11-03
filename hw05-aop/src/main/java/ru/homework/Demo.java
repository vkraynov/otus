package ru.homework;

/**
 * Demo.
 *
 * @author Vadim_Kraynov
 * @since 03.11.2022
 */
public class Demo {
    public static void main(String[] args) throws Exception {

        TestLogging myClass = Ioc.createTestLogging();
        myClass.calculation(6);
        myClass.calculation(6, 4);
        myClass.calculation(6, 4, 10);
    }
}
