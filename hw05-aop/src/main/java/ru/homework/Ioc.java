package ru.homework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ioc.
 *
 * @author Vadim_Kraynov
 * @since 03.11.2022
 */
class Ioc {

    private Ioc() {
    }

    static TestLogging createTestLogging() throws Exception {
        InvocationHandler handler = new TestLoggingInvocationHandler(new TestLoggingImpl(), addOriginalMethods(TestLoggingImpl.class.getName(), new HashMap<>()));

        return (TestLogging) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{TestLogging.class}, handler);
    }

    private static Map<String, List<Integer>> addOriginalMethods(String className, Map<String, List<Integer>> methods) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(Log.class)) {
                if (!methods.containsKey(method.getName())) {
                    methods.put(method.getName(), new ArrayList<>());
                }
                methods.get(method.getName()).add(method.getParameterCount());
            }
        }
        return methods;
    }

    static class TestLoggingInvocationHandler implements InvocationHandler {

        private final TestLogging myClass;
        private final Map<String, List<Integer>> originalMethods;

        TestLoggingInvocationHandler(TestLogging myClass, Map<String, List<Integer>> originalMethods) {
            this.myClass = myClass;
            this.originalMethods = originalMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (originalMethods.containsKey(method.getName()) && originalMethods.get(method.getName()).contains(method.getParameterCount())) {
                System.out.printf("Executed method: %s, params: %s\n", method.getName(), Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }

    }
}
