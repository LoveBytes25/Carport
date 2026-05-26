package app;

import app.config.ConnectionPool;
import app.config.ThymeleafConfig;
import app.persistence.RequestMapper;
import app.controllers.SalespersonController;
import io.javalin.Javalin;
import org.thymeleaf.TemplateEngine;

public class Main {

    private static final String DB_URL  = System.getenv("jdbc:postgresql://localhost:5432/carport");
    private static final String DB_USER = System.getenv("postgres");
    private static final String DB_PASS = System.getenv("postgres");

    public static void main(String[] args) {

        ConnectionPool connectionPool = ConnectionPool.getInstance(DB_URL, DB_USER, DB_PASS);
        TemplateEngine templateEngine = ThymeleafConfig.templateEngine();

        RequestMapper requestMapper = new RequestMapper(connectionPool);

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(7070);

        // Redirect root to login or requests
        app.get("/", ctx -> ctx.redirect("/salesperson/requests"));

        // Controllers
        new SalespersonController(templateEngine, requestMapper).register(app);

        System.out.println("Server running on http://localhost:7070");
    }
}