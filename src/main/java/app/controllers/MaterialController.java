package app.controllers;

import app.entities.CarportComponent;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.services.MaterialCalculationService;

import io.javalin.Javalin;
import io.javalin.http.Context;

import org.thymeleaf.TemplateEngine;

import java.util.List;

public class MaterialController {

    private final TemplateEngine templateEngine;
    private final MaterialCalculationService materialService;

    public MaterialController(TemplateEngine templateEngine, ConnectionPool connectionPool) {

        this.templateEngine = templateEngine;

        this.materialService = new MaterialCalculationService(connectionPool);
    }

    public void register(Javalin app) {

        app.post("/calculate-materials", this::calculateMaterials);
    }

    private void calculateMaterials(Context ctx)
            throws DatabaseException {

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

        org.thymeleaf.context.Context thymeleafCtx = new org.thymeleaf.context.Context();

        thymeleafCtx.setVariable("bom", bom);

        String html = templateEngine.process("bom", thymeleafCtx);

        ctx.html(html);
    }
}