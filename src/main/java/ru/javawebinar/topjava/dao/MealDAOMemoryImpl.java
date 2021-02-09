package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MemoryMealsStorage;

import java.util.List;

public class MealDAOMemoryImpl implements MealDAO {
    private final MemoryMealsStorage memoryMealsStorage = MemoryMealsStorage.getInstance();

    @Override
    public List<Meal> getAll() {
        return memoryMealsStorage.getAll();
    }


    @Override
    public void save(Meal meal) {
        memoryMealsStorage.saveItem(meal);
    }

    @Override
    public Meal get(int id) {
        return memoryMealsStorage.getItem(id);
    }

    @Override
    public void delete(int id) {
        memoryMealsStorage.deleteItem(id);
    }
}
