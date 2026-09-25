package data.entities;

import data.misc.GsonExclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "order_x_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderProduct implements Serializable, Requestable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_product_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE} )
    @JoinColumn(name = "order_id")
    @GsonExclude
    private Order order;

    @EqualsAndHashCode.Include
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @EqualsAndHashCode.Include
    @Column (name = "product_count")
    private Integer productCount;
}
