package connection;

import connection.RequestType;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Request implements Serializable {
    @NonNull
    private RequestType requestType;
    private String data;
}
