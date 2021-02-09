package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDAOMemoryImpl;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class MealServiceImpl implements MealService {
    private final MealDAO mealDAO = new MealDAOMemoryImpl();

    @Override
    public List<Meal> getAll() {
        return mealDAO.getAll();
    }

    @Override
    public void save(Meal meal) {
        mealDAO.save(meal);
    }

    @Override
    public Meal get(int id) {
        return mealDAO.get(id);
    }

    @Override
    public void delete(int id) {
        mealDAO.delete(id);
    }
}
