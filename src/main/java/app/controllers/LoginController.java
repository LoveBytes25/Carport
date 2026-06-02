package app.controllers;

import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;

public class LoginController {

    private final TemplateEngine templateEngine;
    private final UserMapper     userMapper;
    private final ConnectionPool connectionPool;

    public LoginController(TemplateEngine templateEngine,
                           UserMapper userMapper, ConnectionPool connectionPool) {
        this.templateEngine = templateEngine;
        this.userMapper     = userMapper;
        this.connectionPool = connectionPool;
    }

    public void register(Javalin app) {
        app.get("/login",  this::showLoginPage);
        app.post("/login", this::handleLogin);
        app.get("/logout", this::handleLogout);
    }

    private void showLoginPage(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user != null) {
            ctx.redirect("/salesperson/requests");
            return;
        }

        org.thymeleaf.context.Context thymeleafCtx = new org.thymeleaf.context.Context();
        String html = templateEngine.process("login", thymeleafCtx);
        ctx.html(html);
    }

    private void handleLogin(Context ctx) {
        String email    = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = userMapper.findByEmailAndPassword(email, password, connectionPool);

            if (user == null) {
                org.thymeleaf.context.Context thymeleafCtx = new org.thymeleaf.context.Context();
                thymeleafCtx.setVariable("errorMessage", "Forkert email eller adgangskode.");
                String html = templateEngine.process("login", thymeleafCtx);
                ctx.html(html);
                return;
            }

            ctx.sessionAttribute("user", user);
            ctx.redirect("/salesperson/requests");

        } catch (DatabaseException e) {
            org.thymeleaf.context.Context thymeleafCtx = new org.thymeleaf.context.Context();
            thymeleafCtx.setVariable("errorMessage", "Der opstod en fejl. Prøv igen.");
            String html = templateEngine.process("login", thymeleafCtx);
            ctx.html(html);
        }
    }

    private void handleLogout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.redirect("/login");
    }
}