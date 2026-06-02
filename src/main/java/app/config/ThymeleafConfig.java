package app.config;

import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class ThymeleafConfig {

    private static TemplateEngine templateEngine;

    public static TemplateEngine templateEngine() {
        if (templateEngine == null) {
            templateEngine = buildTemplateEngine();
        }
        return templateEngine;
    }

    private static TemplateEngine buildTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();

        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false);

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(resolver);
        return engine;
    }

    public static WebContext buildWebContext(Context ctx) {
        JakartaServletWebApplication application = JakartaServletWebApplication.buildApplication(ctx.req().getServletContext());
        IWebExchange exchange = application.buildExchange(ctx.req(), ctx.res());
        return new WebContext(exchange);
    }
}