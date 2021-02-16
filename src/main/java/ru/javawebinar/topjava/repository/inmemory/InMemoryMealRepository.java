package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(m -> save(m, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId,
                (v) -> new ConcurrentHashMap<>());
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
            } else if (userMeals.get(meal.getId()) == null) {
                return null;
            }
            userMeals.put(meal.getId(), meal);
            return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        if (repository.get(userId) != null) {
            return repository.get(userId).remove(id) != null;
        } else
            return false;
    }

    @Override
    public Meal get(int id, int userId) {
        if (repository.get(userId) != null) {
            return repository.get(userId).get(id);
        } else
            return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllFilterByDate(userId, LocalDate.MIN, LocalDate.MAX);
    }

    @Override
    public List<Meal> getAllFilterByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getOrDefault(userId, new ConcurrentHashMap<>()).values().stream()
                .filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

