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
    private static final String MEALS_JSP = "/meals.jsp";
    private static final String EDITOR_JSP = "/editor.jsp";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("redirect to meals");
        String targetJsp = "";
        String action = request.getParameter("action") == null ? "meals" : request.getParameter("action");
        System.out.println(action);
        int id;
        if (action.equalsIgnoreCase("delete")) {
            id = Integer.parseInt(request.getParameter("id"));
            log.debug("delete meal with id =" + id);
            mealService.delete(id);
            response.sendRedirect("meals");
            return;
        } else if (action.equalsIgnoreCase("meals")) {
            log.debug("show list of meals");
            targetJsp = MEALS_JSP;
            List<MealTo> mealTos = getTos();
            request.setAttribute("mealTos", mealTos);
        } else if (action.equalsIgnoreCase("update")) {
            targetJsp = EDITOR_JSP;
            id = Integer.parseInt(request.getParameter("id"));
            log.debug("update meal with id =" + id);
            Meal meal = mealService.get(id);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("create")) {
            log.debug("create meal");
            targetJsp = EDITOR_JSP;
        }

        request.getRequestDispatcher(targetJsp).forward(request, response);
    }

    private List<MealTo> getTos() {
        List<Meal> meals = mealService.getAll();
        return MealsUtil.filteredByStreams(meals, LocalTime.MIN,
                LocalTime.MAX, MAX_CALORIES);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal;
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        if (!request.getParameter("id").equals("")) {
            int id = Integer.parseInt(request.getParameter("id"));
            meal = new Meal(id, localDateTime, description, calories);
        } else {
            meal = new Meal();
            meal.setDateTime(LocalDateTime.parse(request.getParameter("dateTime")));
            meal.setDescription(request.getParameter("description"));
            meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        }
        mealService.save(meal);
        response.sendRedirect("meals");
    }
}
