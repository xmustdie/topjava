package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@Profile("hsqldb")
public class JdbcMealRepositoryHSQL extends AbstractJdbcMealRepository {
    public JdbcMealRepositoryHSQL(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("date_time", meal.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addValue("user_id", userId);

        if (meal.isNew()) {
            Number newId = super.getInsertMeal().executeAndReturnKey(map);
            meal.setId(newId.intValue());
        } else {
            if (super.getNamedParameterJdbcTemplate()
                    .update("UPDATE meals SET description=:description, calories=:calories, date_time=:date_time " +
                            " WHERE id=:id AND user_id=:user_id", map) == 0) {
                return null;
            }
        }
        return meal;
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDate, LocalDateTime endDate,
                                        int userId) {
        String start=startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end=endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(start);
        return super.getJdbcTemplate().query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN  ? AND ? ORDER BY date_time DESC",
                AbstractJdbcMealRepository.getRowMapper(), userId, start, end);
    }
}