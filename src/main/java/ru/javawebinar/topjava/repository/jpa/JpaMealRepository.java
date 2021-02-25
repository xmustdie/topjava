package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        } else {
            User user = entityManager.getReference(User.class, userId);
            meal.setUser(user);
            if (meal.isNew()) {
                entityManager.persist(meal);
                return meal;
            } else {
                return entityManager.merge(meal);
            }
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return entityManager.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> resultList =
                entityManager.createNamedQuery(Meal.GET, Meal.class)
                        .setParameter("id", id)
                        .setParameter("user_id", userId)
                        .getResultList();
        return DataAccessUtils.singleResult(resultList);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return entityManager.createNamedQuery(Meal.GET_ALL, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return entityManager.createNamedQuery(Meal.GET_BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("startTime", startDateTime)
                .setParameter("endTime", endDateTime)
                .setParameter("user_id", userId)
                .getResultList();
    }
}