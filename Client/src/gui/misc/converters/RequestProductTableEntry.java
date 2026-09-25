package gui.misc.converters;

import data.entities.RequestProduct;
import lombok.Data;

@Data
public class RequestProductTableEntry {
    private RequestProduct requestProduct;

    private String productName;
    private Integer productCount;

    public RequestProductTableEntry(RequestProduct requestProduct) {
        this.requestProduct = requestProduct;

        this.productName = requestProduct.getProduct().getName();
        this.productCount = requestProduct.getProductCount();
    }

    public static ITableConverter<RequestProductTableEntry, RequestProduct> converter = RequestProductTableEntry::new;
}
