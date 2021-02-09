package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    List<Meal> getAll();

    void save(Meal meal);

    Meal get(int id);

    void delete(int id);
}
