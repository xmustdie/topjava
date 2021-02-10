package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int MAX_CALORIES = 2000;
    private static final Logger log = getLogger(MealServlet.class);
    private static final String MEALS_JSP = "/meals.jsp";
    private static final String EDITOR_JSP = "/editor.jsp";
    private MealDao dao;

    @Override
    public void init() throws ServletException {
        super.init();
        dao = new MealDaoMemoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("request processing by servlet: method doGet started");
        String targetJsp;
        String action = request.getParameter("action");
        if (action == null || action.equalsIgnoreCase("meals")) {
            log.debug("show list of meals");
            targetJsp = MEALS_JSP;
            request.setAttribute("mealTos", MealsUtil.filteredByStreams(dao.getAll(),
                    LocalTime.MIN, LocalTime.MAX, MAX_CALORIES));
        } else if (action.equalsIgnoreCase("delete")) {
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("delete meal with id = {}", id);
            dao.delete(id);
            response.sendRedirect("meals");
            return;
        } else if (action.equalsIgnoreCase("update")) {
            targetJsp = EDITOR_JSP;
            int id = Integer.parseInt(request.getParameter("id"));
            log.debug("update meal with id {}", id);
            Meal meal = dao.get(id);
            request.setAttribute("meal", meal);
        } else {
            targetJsp = EDITOR_JSP;
        }
        log.debug("request processed by servlet: method doGet finished");
        request.getRequestDispatcher(targetJsp).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        log.debug("request processing by servlet: method doPost started");
        request.setCharacterEncoding("UTF-8");
        LocalDateTime localDateTime = LocalDateTime.parse(request.getParameter("dateTime"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        int id = 0;
        if (!request.getParameter("id").isEmpty()) {
            id = Integer.parseInt(request.getParameter("id"));
        }
        Meal meal = new Meal(id, localDateTime, description, calories);
        dao.save(meal);
        log.debug("request processed by servlet: method doPost finished");
        response.sendRedirect("meals");
    }
}
