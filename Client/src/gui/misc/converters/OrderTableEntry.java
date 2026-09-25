package gui.misc.converters;

import data.entities.Order;
import data.entities.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderTableEntry {
    private Order order;

    private Integer id;
    private Integer ordererId;
    private Integer supplierId;
    private Map<String, Integer> productNames;

    public OrderTableEntry(Order order) {
        System.out.println(order);
        this.id = order.getId();
        this.ordererId = order.getOrderer().getId();
        this.supplierId = order.getSupplier().getId();

        this.productNames = order.getProducts()
                .stream()
                .collect(
                        Collectors.toMap(
                                rp -> rp.getProduct().getName(),
                                OrderProduct::getProductCount,
                                (p1, p2) -> p2)
                );

        this.order = order;
    }

    public static ITableConverter<OrderTableEntry, Order> converter = OrderTableEntry::new;
}
