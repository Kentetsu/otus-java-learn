package ru.eon;

public class TestClass {

    @Before
    void sayHi() {
        System.out.println("hi before");
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
