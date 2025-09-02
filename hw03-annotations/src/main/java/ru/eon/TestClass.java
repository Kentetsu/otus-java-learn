package ru.eon;

public class TestClass {
    int x;

    @Before
    void sayHi() {
        System.out.println("hi before");
    }

    @Before
    int initObjectX(int x) {
        this.x = x;
        return this.x;
    }

    @Test
    int getNext(int number) {
        return number + 1;
    }

    @Test
    int getPrevious(int number) {
        return number - 1;
    }

    @After
    void alwaysFail() throws Exception {
        throw new Exception("alwaysFail");
        // System.out.println("by after");
    }
}
