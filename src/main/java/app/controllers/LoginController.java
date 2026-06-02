package app.controllers;

import app.config.ThymeleafConfig;
import app.entities.Role;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.UserMapper;
import app.services.UserService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

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
        app.get("/register", this::showRegisterPage);
        app.post("/register", this::handleRegister);
    }

    private void showRegisterPage(Context ctx) {
        WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
        thymeleafCtx.setVariable("prevEmail",     ctx.queryParam("email"));
        thymeleafCtx.setVariable("prevFirstName", ctx.queryParam("firstName"));
        thymeleafCtx.setVariable("prevLastName",  ctx.queryParam("lastName"));
        thymeleafCtx.setVariable("prevAddress",   ctx.queryParam("address"));
        thymeleafCtx.setVariable("prevZipcode",   ctx.queryParam("zipcode"));
        thymeleafCtx.setVariable("prevTown",      ctx.queryParam("town"));
        thymeleafCtx.setVariable("prevPhone",     ctx.queryParam("phone"));
        ctx.html(templateEngine.process("register", thymeleafCtx));
    }

    private void handleRegister (Context ctx) {
        String email     = ctx.formParam("email");
        String password  = ctx.formParam("password");
        String firstName = ctx.formParam("firstName");
        String lastName  = ctx.formParam("lastName");
        String address   = ctx.formParam("address");
        String zipcode   = ctx.formParam("zipcode");
        String town      = ctx.formParam("town");
        String phone     = ctx.formParam("phone");

        try {
            UserService userService = new UserService(connectionPool);
            User newUser = new User(0, email, password, Role.CUSTOMER);
            userService.register(newUser);

            User loggedIn = UserMapper.findByEmailAndPassword(email, password, connectionPool);
            ctx.sessionAttribute("user", loggedIn);

            ctx.redirect("/order");

        } catch (Exception e) {
            WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
            thymeleafCtx.setVariable("errorMessage", e.getMessage());
            thymeleafCtx.setVariable("prevEmail",     email);
            thymeleafCtx.setVariable("prevFirstName", firstName);
            thymeleafCtx.setVariable("prevLastName",  lastName);
            thymeleafCtx.setVariable("prevAddress",   address);
            thymeleafCtx.setVariable("prevZipcode",   zipcode);
            thymeleafCtx.setVariable("prevTown",      town);
            thymeleafCtx.setVariable("prevPhone",     phone);
            ctx.html(templateEngine.process("register", thymeleafCtx));
        }
    }

    private void showLoginPage(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user != null) {
            if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
                ctx.redirect("/salesperson/requests");
            } else {
                ctx.redirect("/order");
            }
            return;

        }

        WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
        String html = templateEngine.process("login", thymeleafCtx);
        ctx.html(html);
    }

    private void handleLogin(Context ctx) {
        String email    = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = userMapper.findByEmailAndPassword(email, password, connectionPool);

            if (user == null) {
                WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
                thymeleafCtx.setVariable("errorMessage", "Forkert email eller adgangskode.");
                String html = templateEngine.process("login", thymeleafCtx);
                ctx.html(html);
                return;
            }

            ctx.sessionAttribute("user", user);

            if (user.getRole() == Role.EMPLOYEE || user.getRole() == Role.ADMIN) {
                ctx.redirect("/salesperson/requests");
            } else {
                ctx.redirect("/order");
            }

        } catch (DatabaseException e) {
            WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
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