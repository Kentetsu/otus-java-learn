package ATM;

public class Cell {
    private final int denomination;
    private int countOfBanknotes;

    protected Cell(int denomination) {
        this.denomination = denomination;
        this.countOfBanknotes = 0;
    }

    protected void addBanknotes(int countOfBanknotes) {
        this.countOfBanknotes += countOfBanknotes;
    }

    protected void removeBanknotes(int count) throws Exception {
        if (this.countOfBanknotes < count) {
            throw new Exception("В ячейке недостаточно купюр");
        }
        this.countOfBanknotes -= count;
    }

    protected int getCount() {
        return this.countOfBanknotes;
    }

    protected int getDenomination() {
        return this.denomination;
    }
}
