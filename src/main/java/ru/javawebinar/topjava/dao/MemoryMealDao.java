package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealDao implements MealDao {
    private static AtomicInteger counter = new AtomicInteger(0);

    private volatile Map<Integer, Meal> storage;

     {
        storage = new ConcurrentHashMap<>();
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 500));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 1000));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                "Ужин", 500));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                "Еда на граничное значение", 100));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                "Завтрак", 1000));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                "Обед", 500));
        storage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                "Ужин", 410));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Meal save(Meal meal) {

        // [уй его знает чо делать
        synchronized (storage) {
            int id;
            if (meal.getId() == 0) {
                id = counter.incrementAndGet();
                storage.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(),
                        meal.getCalories()));
                return storage.get(id);
            } else if (!storage.containsKey(meal.getId())) {
                return null;
            } else {
                id = meal.getId();
                storage.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(),
                        meal.getCalories()));
                return storage.get(id);
            }
        }
    }

    @Override
    public Meal get(int id) {
        return storage.get(id);
    }

    @Override
    public void delete(int id) {
        storage.remove(id);
    }
}
