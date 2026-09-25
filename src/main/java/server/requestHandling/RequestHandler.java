package server.requestHandling;

import com.google.gson.GsonBuilder;
import connection.Request;
import connection.Response;
import data.DAO.DAOException;
import data.entities.Requestable;
import data.misc.GsonExclusionStrategy;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class RequestHandler {
    @Setter
    private RequestHandlingStrategy strategy;

    public Response handleRequest(Request request) throws RuntimeException {
        if (strategy == null) throw new RuntimeException("Missing strategy");
        if (request == null) throw new IllegalArgumentException("Request to handle is null");

        List<? extends Requestable> handledRequestResult;

        try {
            handledRequestResult = strategy.action(request);
        }
        catch (DAOException e) {
            System.out.println("Failed to handle request: " + e.getMessage());
            return new Response(false, e.getMessage(), null);
        }

        System.out.println(handledRequestResult);

        if (handledRequestResult == null || handledRequestResult.isEmpty())
            return new Response(false, "No entries found", null);

        return new Response(true,
                "Found " + handledRequestResult.size() + " entries",
                new GsonBuilder()
                        .setExclusionStrategies(new GsonExclusionStrategy())
                        .serializeNulls()
                        .create()
                        .toJson(handledRequestResult));
    }

}
