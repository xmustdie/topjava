package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int MAX_CALORIES = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private static final List<Meal> MEALS = Arrays.asList(
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
    );

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getHttpServletMapping().getMatchValue();
        int id = Integer.parseInt(request.getParameter("id"));
        switch (action) {
            case "delete":
                System.out.println("delete meal with id = " + id);
                break;
            case "update":
                System.out.println("update meal with id = " + id);
                break;
            case "create":
                System.out.println("do smthng");
                break;
        }
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(MEALS, LocalTime.MIN, LocalTime.MAX, MAX_CALORIES);
        request.setAttribute("allMeals", mealsTo);
//        System.out.println(mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
