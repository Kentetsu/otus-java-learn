package ru.eon;

class TestLogging implements TestLoggingInterface {
    @Log
    @Override
    public void calculation(int first) {
        System.out.println(first);
    }

    @Override
    public void calculation(int first, int second) {
        System.out.println(first + second);
    }

    @Log
    @Override
    public void calculation(int first, int second, int third) {
        System.out.println(first + second + third);
    }

    @Override
    public void calculation(String first, int second) {
        System.out.println(Integer.parseInt(first) + second);
    }

    @Log
    @Override
    public void calculation(int[] arrayOfInt) {
        int sum = 0;
        for (int num : arrayOfInt) {
            sum += num;
        }
        System.out.println(sum);
    }
}
