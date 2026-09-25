package data.entities;

import com.google.gson.annotations.Expose;
import data.misc.GsonExclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table (name = "request_x_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RequestProduct implements Serializable, Requestable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "request_product_id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ToString.Exclude
    @ManyToOne (fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
    @JoinColumn(name = "request_id")
    @GsonExclude
    private Request request;

    @EqualsAndHashCode.Include
    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;

    @EqualsAndHashCode.Include
    @Column (name = "product_count")
    private Integer productCount;
}
