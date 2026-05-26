package app.config;

import io.javalin.http.Context;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        return new WebContext(
                ctx.req(),   // HttpServletRequest
                ctx.res(),   // HttpServletResponse
                ctx.req().getServletContext()
        );
    }
}
