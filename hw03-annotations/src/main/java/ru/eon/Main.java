package ru.eon;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        runTests(TestClass.class);
    }

    private static void runTests(Class<?> clazz) {
        statisticOfTests statObject = new statisticOfTests();
        handleAnnotation(Before.class, clazz, statObject);
        handleAnnotation(Test.class, clazz, statObject);
        handleAnnotation(After.class, clazz, statObject);
        log.info("Total tests {}", statObject.getTotalTests());
        log.info("Total tests succeed {}", statObject.getTotalSuccess());
        log.info("Total tests failure {}", statObject.getTotalFailure());
    }

    private static void handleAnnotation(Class<?> targetAnnotation, Class<?> targetClass, statisticOfTests statistic) {
        Object object = initObject(targetClass);

        for (Method method : targetClass.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(targetAnnotation)) {
                    testAnnotationHandler(object, method, statistic);
                }
            }
        }
    }

    private static void testAnnotationHandler(Object object, Method method, statisticOfTests statistic) {
        statistic.increaseTotalTests();
        try {
            int result;
            if (method.getParameterCount() == 0 && method.getReturnType().equals(void.class)) {
                method.invoke(object);
            } else {
                result = (int) method.invoke(object, 1);
                System.out.println(result);
            }
            statistic.increaseTotalSuccess();
        } catch (Exception e) {
            statistic.increaseTotalFailure();
            log.error(String.valueOf(e));
        }
    }

    private static Object initObject(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException
                | InvocationTargetException
                | InstantiationException
                | IllegalAccessException e) {
            log.error(String.valueOf(e));
        }
        return null;
    }
}
