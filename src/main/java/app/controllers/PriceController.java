package app.controllers;

import app.config.ThymeleafConfig;
import app.entities.CarportComponent;
import app.entities.PriceSummary;

import app.exceptions.DatabaseException;

import app.persistence.ConnectionPool;

import app.services.MaterialCalculationService;
import app.services.PriceCalculationService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import java.util.List;

public class PriceController {

    private final TemplateEngine templateEngine;

    private final MaterialCalculationService materialService;

    private final PriceCalculationService priceService;

    public PriceController(TemplateEngine templateEngine, ConnectionPool connectionPool) {

        this.templateEngine = templateEngine;

        this.materialService = new MaterialCalculationService(connectionPool);

        this.priceService = new PriceCalculationService(connectionPool);
    }

    public void register(Javalin app) {

        app.post("/calculate-price", this::calculatePrice);
    }

    private void calculatePrice(Context ctx) throws DatabaseException {

        double length = Double.parseDouble(ctx.formParam("length"));

        double width = Double.parseDouble(ctx.formParam("width"));

        String roofType = ctx.formParam("roofType");

        double roofAngle = Double.parseDouble(ctx.formParam("roofAngle"));

        List<CarportComponent> bom =
                materialService.calculate(
                        length,
                        width,
                        roofType,
                        roofAngle
                );

        PriceSummary summary = priceService.calculatePrice(bom);

        WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);

        thymeleafCtx.setVariable("bom", bom);

        thymeleafCtx.setVariable("priceSummary", summary);

        String html = templateEngine.process("offer", thymeleafCtx);

        ctx.html(html);
    }
}