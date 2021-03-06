package com.es.phoneshop.core.order.dao;

import com.es.phoneshop.core.order.model.Order;
import com.es.phoneshop.core.order.model.OrderItem;
import com.es.phoneshop.core.order.model.OrderStatus;
import com.es.phoneshop.core.util.SQLQueries;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Stream;

@Component
public class JdbcOrderDao implements OrderDao {
    @Resource
    private OrderRowMapper orderRowMapper;
    @Resource
    private OrderItemRowMapper orderItemRowMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Optional<Order> get(String id) {
        try {
            Order order = jdbcTemplate.queryForObject(SQLQueries.GET_ORDER, orderRowMapper, id);
            populateOrderItems(order);
            return Optional.of(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private void populateOrderItems(Order order) {
        List<OrderItem> orderItems = jdbcTemplate.query(SQLQueries.GET_ORDER_ITEMS, orderItemRowMapper, order.getId());
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);
    }

    @Override
    public void save(Order order) throws IllegalArgumentException {
        Integer test = jdbcTemplate.queryForObject(SQLQueries.TEST_ORDER, Integer.class, order.getId());
        if (test != 0)
            throw new IllegalArgumentException();
        namedParameterJdbcTemplate.update(SQLQueries.INSERT_ORDER, prepareOrderMap(order));
        Map<String, Object>[] mapBatchArgs = order.getOrderItems().stream()
                .map(orderItem -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("orderId", order.getId());
                    map.put("phoneId", orderItem.getPhone().getId());
                    map.put("quantity", orderItem.getQuantity());
                    return map;
                })
                .toArray((IntFunction<Map<String, Object>[]>) Map[]::new);
        namedParameterJdbcTemplate.batchUpdate(SQLQueries.INSERT_ORDER_ITEMS, mapBatchArgs);
    }

    private Map<String, Object> prepareOrderMap(Order order) {
        return Stream.of(
                new AbstractMap.SimpleEntry<>("id", order.getId()),
                new AbstractMap.SimpleEntry<>("subtotal", order.getSubtotal()),
                new AbstractMap.SimpleEntry<>("deliveryPrice", order.getDeliveryPrice()),
                new AbstractMap.SimpleEntry<>("totalPrice", order.getTotalPrice()),
                new AbstractMap.SimpleEntry<>("firstName", order.getFirstName()),
                new AbstractMap.SimpleEntry<>("lastName", order.getLastName()),
                new AbstractMap.SimpleEntry<>("deliveryAddress", order.getDeliveryAddress()),
                new AbstractMap.SimpleEntry<>("contactPhoneNo", order.getContactPhoneNo()),
                new AbstractMap.SimpleEntry<>("additionalInformation", order.getAdditionalInformation()),
                new AbstractMap.SimpleEntry<>("status", order.getStatus().toString())
        ).collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
    }

    @Override
    public boolean isIdUnique(String id) {
        return jdbcTemplate.queryForObject(SQLQueries.TEST_ORDER, Integer.class, id) == 0;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = jdbcTemplate.query(SQLQueries.GET_ORDERS, orderRowMapper);
        orders.forEach(this::populateOrderItems);
        return orders;
    }

    @Override
    public void updateStatus(String id, OrderStatus status) {
        jdbcTemplate.update(SQLQueries.UPDATE_ORDER_STATUS, status.toString(), id);
    }
}
