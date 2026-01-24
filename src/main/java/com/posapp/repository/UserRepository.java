package com.posapp.repository;

import com.posapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public User save(User user) {
        String sql = "INSERT INTO users (username, email, password, role, created_at, updated_at) " +
                     "VALUES (:username, :email, :password, :role, NOW(), NOW())";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", user.getUsername())
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("role", user.getRole());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = :username";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("username", username);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(User.class));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(User.class));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<User> findById(Integer id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(User.class));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET email = :email, updated_at = NOW() WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", user.getId())
                .addValue("email", user.getEmail());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updatePassword(Integer userId, String password) {
        String sql = "UPDATE users SET password = :password, updated_at = NOW() WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("password", password);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updateResetToken(Integer userId, String token) {
        String sql = "UPDATE users SET reset_token = :token WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("token", token);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public Optional<User> findByResetToken(String token) {
        String sql = "SELECT * FROM users WHERE reset_token = :token";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("token", token);

        try {
            User user = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(User.class));
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
