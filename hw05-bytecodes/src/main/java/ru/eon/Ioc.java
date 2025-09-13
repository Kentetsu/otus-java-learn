package ru.eon;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Ioc {
    private static final Logger logger = LoggerFactory.getLogger(Ioc.class);

    private Ioc() {}

    static TestLoggingInterface createMyClass() {
        InvocationHandler handler = new TestInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    static class TestInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface myClass;

        TestInvocationHandler(TestLogging myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method targetMethod = myClass.getClass().getMethod(method.getName(), method.getParameterTypes());

            if (targetMethod.isAnnotationPresent(Log.class)) {
                logMethodCall(method.getName(), args);
            }

            return method.invoke(myClass, args);
        }

        private void logMethodCall(String methodName, Object[] args) {
            StringBuilder logMessage = new StringBuilder("executed method: " + methodName);

            if (args.length > 0) {
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
}
