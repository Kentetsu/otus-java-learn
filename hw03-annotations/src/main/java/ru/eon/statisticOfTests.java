package ru.eon;

final class statisticOfTests {
    private int totalTests = 0;
    private int totalSuccess = 0;
    private int totalFailure = 0;

    int getTotalTests() {
        return totalTests;
    }

    void increaseTotalTests() {
        this.totalTests++;
    }

    int getTotalSuccess() {
        return totalSuccess;
    }

    void increaseTotalSuccess() {
        this.totalSuccess++;
    }

    int getTotalFailure() {
        return totalFailure;
    }

    void increaseTotalFailure() {
        this.totalFailure++;
    }
}
