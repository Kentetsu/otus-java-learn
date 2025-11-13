package ru.otus.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> componentMethods = Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        method -> method.getAnnotation(AppComponent.class).order()))
                .toList();

        Set<String> componentNames = new HashSet<>();
        for (Method method : componentMethods) {
            String componentName = method.getAnnotation(AppComponent.class).name();
            if (!componentNames.add(componentName)) {
                throw new IllegalArgumentException("Дублирование имени компонента: " + componentName);
            }
        }

        Object configInstance;
        try {
            configInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при инициализации инстанса конфигурации", e);
        }

        List<Object> tempComponents = new ArrayList<>();
        Map<String, Object> tempComponentsByName = new HashMap<>();

        for (Method method : componentMethods) {
            try {
                Object[] parameters = resolveParameters(tempComponents, method);
                Object component = method.invoke(configInstance, parameters);

                String componentName = method.getAnnotation(AppComponent.class).name();

                tempComponents.add(component);
                tempComponentsByName.put(componentName, component);

            } catch (Exception e) {
                throw new RuntimeException("Ошибка при создании компонента: " + method.getName(), e);
            }
        }

        appComponents.addAll(tempComponents);
        appComponentsByName.putAll(tempComponentsByName);
    }

    private Object[] resolveParameters(List<Object> components, Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = findComponentByType(components, parameterTypes[i]);
            if (parameters[i] == null) {
                throw new IllegalArgumentException("Не найдено компонента для параметра типа: " + parameterTypes[i]);
            }
        }

        return parameters;
    }

    private Object findComponentByType(List<Object> components, Class<?> targetType) {
        List<Object> exactMatches = components.stream()
                .filter(comp -> targetType.equals(comp.getClass()))
                .toList();

        if (exactMatches.size() == 1) {
            return exactMatches.getFirst();
        }

        List<Object> assignableMatches = components.stream()
                .filter(comp -> targetType.isAssignableFrom(comp.getClass()))
                .toList();

        if (assignableMatches.isEmpty()) {
            return null;
        }
        if (assignableMatches.size() > 1) {
            throw new IllegalArgumentException("Найдено более одного компонента для типа: " + targetType);
        }

        return assignableMatches.getFirst();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        Object component = findComponentByType(appComponents, componentClass);
        if (component == null) {
            throw new IllegalArgumentException("Не найдено компонента для типа: " + componentClass);
        }

        if (!componentClass.isInstance(component)) {
            throw new ClassCastException(
                    "Компонент " + component.getClass() + " не может быть приведен к " + componentClass);
        }

        return componentClass.cast(component);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new IllegalArgumentException("Не найдено компонента для имени: " + componentName);
        }
        @SuppressWarnings("unchecked")
        C result = (C) component;
        return result;
    }
}
