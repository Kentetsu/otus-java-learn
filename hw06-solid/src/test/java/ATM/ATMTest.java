package ATM;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ATMTest {

    private ATM atm;

    @BeforeEach
    void initATM() {
        atm = new ATM();
    }

    @Test
    @DisplayName("Тест добавления купюр.")
    void addBillsTest() throws Exception {
        Map<Integer, Integer> testDeposit = new HashMap<>();
        testDeposit.put(5000, 1);
        testDeposit.put(1000, 2);
        testDeposit.put(500, 4);
        testDeposit.put(100, 10);
        assert (atm.depositMoney(testDeposit) == 10000);
        assert (atm.getAmountOfMoney() == 10000);
    }

    @Test
    @DisplayName("Тест удаления курпюр.")
    void removeBillsTest() throws Exception {
        Map<Integer, Integer> testDeposit = new HashMap<>();
        testDeposit.put(100, 10);
        assert (atm.depositMoney(testDeposit) == 1000);
        assert (atm.withdrawMoney(100) == 900);
    }

    @Test
    @DisplayName("Тест удаления курпюр больше чем имеется в банкомате.")
    void removeMoreThanExist() throws Exception {
        Map<Integer, Integer> testDeposit = new HashMap<>();
        testDeposit.put(1000, 1);
        assert (atm.depositMoney(testDeposit) == 1000);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            atm.withdrawMoney(2000);
        });
        Assertions.assertEquals("В банкомате недостаточно необходимых купюр", exception.getMessage());
        assert (atm.getAmountOfMoney() == 1000);
    }

    @Test
    @DisplayName("Тест выдачи купюр, которых нет в банкомате.")
    void removeNonExistingBills() throws Exception {
        Map<Integer, Integer> testDeposit = new HashMap<>();
        testDeposit.put(1000, 1);
        assert (atm.depositMoney(testDeposit) == 1000);

        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            atm.withdrawMoney(200);
        });
        Assertions.assertEquals("В банкомате недостаточно необходимых купюр", exception.getMessage());
        assert (atm.getAmountOfMoney() == 1000);
    }

    @Test
    @DisplayName("Тест добавления купюр с номиналом, для которых отсутствует ячейка")
    void addWrongDenomination() throws Exception {
        Map<Integer, Integer> testDeposit = new HashMap<>();
        testDeposit.put(1, 1);
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            atm.depositMoney(testDeposit);
        });
        Assertions.assertEquals(
                "Номинал не найден, в банкомате отстутствует ячейка с номиналом 1", exception.getMessage());
    }
}
