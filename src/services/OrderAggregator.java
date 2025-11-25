package services;

import entities.LineItem;
import entities.Order;
import entities.Product;
import entities.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderAggregator {

    public List<User> aggregate(List<LineItem> items) {
        Map<Long, User> usersById = new LinkedHashMap<>();
        Map<Long, Map<Long, Order>> ordersByUser = new LinkedHashMap<>();

        for (LineItem item : items) {
            long userId = item.userId();
            long orderId = item.orderId();

            User user = usersById.get(userId);
            if (user == null) {
                user = new User();
                user.setUserId(userId);
                user.setName(item.name());
                usersById.put(userId, user);
            }

            Map<Long, Order> ordersForUser = ordersByUser.computeIfAbsent(userId, k -> new LinkedHashMap<>());

            Order order = ordersForUser.get(orderId);
            if (order == null) {
                order = new Order();
                order.setOrderId(orderId);
                order.setDate(item.date());
                order.setTotal(BigDecimal.ZERO);
                ordersForUser.put(orderId, order);
                user.addOrder(order);
            }

            Product product = new Product();
            product.setProductId(item.productId());
            product.setValue(item.value());
            order.addProduct(product);

            BigDecimal newTotal = order.getTotal().add(item.value());
            order.setTotal(newTotal);
        }

        return new ArrayList<>(usersById.values());
    }
}
