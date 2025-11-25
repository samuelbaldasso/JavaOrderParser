package entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LineItem(long userId, String name, long orderId, long productId, BigDecimal value, LocalDate date) {
}
