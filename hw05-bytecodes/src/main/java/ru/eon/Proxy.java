package ru.eon;

public class Proxy {
    public static void main(String[] args) {
        TestLoggingInterface clazz = Ioc.createMyClass();
        clazz.calculation(1);
        clazz.calculation(2, 1);
        clazz.calculation(1, 2, 3);
        clazz.calculation("5", 1);
        clazz.calculation(new int[] {0, 1, 2, 3, 4, 5});
    }
}
