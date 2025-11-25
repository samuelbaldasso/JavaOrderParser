package utils;

import entities.LineItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LineParser {

    private static final int USER_ID_START = 0;
    private static final int USER_ID_END = 10;

    private static final int NAME_START = 10;
    private static final int NAME_END = 55;

    private static final int ORDER_ID_START = 55;
    private static final int ORDER_ID_END = 65;

    private static final int PRODUCT_ID_START = 65;
    private static final int PRODUCT_ID_END = 75;

    private static final int VALUE_START = 75;
    private static final int VALUE_END = 87;

    private static final int DATE_START = 87;
    private static final int DATE_END = 95;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.BASIC_ISO_DATE;

    public LineItem parse(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Linha vazia ou nula");
        }

        if (line.length() < DATE_END) {
            throw new IllegalArgumentException("Linha com tamanho inválido: " + line.length());
        }

        String userIdStr = line.substring(USER_ID_START, USER_ID_END);
        String nameRaw = line.substring(NAME_START, NAME_END);
        String orderIdStr = line.substring(ORDER_ID_START, ORDER_ID_END);
        String productIdStr = line.substring(PRODUCT_ID_START, PRODUCT_ID_END);
        String valueStr = line.substring(VALUE_START, VALUE_END);
        String dateStr = line.substring(DATE_START, DATE_END);

        long userId = Long.parseLong(userIdStr);

        String name = nameRaw.stripLeading();

        long orderId = Long.parseLong(orderIdStr);
        long productId = Long.parseLong(productIdStr);

        BigDecimal value = new BigDecimal(valueStr.trim());
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

        return new LineItem(userId, name, orderId, productId, value, date);
    }
}
