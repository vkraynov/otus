package ru.homework;

import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Init loader for ATM.
 *
 * @author Vadim_Kraynov
 * @since 16.11.2022
 */
public class AtmInitLoader {
    public void load(Atm atm) {
        TreeSet<Cell> setOfCells = Stream.of(Nominal.values())
                .map(e -> new Cell(new Bill(e)).load(10))
                .collect(Collectors.toCollection(TreeSet::new));

        atm.load(setOfCells);
    }
}
