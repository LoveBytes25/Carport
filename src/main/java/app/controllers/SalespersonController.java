package app.controllers;

import app.config.ThymeleafConfig;
import app.dtos.RequestSummaryDTO;
import app.entities.CarportComponent;
import app.entities.PriceSummary;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.RequestMapper;
import app.services.MaterialCalculationService;
import app.services.PriceCalculationService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;

import java.sql.SQLException;
import java.util.List;

public class SalespersonController {

    private final TemplateEngine templateEngine;
    private final RequestMapper  requestMapper;
    private final MaterialCalculationService materialService;
    private final PriceCalculationService priceService;

    public SalespersonController(TemplateEngine templateEngine,
                                 RequestMapper  requestMapper, ConnectionPool connectionPool) {
        this.templateEngine = templateEngine;
        this.requestMapper  = requestMapper;
        this.materialService = new MaterialCalculationService(connectionPool);
        this.priceService = new PriceCalculationService(connectionPool);
    }

    public void register(Javalin app) {
        app.get("/salesperson/requests",     this::showRequests);
        app.get("/salesperson/request/{id}", this::showRequestDetail);
        app.post("/salesperson/request/{id}/status", this::updateStatus);
    }

    private void showRequests(Context ctx) {
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

    ctx.html(templateEngine.process("requests", thymeleafCtx));

} catch (Exception e){
    e.printStackTrace();
    ctx.result("Fejl ved indhentning af forespørgsler: " + e.getMessage());
        }
    }

    private void showRequestDetail(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user == null) {
            ctx.redirect("/login");
            return;
        }

        try {
            int rqId = Integer.parseInt(ctx.pathParam("id"));

            RequestSummaryDTO detail = requestMapper.getDetail(rqId);

            String roofType = detail.getCarpHeight() > 90 ? "pitch" : "flat";
            double roofAngle = detail.getCarpHeight() > 90 ? detail.getCarpHeight() : 0;

            List<CarportComponent> bom = materialService.calculate(
                    detail.getCarpLength(),
                    detail.getCarpWidth(),
                    roofType,
                    roofAngle
            );

            // Købspris
            PriceSummary priceSummary = priceService.calculatePrice(bom);

            // Dækningsgrad
            double coverageRate = (priceSummary.getSalesPriceExclVat() - priceSummary.getMaterialPrice()) / priceSummary.getSalesPriceExclVat() * 100;

            org.thymeleaf.context.Context tc = new org.thymeleaf.context.Context();
            tc.setVariable("detail",       detail);
            tc.setVariable("bom",          bom);
            tc.setVariable("priceSummary", priceSummary);
            tc.setVariable("coverageRate", String.format("%.1f", coverageRate));
            tc.setVariable("user",         user);

            ctx.html(templateEngine.process("request", tc));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.result("Fejl ved hentning af forespørgsel: " + e.getMessage());
        }
    }

    private void updateStatus(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user == null) { ctx.redirect("/login"); return; }

        try {
            int rqId = Integer.parseInt(ctx.pathParam("Id"));
            String status = ctx.formParam("status");
            requestMapper.updateStatus(rqId, status);
            ctx.redirect("/salesperson/request/" + rqId);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.result("Fejl: " + e.getMessage());
        }
    }
}