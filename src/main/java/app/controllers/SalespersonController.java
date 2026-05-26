package app.controllers;

import app.config.ThymeleafConfig;
import app.persistence.RequestMapper;
import app.dtos.RequestSummaryDTO;
import app.entities.User;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


import java.sql.SQLException;
import java.util.List;

/**
 * SalespersonController handles the salesperson-facing pages.
 *
 * Routes:
 *   GET /salesperson/requests        → overview list of all requests
 *   GET /salesperson/request/{id}    → detail view for one request (to add later)
 *
 * ERD tables used:
 *   request       → rq_id, carp_id, ci_id, created_at, status
 *   carport       → width, length, rt_id
 *   roof_type     → name
 *   contact_info  → first_name, last_name, email, phone
 *   shed          → exists for carp_id? → hasShed flag
 */
public class SalespersonController {

    private final TemplateEngine templateEngine;
    private final RequestMapper requestMapper;

    public SalespersonController(TemplateEngine templateEngine,
                                 RequestMapper requestMapper) {
        this.templateEngine = templateEngine;
        this.requestMapper = requestMapper;
    }

    public void register(Javalin app) {
        app.get("/salesperson/requests",      this::showRequests);
        app.get("/salesperson/request/{id}",  this::showRequestDetail);
    }

    private void showRequests(Context ctx) throws SQLException {

        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        List<RequestSummaryDTO> requests = requestMapper.getAllSummaries();

        long total    = requests.size();
        long pending  = requests.stream().filter(r -> "PENDING".equals(r.getStatus())).count();
        long sent     = requests.stream().filter(r -> "SENT".equals(r.getStatus())).count();
        long accepted = requests.stream().filter(r -> "ACCEPTED".equals(r.getStatus())).count();

        WebContext webCtx = ThymeleafConfig.buildWebContext(ctx);
        webCtx.setVariable("requests",      requests);
        webCtx.setVariable("totalCount",    total);
        webCtx.setVariable("pendingCount",  pending);
        webCtx.setVariable("sentCount",     sent);
        webCtx.setVariable("acceptedCount", accepted);

        String html = templateEngine.process("salesperson-requests", webCtx);
        ctx.html(html);
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