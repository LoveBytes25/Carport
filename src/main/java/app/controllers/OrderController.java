package app.controllers;

import app.config.ThymeleafConfig;
import app.entities.*;
import app.exceptions.DatabaseException;
import app.persistence.RequestMapper;
import io.javalin.Javalin;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;


import java.util.List;

public class OrderController {

    private final TemplateEngine templateEngine;
    private final RequestMapper  requestMapper;

    public OrderController(TemplateEngine templateEngine,
                           RequestMapper  requestMapper) {
        this.templateEngine = templateEngine;
        this.requestMapper  = requestMapper;
    }

    public void register(Javalin app) {
        app.get("/order",           this::showOrderForm);
        app.post("/request/submit", this::submitRequest);
    }

    private void showOrderForm(io.javalin.http.Context ctx) throws DatabaseException {
        User loggedInUser = ctx.sessionAttribute("user");
        ContactInfo contactInfo = null;

        if (loggedInUser != null) {
            contactInfo = requestMapper.findContactInfoByUserId(loggedInUser.getId());
        }

        // Fetch roof types for the tagtype dropdown
        List<RoofType> roofTypes = requestMapper.getAllRoofTypes();

        WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
        thymeleafCtx.setVariable("roofTypes",   roofTypes);
        thymeleafCtx.setVariable("contactInfo", contactInfo);
        thymeleafCtx.setVariable("user",        loggedInUser);

        String html = templateEngine.process("order", thymeleafCtx);
        ctx.html(html);
    }

    private void submitRequest(io.javalin.http.Context ctx) throws DatabaseException {
        User loggedInUser = ctx.sessionAttribute("user");

        try {
            int    rtId   = Integer.parseInt(ctx.formParam("rtId"));
            double width  = Double.parseDouble(ctx.formParam("width"));
            double length = Double.parseDouble(ctx.formParam("length"));
            String roofType = ctx.formParam("roofType");

            double height = 210; // default for flat roof
            if ("pitch".equals(roofType)) {
                String slopeStr = ctx.formParam("slope");
                height = slopeStr != null ? Double.parseDouble(slopeStr) : 25;
            }

            // Resolve or create contact_info
            int ciId;
            if (loggedInUser != null) {
                ContactInfo existing = requestMapper.findContactInfoByUserId(loggedInUser.getId());
                ciId = existing != null
                        ? existing.getId()
                        : createContactInfo(ctx, loggedInUser.getId());
            } else {
                ciId = createContactInfo(ctx, null);
            }

            // Persist carport
            Integer userId = loggedInUser != null ? loggedInUser.getId() : null;
            int carpId = requestMapper.createCarport(userId, rtId, length, width, height);

            // Persist shed (optional)
            String includeShedStr = ctx.formParam("includeShed");
            boolean includeShed = "on".equals(includeShedStr) || "true".equals(includeShedStr);
            if (includeShed) {
                double shedWidth  = Double.parseDouble(ctx.formParam("shedWidth"));
                double shedLength = Double.parseDouble(ctx.formParam("shedLength"));
                requestMapper.createShed(carpId, shedLength, shedWidth);
            }

            // Persist request
            int rqId = requestMapper.createRequest(carpId, ciId);

            ctx.redirect("/request/confirmation?rqId=" + rqId);

        } catch (NumberFormatException e) {
            rerenderWithError(ctx, loggedInUser, "Ugyldige mål — tjek venligst dine indtastninger.");
        } catch (Exception e) {
            rerenderWithError(ctx, loggedInUser, "Der opstod en fejl. Prøv venligst igen.");
        }
    }

    /* ── Helpers ── */

    private int createContactInfo(io.javalin.http.Context ctx, Integer userId) throws Exception {
        String firstName  = ctx.formParam("firstName");
        String lastName   = ctx.formParam("lastName");
        String address    = ctx.formParam("address");
        String zipcodeStr = ctx.formParam("zipcode");
        String town       = ctx.formParam("town");
        String email      = ctx.formParam("email");
        String phone      = ctx.formParam("phone");

        // Find or create zip row
        Zip zip = requestMapper.findZipByZipcode(zipcodeStr);
        int zipId = zip != null
                ? zip.getId()
                : requestMapper.createZip(zipcodeStr, town);

        return requestMapper.createContactInfo(
                userId, firstName, lastName, address, phone, email, zipId);
    }

    private void rerenderWithError(io.javalin.http.Context ctx,
                                   User loggedInUser,
                                   String errorMessage) throws DatabaseException {
        List<RoofType> roofTypes = requestMapper.getAllRoofTypes();
        ContactInfo contactInfo = null;
        if (loggedInUser != null) {
            contactInfo = requestMapper.findContactInfoByUserId(loggedInUser.getId());
        }

        WebContext thymeleafCtx = ThymeleafConfig.buildWebContext(ctx);
        thymeleafCtx.setVariable("roofTypes",    roofTypes);
        thymeleafCtx.setVariable("contactInfo",  contactInfo);
        thymeleafCtx.setVariable("user",         loggedInUser);
        thymeleafCtx.setVariable("errorMessage", errorMessage);

        // Remember information if entered wrong
        thymeleafCtx.setVariable("prevFirstName", ctx.formParam("firstName"));
        thymeleafCtx.setVariable("prevLastName",  ctx.formParam("lastName"));
        thymeleafCtx.setVariable("prevAddress",   ctx.formParam("address"));
        thymeleafCtx.setVariable("prevZipcode",   ctx.formParam("zipcode"));
        thymeleafCtx.setVariable("prevTown",      ctx.formParam("town"));
        thymeleafCtx.setVariable("prevEmail",     ctx.formParam("email"));
        thymeleafCtx.setVariable("prevPhone",     ctx.formParam("phone"));
        thymeleafCtx.setVariable("prevComments",  ctx.formParam("comments"));

        String html = templateEngine.process("order", thymeleafCtx);
        ctx.html(html);
    }
}