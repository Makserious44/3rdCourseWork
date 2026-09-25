package gui.misc.converters;

import data.entities.Request;
import data.entities.RequestProduct;
import lombok.Data;

import java.util.Map;
import java.util.stream.Collectors;

@Data
public class RequestTableEntry {
    private Request request;
    private Integer id;
    private Integer userId;
    private Map<String, Integer> productNames;
//    private List<Product> products;

    public RequestTableEntry(Request request) {
        this.id = request.getId();
        this.userId = request.getSubmitter().getId();
        this.productNames = request.getProducts()
                .stream()
                .collect(
                        Collectors.toMap(
                                rp -> rp.getProduct().getName(),
                                RequestProduct::getProductCount,
                                (p1, p2) -> p2)
                );
//        this.products = request.getProducts().stream().toList();

        this.request = request;
    }

    public static ITableConverter<RequestTableEntry, Request> converter = RequestTableEntry::new;
}
