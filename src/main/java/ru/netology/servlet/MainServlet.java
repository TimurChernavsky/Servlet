package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private static PostController controller;
    private static String METHOD_GET = "GET";
    private static String METHOD_DELETE = "DELETE";
    private static String METHOD_POST = "POST";

    private static String WAY_api_posts = "/api/posts";

    @Controller
    public void init() {
        final var context = new AnnotationConfigApplicationContext(JavaConfig.class);
        controller = context.getBean(PostController.class);
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
            if (method.equals(METHOD_GET) && path.equals(WAY_api_posts)) {
                controller.all(resp);
                return;
            } else if (method.equals(METHOD_GET) && path.matches(WAY_api_posts + "\\d")) {
                // easy way
                final var id = getID(path);
                controller.getById(id, resp);
                return;
            } else if (method.equals(METHOD_POST) && path.equals(WAY_api_posts)) {
                controller.save(req.getReader(), resp);
                return;
            } else if (method.equals(METHOD_DELETE) && path.matches(WAY_api_posts + "\\d")) {
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

