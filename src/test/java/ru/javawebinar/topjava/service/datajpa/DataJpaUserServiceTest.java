package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User userFromDb = service.getWithMeals(USER_ID);
        USER_MATCHER.assertMatch(userFromDb, user);
        MealTestData.MEAL_MATCHER.assertMatch(userFromDb.getMeals(), MealTestData.meals);
    }

    @Test
    public void getWithMealsNotFound() {
        Assert.assertThrows(NotFoundException.class,
                () -> service.getWithMeals(1));
    }

    @Test
    public void getWithNoMeals() {
        User newUser = service.create(new User(null, "User without meals", "pooruser@yandex.ru",
                "newPass", Role.USER));
        User getUser = service.getWithMeals(newUser.id());
        List<Meal> nullList = new ArrayList<>();
        MealTestData.MEAL_MATCHER.assertMatch(getUser.getMeals(), nullList);
    }

}