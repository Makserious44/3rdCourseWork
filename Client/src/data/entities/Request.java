package data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Integer id;
    private Set<RequestProduct> products;
    private User submitter;
}
