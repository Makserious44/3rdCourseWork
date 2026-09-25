package gui.misc.converters;

import data.entities.OrderProduct;
import data.entities.RequestProduct;
import lombok.Data;

@Data
public class OrderProductTableEntry {
    private OrderProduct orderProduct;

    private String productName;
    private Integer productCount;

    public OrderProductTableEntry(OrderProduct orderProduct) {
        this.orderProduct = orderProduct;

        this.productName = orderProduct.getProduct().getName();
        this.productCount = orderProduct.getProductCount();
    }

    public OrderProductTableEntry(RequestProduct request) {
        this.orderProduct = new OrderProduct();
        orderProduct.setProduct(request.getProduct());
        orderProduct.setProductCount(request.getProductCount());

        this.productName = request.getProduct().getName();
        this.productCount = request.getProductCount();
    }

    public static ITableConverter<OrderProductTableEntry, OrderProduct> converter = OrderProductTableEntry::new;
}
