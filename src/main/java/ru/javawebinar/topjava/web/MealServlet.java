package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static final String CONFIG_LOCATION = "spring/spring-app.xml";

    private ConfigurableApplicationContext applicationContext;
    private MealRestController restController;


    @Override
    public void init() {
        applicationContext = new ClassPathXmlApplicationContext(CONFIG_LOCATION);
        System.out.printf("Bean definition names: %s%n", Arrays.toString(applicationContext.getBeanDefinitionNames()));
        AdminRestController adminUserController = applicationContext.getBean(AdminRestController.class);
        adminUserController.create(new User(null, "userName", "email@mail.ru", "password",
                Role.ADMIN));
        restController = applicationContext.getBean(MealRestController.class);
    }


    @Override
    public void destroy() {
        applicationContext.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        restController.save(meal, 1);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                restController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        restController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "filter":
                String parameterStartDate = request.getParameter("startDate");
                String parameterEndDate = request.getParameter("endDate");
                String parameterStartTime = request.getParameter("startTime");
                String parameterEndTime = request.getParameter("endTime");
                LocalDate startDate = parameterStartDate.isEmpty() ? LocalDate.MIN :
                        LocalDate.parse(parameterStartDate);
                LocalDate endDate = parameterEndDate.isEmpty() ? LocalDate.MAX :
                        LocalDate.parse(parameterEndDate);
                LocalTime startTime = parameterStartTime.isEmpty() ? LocalTime.MIN :
                        LocalTime.parse(parameterStartTime);
                LocalTime endTime = parameterEndTime.isEmpty() ? LocalTime.MAX :
                        LocalTime.parse(parameterEndTime);
                request.setAttribute("meals", restController.getBetween(startDate, endDate, startTime, endTime));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
            case "all":
            default:
                request.setAttribute("meals", restController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
