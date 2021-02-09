package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int MAX_CALORIES = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private final MealService mealService = new MealServiceImpl();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getHttpServletMapping().getMatchValue();
        int id;
        switch (action) {
            case "delete":
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("delete meal with id = " + id);
                mealService.delete(id);
                break;
            case "update":
                id = Integer.parseInt(request.getParameter("id"));
                log.debug("update meal with id = " + id);
                Meal meal = mealService.get(id);
                request.setAttribute("myMeal", meal);
                request.getRequestDispatcher("editor.jsp").forward(request, response);
                break;
            case "create":
                log.debug("create meal");
                request.getRequestDispatcher("editor.jsp").forward(request, response);
                break;
        }
        List<Meal> meals = mealService.getAll();
        List<MealTo> mealsTo = MealsUtil.filteredByStreams(meals, LocalTime.MIN,
                LocalTime.MAX, MAX_CALORIES);

        request.setAttribute("allMeals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Meal meal = new Meal();
        meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));

        List<MealTo> mealsTo = MealsUtil.filteredByStreams(mealService.getAll(), LocalTime.MIN,
                LocalTime.MAX, MAX_CALORIES);
        request.setAttribute("allMeals", mealsTo);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

}
