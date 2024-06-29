package handlers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;
import server.HttpTaskServer;
import java.io.IOException;
import com.sun.net.httpserver.HttpHandler;

public class HistoryHandler implements HttpHandler {
    private final Gson gson = HttpTaskServer.getGson();
    private final TaskManager manager;

    public HistoryHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode = 400;
        String response;
        String method = exchange.getRequestMethod();
        String path = String.valueOf(exchange.getRequestURI());

        System.out.println("Обработка запроса " + path + " с методом " + method);

        if (method.equals("GET")) {
            statusCode = 200;
            response = gson.toJson(manager.getHistory());
        } else {
            response = "Некорректный запрос";
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
    }
}
