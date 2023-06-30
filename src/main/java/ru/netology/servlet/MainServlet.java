package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static String METHOD_GET = "GET";
    private static String METHOD_DELETE = "DELETE";
    private static String METHOD_POST = "POST";

    private static String API_POST_PATH = "/api/posts \\d";

    @Controller
    public void init() {
    final var repository = new PostRepository();
    final var service = new PostService(repository);
    controller = new PostController(service);
    }

    private Long getID(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(METHOD_GET) && path.equals(API_POST_PATH)) {
                controller.all(resp);
                return;
            } else if (method.equals(METHOD_GET) && path.matches(API_POST_PATH)) {
                // easy way
                final var id = getID(path);
                controller.getById(id, resp);
                return;
            } else if (method.equals(METHOD_POST) && path.equals(API_POST_PATH)) {
                controller.save(req.getReader(), resp);
                return;
            } else if (method.equals(METHOD_DELETE) && path.matches(API_POST_PATH)) {
                // easy way
                final var id = getID(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}

