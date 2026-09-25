package gui.misc.converters;

import data.entities.Product;
import lombok.Data;

@Data
public class ProductTableEntry {
    private Product product;

    private Integer id;
    private String name;
    private String description;
    private Integer price;

    public ProductTableEntry(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getRetailPrice();

        this.product = product;
    }

    public static ITableConverter<ProductTableEntry, Product> converter = ProductTableEntry::new;
}
