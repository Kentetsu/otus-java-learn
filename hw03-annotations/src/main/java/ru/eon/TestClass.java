package ru.eon;

public class TestClass {
    int x;

    @Before
    void sayHi() {
        System.out.println("hi before");
    }

    @Before
    int initObjectX() {
        this.x = 1;
        return this.x;
    }

    @Test
    int getNext() {
        return this.x + 1;
    }

    @Test
    int getPrevious() {
        return this.x - 1;
    }

    @After
    void alwaysFail() throws Exception {
        throw new Exception("alwaysFail");
        // System.out.println("by after");
    }
}
