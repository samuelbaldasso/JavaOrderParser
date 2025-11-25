package utils;

import entities.Order;
import entities.Product;
import entities.User;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class JsonExporter {

    public String toJson(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            sb.append("  {\n");
            sb.append("    \"user_id\": ").append(user.getUserId()).append(",\n");
            sb.append("    \"name\": \"").append(escape(user.getName())).append("\",\n");
            sb.append("    \"orders\": [\n");

            List<Order> orders = user.getOrders();
            for (int j = 0; j < orders.size(); j++) {
                Order order = orders.get(j);
                sb.append("      {\n");
                sb.append("        \"order_id\": ").append(order.getOrderId()).append(",\n");
                sb.append("        \"total\": \"").append(formatMoney(order.getTotal())).append("\",\n");
                sb.append("        \"date\": \"").append(order.getDate()).append("\",\n");
                sb.append("        \"products\": [\n");

                List<Product> products = order.getProducts();
                for (int k = 0; k < products.size(); k++) {
                    Product product = products.get(k);
                    sb.append("          {\n");
                    sb.append("            \"product_id\": ").append(product.getProductId()).append(", ");
                    sb.append("\"value\": \"").append(formatMoney(product.getValue())).append("\"\n");
                    sb.append("          }");
                    if (k < products.size() - 1) {
                        sb.append(",");
                    }
                    sb.append("\n");
                }

                sb.append("        ]\n");
                sb.append("      }");
                if (j < orders.size() - 1) {
                    sb.append(",");
                }
                sb.append("\n");
            }

            sb.append("    ]\n");
            sb.append("  }");
            if (i < users.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }

        sb.append("]\n");
        return sb.toString();
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            return "0.00";
        }
        return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}
