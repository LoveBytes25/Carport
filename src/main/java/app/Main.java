package app;

import app.config.ThymeleafConfig;
import app.persistence.ConnectionPool;
import app.persistence.RequestMapper;
import app.controllers.SalespersonController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.thymeleaf.TemplateEngine;

public class Main
{
    public static void main(String[] args)
    {
        // Initialize connection pool
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        TemplateEngine templateEngine = ThymeleafConfig.templateEngine();

        RequestMapper requestMapper = new RequestMapper(connectionPool);

        Javalin app = Javalin.create(config ->
        {
            config.staticFiles.add("public", Location.CLASSPATH);
        }).start(7070);

        // Redirect root
        app.get("/", ctx -> ctx.redirect("/salesperson/requests"));

        // Register controllers
        new SalespersonController(templateEngine, requestMapper).register(app);

        System.out.println("Server running on http://localhost:7070");
    }
}