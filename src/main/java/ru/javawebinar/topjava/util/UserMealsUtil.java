package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        for (UserMealWithExcess userMealWithExcess : mealsTo) {
            System.out.println(userMealWithExcess);
        }
        System.out.println("===============================");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles
        List<UserMealWithExcess> userMealWithExcessList = new ArrayList<>();
        Map<LocalDate, Integer> totalCalories = new HashMap<>();
        meals.forEach(usrMeal -> {
            totalCalories.merge(usrMeal.getDateTime().toLocalDate(), usrMeal.getCalories(), (Integer::sum));
            if (TimeUtil.isBetweenHalfOpen(usrMeal.getDateTime().toLocalTime(), startTime, endTime)) {

                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(
                        usrMeal.getDateTime(), usrMeal.getDescription(), usrMeal.getCalories(), false);

                userMealWithExcessList.add(userMealWithExcess);
            }
        });

        userMealWithExcessList.forEach(usrMealWithExcess -> {
            LocalDate localDate = usrMealWithExcess.getDateTime().toLocalDate();
            if (totalCalories.containsKey(localDate)) {
                usrMealWithExcess.setExcess(
                        totalCalories.get(localDate) > caloriesPerDay);
            }
        });
        return userMealWithExcessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        meals.stream()
                                .collect(Collectors.groupingBy(usrMeal -> usrMeal.getDateTime().toLocalDate())).entrySet().stream()
                                .collect(Collectors.toMap(
                                        Map.Entry::getKey,
                                        calPerDay -> calPerDay.getValue().stream().mapToInt(UserMeal::getCalories).sum(),
                                        (a, b) -> b)).get(meal.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }
}
