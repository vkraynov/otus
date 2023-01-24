package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> sortedMethods = Arrays.stream(configClass.getMethods())
                .filter(el -> el.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(el -> el.getAnnotation(AppComponent.class).order()))
                .toList();

        try {
            Object configClassInstance = configClass.getConstructor().newInstance();
            for (Method method : sortedMethods) {
                String componentName = method.getAnnotation(AppComponent.class).name();
                if (appComponentsByName.containsKey(componentName)) {
                    throw new RuntimeException("Компонент с таким именем уже есть в контексте");
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                ArrayList<Object> constructorParams = new ArrayList<>();
                for (Class<?> parameterType : parameterTypes) {
                    for (Object appComponent : appComponents) {
                        AnnotatedType[] annotatedInterfaces = appComponent.getClass().getAnnotatedInterfaces();
                        for (AnnotatedType annotatedInterface : annotatedInterfaces) {
                            if (annotatedInterface.getType().equals(parameterType)) {
                                constructorParams.add(appComponent);
                            }
                        }
                    }
                }

                Object component = method.invoke(configClassInstance, constructorParams.toArray());
                appComponents.add(component);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = new ArrayList<>();
        for (Object appComponent : appComponents) {
            AnnotatedType[] annotatedInterfaces = appComponent.getClass().getAnnotatedInterfaces();
            for (AnnotatedType annotatedType : annotatedInterfaces) {
                if (annotatedType.getType().equals(componentClass) || appComponent.getClass().isAssignableFrom(componentClass)) {
                    components.add(appComponent);
                }
            }
        }
        if (components.size() != 1) {
            throw new RuntimeException("В контексте отсутсвтует или дублируется искомый компонент");
        }

        return (C) components.get(0);

    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
