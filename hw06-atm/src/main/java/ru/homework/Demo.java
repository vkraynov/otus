package ru.homework;

import java.util.Collections;
import java.util.Map;

/**
 * Demo.
 *
 * @author Vadim_Kraynov
 * @since 15.11.2022
 */
public class Demo {

    public static void main(String[] args) {
        runSuccessCase();
        runFailedCases();
    }

    private static void runSuccessCase() {
        Atm atm = new Atm();
        new AtmInitLoader().load(atm);
        ResultPrinter resultPrinter = new ResultPrinter();

        int initBalance = atm.getBalance();
        System.out.println("Баланс: " + initBalance);
        Map<Bill, Integer> result1 = atm.withdraw(5000);
        resultPrinter.printResult(result1);
        System.out.println("Сумма должна быть выдана 1 купюрой 5000 - " + result1.get(new Bill(Nominal.FIVE_THOUSAND)).equals(1));
        System.out.println("Баланс: " + atm.getBalance());

        Map<Bill, Integer> result2 = atm.withdraw(6500);
        resultPrinter.printResult(result2);
        System.out.println("Баланс должен быть меньше на 11500 - " + (initBalance - 11500 == atm.getBalance()));
        System.out.println("Сумма должна быть выдана 3 купюрами 5000, 1000 и 500 - " +
                ((result2.get(new Bill(Nominal.FIVE_THOUSAND)).equals(1)) &&
                        (result2.get(new Bill(Nominal.THOUSAND)).equals(1)) &&
                        (result2.get(new Bill(Nominal.FIVE_HUNDRED)).equals(1))));

        Cell cellWith5000 = atm.getCells().stream().filter(e -> e.getBill().getNominal() == Nominal.FIVE_THOUSAND).findAny().orElse(null);
        System.out.println("В банкомате должно остаться 8 купюр номиналом 5000 - " + (cellWith5000.getQuantity() == 8));

        atm.load(Collections.singletonList(new Bill(Nominal.FIVE_THOUSAND)));

        System.out.println("В банкомате должно стать 9 купюр номиналом 5000 - " + (cellWith5000.getQuantity() == 9));
        System.out.println("Баланс банкомата должен стать 62000 - " + (atm.getBalance() == 62000));

    }

    private static void runFailedCases() {
        System.out.println("\n___________________________________________________________________\n");
        Atm atm = new Atm();
        new AtmInitLoader().load(atm);
        int initBalance = atm.getBalance();
        System.out.println("Баланс: " + initBalance);
        try {
            atm.withdraw(68600);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Баланс банкомата не должен был измениться - " + (initBalance == atm.getBalance()));
        try {
            atm.withdraw(123);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Баланс банкомата не должен был измениться - " + (initBalance == atm.getBalance()));
    }
}
