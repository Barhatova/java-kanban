package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import service.TaskManager;
import server.HttpTaskServer;
import java.io.IOException;

public class PrioritizedHandler implements HttpHandler {
        private final Gson gson = HttpTaskServer.getGson();
        private final TaskManager manager;

        public PrioritizedHandler(TaskManager manager) {
            this.manager = manager;
        }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode = 400;
        String response;
        String method = exchange.getRequestMethod();
        String path = String.valueOf(exchange.getRequestURI());

        System.out.println("Обработка запроса " + path + " с методом " + method);

        switch (method) {
            case "GET":
                statusCode = 200;
                response = gson.toJson(manager.getPrioritizedTasks());
                break;
            default:
                response = "Некорректный запрос";
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
    }
}

