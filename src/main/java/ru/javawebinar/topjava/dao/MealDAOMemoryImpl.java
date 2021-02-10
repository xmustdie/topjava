package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDAOMemoryImpl implements MealDAO {
    private static AtomicInteger counter = new AtomicInteger(0);

    public static ConcurrentHashMap<Integer, Meal> memoryStorage = new ConcurrentHashMap<>();

    static {
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0),
                "Завтрак", 500));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0),
                "Обед", 1000));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0),
                "Ужин", 500));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0),
                "Еда на граничное значение", 100));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0),
                "Завтрак", 1000));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0),
                "Обед", 500));
        memoryStorage.put(counter.incrementAndGet(), new Meal(counter.get(),
                LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0),
                "Ужин", 410));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(memoryStorage.values());
    }


    @Override
    public Meal save(Meal meal) {
        int id;
        if (meal.getId() == 0) {
            id = counter.incrementAndGet();
        } else {
            id = meal.getId();
        }
        memoryStorage.merge(id, new Meal(id, meal.getDateTime(), meal.getDescription(),
                meal.getCalories()), (meal1, meal2) -> meal);
        return memoryStorage.get(id);
    }

    @Override
    public Meal get(int id) {
        return memoryStorage.get(id);
    }

    @Override
    public void delete(int id) {
        memoryStorage.remove(id);
    }
}
