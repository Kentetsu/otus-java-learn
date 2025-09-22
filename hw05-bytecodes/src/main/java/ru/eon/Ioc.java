package ru.eon;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static TestLoggingInterface createMyClass(Class<?> targetClass) {
        Class<?> interfaceClass = TestLoggingInterface.class;
        if (!interfaceClass.isAssignableFrom(targetClass)) {
            throw new IllegalArgumentException("Класс не имплементирует интерфейс TestLoggingInterface");
        }
        try {
            TestLoggingInterface implementation =
                    (TestLoggingInterface) targetClass.getDeclaredConstructor().newInstance();
            Set<MethodSignature> annotatedMethods = findAnnotatedMethods(targetClass);
            InvocationHandler handler = new TestInvocationHandler(implementation, annotatedMethods);
            return (TestLoggingInterface)
                    Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {interfaceClass}, handler);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать прокси сущность", e);
        }
    }

    static class TestInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;
        private final Set<MethodSignature> annotatedMethods;

        TestInvocationHandler(TestLoggingInterface targetObject, Set<MethodSignature> annotatedMethods) {
            this.myClass = targetObject;
            this.annotatedMethods = annotatedMethods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (shouldLogMethodCall(method)) {
                logMethodCall(method.getName(), args);
            }

            return method.invoke(myClass, args);
        }

        private boolean shouldLogMethodCall(Method method) {
            return annotatedMethods.contains(new MethodSignature(method));
        }

        private void logMethodCall(String methodName, Object[] args) {
            StringBuilder logMessage = new StringBuilder("executed method: " + methodName);

            if (args != null && args.length > 0) {
                logMessage.append(", params: ");
                for (int i = 0; i < args.length; i++) {
                    if (i > 0) {
                        logMessage.append(", ");
                    }
                    if (args[i] instanceof int[]) {
                        logMessage.append(Arrays.toString((int[]) args[i]));
                        continue;
                    }
                    logMessage.append(args[i]);
                }
            }

            logger.info(logMessage.toString());
        }
    }

    private static class MethodSignature {
        private final String name;
        private final Class<?>[] parameterTypes;
        private final int hashCode;

        public MethodSignature(Method method) {
            this.name = method.getName();
            this.parameterTypes = method.getParameterTypes();
            this.hashCode = computeHashCode();
        }

        public MethodSignature(String name, Class<?>[] parameterTypes) {
            this.name = name;
            this.parameterTypes = parameterTypes;
            this.hashCode = computeHashCode();
        }

        private int computeHashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + Arrays.hashCode(parameterTypes);
            return result;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodSignature that = (MethodSignature) o;
            return name.equals(that.name) && Arrays.equals(parameterTypes, that.parameterTypes);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    private static Set<MethodSignature> findAnnotatedMethods(Class<?> targetClass) {
        Set<MethodSignature> annotatedMethods = new HashSet<>();

        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Log.class)) {
                annotatedMethods.add(new MethodSignature(method));
            }
        }

        return annotatedMethods;
    }
}
