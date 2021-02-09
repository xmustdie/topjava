package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MemoryMealsStorage {
    private static AtomicInteger counter = new AtomicInteger(0);
    private static MemoryMealsStorage instance;
    private static CopyOnWriteArrayList<Meal> memoryStorage = new CopyOnWriteArrayList<>(
            Arrays.asList(
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            30, 10, 0), "Завтрак", 500),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            30, 13, 0), "Обед", 1000),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            30, 20, 0), "Ужин", 500),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            31, 0, 0), "Еда на граничное значение", 100),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            31, 10, 0), "Завтрак", 1000),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            31, 13, 0), "Обед", 500),
                    new Meal(counter.incrementAndGet(), LocalDateTime.of(2020, Month.JANUARY,
                            31, 20, 0), "Ужин", 410)));

    private MemoryMealsStorage() {
    }

    public static MemoryMealsStorage getInstance() {
        return instance == null ? new MemoryMealsStorage() : instance;
    }

    public List<Meal> getAll() {
        return memoryStorage;
    }

    public void saveItem(Meal meal) {
        memoryStorage.add(meal.getId(), meal);
    }

    public Meal getItem(int id) {
        return memoryStorage.stream().filter(meal -> meal.getId() == id).findFirst().orElse(null);
    }

    public void deleteItem(int id) {
        memoryStorage.removeIf(meal -> meal.getId() == id);
    }

}
