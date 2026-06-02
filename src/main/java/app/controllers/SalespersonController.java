package app.controllers;

import app.config.ThymeleafConfig;
import app.dtos.RequestSummaryDTO;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.RequestMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;

import java.sql.SQLException;
import java.util.List;

public class SalespersonController {

    private final TemplateEngine templateEngine;
    private final RequestMapper  requestMapper;

    public SalespersonController(TemplateEngine templateEngine,
                                 RequestMapper  requestMapper) {
        this.templateEngine = templateEngine;
        this.requestMapper  = requestMapper;
    }

    public void register(Javalin app) {
        app.get("/salesperson/requests",     this::showRequests);
        app.get("/salesperson/request/{id}", this::showRequestDetail);
    }

    private void showRequests(Context ctx) throws SQLException, DatabaseException {
        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

try {
    List<RequestSummaryDTO> requests = requestMapper.getAllSummaries();

    long total = requests.size();
    long pending = requests.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
    long sent = requests.stream().filter(r -> "SENT".equals(r.getStatus())).count();
    long accepted = requests.stream().filter(r -> "ACCEPTED".equals(r.getStatus())).count();

    org.thymeleaf.context.Context thymeleafCtx = new org.thymeleaf.context.Context();
    thymeleafCtx.setVariable("requests", requests);
    thymeleafCtx.setVariable("totalCount", total);
    thymeleafCtx.setVariable("pendingCount", pending);
    thymeleafCtx.setVariable("sentCount", sent);
    thymeleafCtx.setVariable("acceptedCount", accepted);
    thymeleafCtx.setVariable("user", user);

    String html = templateEngine.process("requests", thymeleafCtx);
    ctx.html(html);

} catch (Exception e){
    ctx.result("Database error: " + e.getMessage());
        }
    }

    private void showRequestDetail(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        int rqId = Integer.parseInt(ctx.pathParam("id"));

        ctx.result("Detail view for request #" + rqId + " — coming soon");
    }

}