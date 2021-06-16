package it.polimi.tiw.util;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import java.sql.Connection;

public class Initializer {

    private Initializer() {}

    public static Connection connectionInit(ServletContext context) {
        Connection connection = null;
        try {
            connection = ConnectionHandler.getConnection(context);
        } catch (IllegalAccessException e) {
            System.err.println(e.getMessage());
        }
        return connection;
    }

    public static TemplateEngine templateEngineInit(ServletContext servletContext) {
        ServletContext context = servletContext;
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(context);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        TemplateEngine newTemplateEngine = new TemplateEngine();
        newTemplateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        return newTemplateEngine;
    }
}
