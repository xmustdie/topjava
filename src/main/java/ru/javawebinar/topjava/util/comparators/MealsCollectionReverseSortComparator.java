package ru.javawebinar.topjava.util.comparators;

import ru.javawebinar.topjava.model.Meal;

import java.util.Comparator;

public class MealsCollectionReverseSortComparator implements Comparator<Meal> {
    @Override
    public int compare(Meal o1, Meal o2) {
        return (-1) * o1.getDate().compareTo(o2.getDate());
    }
}
