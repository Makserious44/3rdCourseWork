package data.entities;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct implements Serializable {
    private Integer id;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Order order;
    private Product product;
    private Integer productCount;
}
