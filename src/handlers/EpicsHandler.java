package handlers;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;
import server.HttpTaskServer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpHandler;

public class EpicsHandler implements HttpHandler {
    private final Gson gson = HttpTaskServer.getGson();
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int statusCode;
        String response;

        String method = exchange.getRequestMethod();
        String query = exchange.getRequestURI().getQuery();
        switch (method) {
            case "GET":
                if (query == null) {
                    statusCode = 200;
                    response = HttpTaskServer.getGson().toJson(manager.getAllEpics());
                    System.out.println("GET EPICS: " + response);

                } else {
                    try {
                        Integer id = Integer.parseInt(query.substring(query.indexOf("id:") + 3));
                        Epic epic = manager.getEpicById(id);
                        if (epic != null) {
                            response = gson.toJson(epic);
                            statusCode = 200;
                        } else {
                            response = "Эпик по заданному id не найден";
                            statusCode = 404;
                        }

                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Epic epic = gson.fromJson(bodyRequest, Epic.class);
                    Epic newEpic = new Epic(epic.getEpicId(), epic.getType(), epic.getName(),epic.getDescription());
                    Integer id = epic.getId();
                    if (manager.getEpicById(id) != null) {
                        manager.updateEpic(epic);
                        statusCode = 200;
                        response = "Эпик с id:" + id + " обновлен";
                    } else {
                        System.out.println("CREATED");
                        Epic epicCreated = manager.createEpic(epic);
                        System.out.println("CREATED EPIC: " + epicCreated);
                        Integer idCreated = epicCreated.getId();
                        statusCode = 201;
                        response = "Создан эпик с id:" + idCreated;
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                response = "";
                query = exchange.getRequestURI().getQuery();
                if (query == null) {
                    manager.deleteAllEpics();
                    statusCode = 201;
                    exchange.sendResponseHeaders(statusCode,-1);
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id:") + 3));
                        manager.deleteEpicById(id);
                        statusCode = 201;
                        exchange.sendResponseHeaders(statusCode,-1);
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
    }
}
