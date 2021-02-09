package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {
    List<Meal> getAll();

    void save(Meal meal);

    Meal get(int id);

    void delete(int id);
}
