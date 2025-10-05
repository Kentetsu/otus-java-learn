package ATM;

import java.util.*;

public class CashRegister {
    private final List<Cell> cellBlock = new ArrayList<>();

    protected CashRegister(int[] arrayOfDenominations) {
        for (int denomination : arrayOfDenominations) {
            cellBlock.add(new Cell(denomination));
        }
    }

    protected int getAmountOfMoney() {
        var totalValue = 0;
        for (Cell cell : cellBlock) {
            var count = cell.getCount();
            var denomination = cell.getDenomination();
            totalValue += count * denomination;
        }
        return totalValue;
    }

    protected int addBills(Map<Integer, Integer> mapOfBills) throws Exception {
        Cell targetCell = null;
        for (Map.Entry<Integer, Integer> bills : mapOfBills.entrySet()) {
            for (Cell currentCell : cellBlock) {
                if (currentCell.getDenomination() == bills.getKey()) {
                    targetCell = currentCell;
                }
            }
            if (targetCell == null) {
                throw new Exception(
                        "Номинал не найден, в банкомате отстутствует ячейка с номиналом " + bills.getValue());
            }
            targetCell.addBanknotes(bills.getValue());
        }
        return getAmountOfMoney();
    }

    private boolean checkAbilityToWithdraw(int count) {
        int needsToWithdraw = count;
        for (Cell cell : cellBlock) {
            if (cell.getDenomination() <= needsToWithdraw) {
                if (cell.getCount() * cell.getDenomination() >= needsToWithdraw) {
                    needsToWithdraw -= cell.getDenomination() * (needsToWithdraw / cell.getDenomination());
                } else {
                    needsToWithdraw -= cell.getDenomination() * cell.getCount();
                }
            }
        }
        return needsToWithdraw == 0;
    }

    public int removeBills(int count) throws Exception {
        if (checkAbilityToWithdraw(count)) {
            int needsToWithdraw = count;
            for (Cell currentCell : cellBlock) {
                if (currentCell.getDenomination() <= needsToWithdraw) {
                    if (currentCell.getCount() * currentCell.getDenomination() >= needsToWithdraw) {

                        currentCell.removeBanknotes((needsToWithdraw / currentCell.getDenomination()));
                        needsToWithdraw -=
                                currentCell.getDenomination() * (needsToWithdraw / currentCell.getDenomination());

                    } else {
                        currentCell.removeBanknotes(currentCell.getCount());
                        needsToWithdraw -= currentCell.getDenomination() * currentCell.getCount();
                    }
                }
            }
        } else {
            throw new Exception("В банкомате недостаточно необходимых купюр");
        }
        return getAmountOfMoney();
    }
}
