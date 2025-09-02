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
        StatisticOfTests statistic = runTests(TestClass.class);
        log.info("Всего тестов {}", statistic.totalTests());
        log.info("Успешно выполнено {}", statistic.totalSuccess());
        log.info("Всего ошибок при исполнение {}", statistic.totalFailure());
    }

    private static StatisticOfTests runTests(Class<?> clazz) {
        StatisticOfTests statistic = new StatisticOfTests(0, 0, 0);
        for (Method method : clazz.getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Test methodOfTestMethod) {
                    Object object = initObject(clazz);
                    assert object != null;
                    int[] resultsOfBefore = handleAnnotations(Before.class, object);
                    statistic = statistic.mergeStatistics(statistic.convertFromArray(resultsOfBefore));

                    int statusOfTest = runTest(object, method);
                    if (statusOfTest == 0) statistic = statistic.incrementSuccess();
                    else statistic = statistic.incrementFailure();

                    int[] resultsOfAfter = handleAnnotations(After.class, object);
                    StatisticOfTests statisticOnAfter = new StatisticOfTests(0, 0, 0).convertFromArray(resultsOfAfter);

                    statistic = statistic.mergeStatistics(statisticOnAfter);
                }
            }
        }
        return statistic;
    }

    private static int runTest(Object object, Method method) {
        try {
            int result;
            if (method.getParameterCount() == 0 && method.getReturnType().equals(void.class)) {
                method.invoke(object);
            } else {
                result = (int) method.invoke(object, 1);
                System.out.println(result);
            }
            return 0;

        } catch (Exception e) {
            log.error(String.valueOf(e));
            return 1;
        }
    }

    private static int[] handleAnnotations(Class<?> targetAnnotation, Object objectOfClass) {
        int[] result = {0, 0};
        for (Method method : objectOfClass.getClass().getDeclaredMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(targetAnnotation)) {
                    int resultOfCurrentTest = runTest(objectOfClass, method);
                    result[resultOfCurrentTest]++;
                }
            }
        }
        return result;
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

record StatisticOfTests(int totalTests, int totalSuccess, int totalFailure) {
    StatisticOfTests incrementSuccess() {
        return new StatisticOfTests(this.totalTests + 1, this.totalSuccess + 1, this.totalFailure);
    }

    StatisticOfTests incrementFailure() {
        return new StatisticOfTests(this.totalTests + 1, this.totalSuccess, this.totalFailure + 1);
    }

    StatisticOfTests convertFromArray(int[] targetArray) {
        return new StatisticOfTests(targetArray[0] + targetArray[1], targetArray[0], targetArray[1]);
    }

    StatisticOfTests mergeStatistics(StatisticOfTests target) {
        return new StatisticOfTests(
                this.totalTests + target.totalTests,
                this.totalSuccess + target.totalSuccess,
                this.totalFailure + target.totalFailure);
    }
}
