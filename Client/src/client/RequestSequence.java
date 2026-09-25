package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.Request;
import connection.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RequestSequence {
    private RequestSequence() {}

    public static Response sendRequest(Request request) throws IOException {
        System.out.println(request);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(request));

        String jsonResponse = ClientSocket.getInstance().getIn().readLine();

        System.out.println(jsonResponse);

        return new Gson().fromJson(jsonResponse, Response.class);
    }

    public static <T> List<T> deserializeResponse(Response response, Class<T> deserializeClass) {
        Type type = TypeToken.getParameterized(ArrayList.class, deserializeClass).getType();
        return new Gson().fromJson(response.getData(), type);
    }
}
