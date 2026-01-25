package com.posapp.repository;

import com.posapp.entity.InventoryTransaction;
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
public class InventoryTransactionRepository {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public InventoryTransaction save(InventoryTransaction transaction) {
        String sql = "INSERT INTO inventory_transactions (product_id, user_id, type, quantity, notes, trans_at) " +
                     "VALUES (:productId, :userId, :type, :quantity, :notes, NOW())";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("productId", transaction.getProductId())
                .addValue("userId", transaction.getUserId())
                .addValue("type", transaction.getType())
                .addValue("quantity", transaction.getQuantity())
                .addValue("notes", transaction.getNotes());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder);

        transaction.setId(keyHolder.getKey().intValue());
        return transaction;
    }

    public Optional<InventoryTransaction> findById(Integer id) {
        String sql = "SELECT * FROM inventory_transactions WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            InventoryTransaction transaction = namedParameterJdbcTemplate.queryForObject(sql, params,
                    new BeanPropertyRowMapper<>(InventoryTransaction.class));
            return Optional.of(transaction);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<InventoryTransaction> findAll(int page, int size) {
        String sql = "SELECT * FROM inventory_transactions ORDER BY trans_at DESC LIMIT :size OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("size", size)
                .addValue("offset", page * size);

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(InventoryTransaction.class));
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM inventory_transactions";
        return namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource(), Long.class);
    }

    public List<InventoryTransaction> findByProductId(Integer productId) {
        String sql = "SELECT * FROM inventory_transactions WHERE product_id = :productId ORDER BY trans_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("productId", productId);

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(InventoryTransaction.class));
    }

    public List<InventoryTransaction> findByUserId(Integer userId) {
        String sql = "SELECT * FROM inventory_transactions WHERE user_id = :userId ORDER BY trans_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId);

        return namedParameterJdbcTemplate.query(sql, params,
                new BeanPropertyRowMapper<>(InventoryTransaction.class));
    }
}
