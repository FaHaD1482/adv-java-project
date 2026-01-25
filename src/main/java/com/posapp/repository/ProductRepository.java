package com.posapp.repository;

import com.posapp.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product save(Product product) {
        String sql = "INSERT INTO products (name, description, price, category, supplier, stock, created_at, updated_at) " +
                     "VALUES (:name, :description, :price, :category, :supplier, :stock, NOW(), NOW())";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", product.getName())
                .addValue("description", product.getDescription())
                .addValue("price", product.getPrice())
                .addValue("category", product.getCategory())
                .addValue("supplier", product.getSupplier())
                .addValue("stock", product.getStock());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        product.setId(keyHolder.getKey().intValue());
        return product;
    }

    public Optional<Product> findById(Integer id) {
        String sql = "SELECT * FROM products WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            Product product = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(Product.class));
            return Optional.of(product);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<Product> findAll(int page, int size) {
        String sql = "SELECT * FROM products ORDER BY id ASC LIMIT :size OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", page * size);

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(Product.class));
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM products";
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
    }

    public void update(Product product) {
        String sql = "UPDATE products SET name = :name, description = :description, price = :price, " +
                     "category = :category, supplier = :supplier, stock = :stock, updated_at = NOW() " +
                     "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", product.getId())
                .addValue("name", product.getName())
                .addValue("description", product.getDescription())
                .addValue("price", product.getPrice())
                .addValue("category", product.getCategory())
                .addValue("supplier", product.getSupplier())
                .addValue("stock", product.getStock());

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updateStock(Integer id, Integer newStock) {
        String sql = "UPDATE products SET stock = :stock, updated_at = NOW() WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("stock", newStock);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void delete(Integer id) {
        String sql = "DELETE FROM products WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = :category";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("category", category);

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(Product.class));
    }
}
