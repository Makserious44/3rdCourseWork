package gui.misc.converters;

import data.entities.Supplier;
import lombok.Data;

@Data
public class SupplierTableEntry {
    private Supplier supplier;

    private Integer id;
    private String name;
    private String description;
    private Integer userId;

    public SupplierTableEntry(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.description = supplier.getDescription();
        this.userId = supplier.getUser().getId();

        this.supplier = supplier;
    }

    public static ITableConverter<SupplierTableEntry, Supplier> converter = SupplierTableEntry::new;
}
