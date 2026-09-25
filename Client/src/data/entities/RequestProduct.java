package data.entities;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestProduct implements Serializable {
    private Integer id;
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private transient Request request;
    private Product product;
    private Integer productCount;
}
