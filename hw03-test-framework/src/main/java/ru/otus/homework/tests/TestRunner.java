package ru.otus.homework.tests;

import ru.otus.homework.annotations.After;
import ru.otus.homework.annotations.Before;
import ru.otus.homework.annotations.Test;
import ru.otus.homework.utils.ConsoleColors;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Runner for test class.
 *
 * @author Vadim_Kraynov
 * @date 25.10.2022
 */
public class TestRunner {

    private final EnumMap<TestResult, Integer> resultMap = new EnumMap<>(TestResult.class);


    public EnumMap<TestResult, Integer> runTests(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getConstructor();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        int errorCount = 0;
        Object object;

        List<Method> methodsWithBeforeAnnotation = findMethodsWithAnnotation(declaredMethods, Before.class);
        List<Method> methodsWithTestAnnotation = findMethodsWithAnnotation(declaredMethods, Test.class);
        List<Method> methodsWithAfterAnnotation = findMethodsWithAnnotation(declaredMethods, After.class);

        for (Method testMethod : methodsWithTestAnnotation) {
            object = constructor.newInstance();
            try {
                runMethods(methodsWithBeforeAnnotation, object);
                runMethods(Collections.singletonList(testMethod), object);
            } catch (Throwable throwable) {
                System.out.println("Error");
                errorCount++;
            } finally {
                runMethods(methodsWithAfterAnnotation, object);
            }
        }

        resultMap.put(TestResult.TOTAL, methodsWithTestAnnotation.size());
        resultMap.put(TestResult.SUCCESS, methodsWithTestAnnotation.size() - errorCount);
        resultMap.put(TestResult.FAILED, errorCount);

        return resultMap;

    }

    private List<Method> findMethodsWithAnnotation(Method[] declaredMethods, Class annotationClass) {
        return Arrays.stream(declaredMethods)
                .filter(method -> method.isAnnotationPresent(annotationClass))
                .collect(Collectors.toList());
    }

    private void runMethods(List<Method> methodsWithBeforeAnnotation, Object object) throws Exception {
        for (Method method : methodsWithBeforeAnnotation) {
            method.invoke(object);
        }
    }
}
