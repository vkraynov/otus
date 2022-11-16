package ru.homework;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * ATM.
 *
 * @author Vadim_Kraynov
 * @since 15.11.2022
 */
public class Atm {
    private int balance;
    private Set<Cell> cells;


    public void load(Set<Cell> cells) {
        this.cells = cells;

        for (Cell cell : cells) {
            balance += cell.getBill().getNominal().getValue() * cell.getQuantity();
        }
    }

    public void load(List<Bill> bills) {
        bills.forEach(e -> {
            Cell cell = cells.stream().filter(c -> c.getBill().getNominal() == e.getNominal()).findAny().orElse(null);
            if (Objects.nonNull(cell)) {
                cell.load(e);
                balance += e.getNominal().getValue();
            }
        });
    }

    public Map<Bill, Integer> withdraw(int requestedSum) {
        Map<Bill, Integer> result = new TreeMap<>((o1, o2) -> o2.getNominal().compareTo(o1.getNominal()));

        int sumToWithdraw = requestedSum;


        if (requestedSum > balance) {
            throw new RuntimeException("В банкомате недостаточно средств");
        }

        TreeSet<Cell> copy = new TreeSet<>(cells);

        int tempBalance = balance;

        for (Cell cell : copy) {
            int currentNominal = cell.getBill().getNominal().getValue();
            int numberOfRequestedBills = sumToWithdraw / currentNominal;
            int numberOfAvailableBills = cell.withdraw(numberOfRequestedBills);
            if (numberOfAvailableBills > 0) {
                tempBalance -= numberOfAvailableBills * currentNominal;
                sumToWithdraw -= numberOfAvailableBills * currentNominal;
                result.put(cell.getBill(), numberOfAvailableBills);
            }
        }

        if (sumToWithdraw != 0) {
            throw new RuntimeException("Запрошенная сумма не может быть выдана");
        }

        cells = copy;
        balance = tempBalance;

        return result;
    }

    public int getBalance() {
        return balance;
    }

    public Set<Cell> getCells() {
        return cells;
    }
}
