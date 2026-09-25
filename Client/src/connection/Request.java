package connection;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Request implements Serializable {
    @NonNull
    private RequestType requestType;
    private String data;
}
