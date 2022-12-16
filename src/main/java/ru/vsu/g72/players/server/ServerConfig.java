package ru.vsu.g72.players.server;

import com.google.inject.Inject;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.vsu.g72.players.servlets.CurrencyServlet;
import ru.vsu.g72.players.servlets.ItemServlet;
import ru.vsu.g72.players.servlets.PlayerServlet;
import ru.vsu.g72.players.servlets.ProgressServlet;

public class ServerConfig {
    private final PlayerServlet playerServlet;
    private final ItemServlet itemServlet;
    private final CurrencyServlet currencyServlet;
    private final ProgressServlet progressServlet;

    @Inject
    public ServerConfig(PlayerServlet playerServlet, ItemServlet itemServlet, CurrencyServlet currencyServlet, ProgressServlet progressServlet) {
        this.playerServlet = playerServlet;
        this.itemServlet = itemServlet;
        this.currencyServlet = currencyServlet;
        this.progressServlet = progressServlet;
    }

    private Server build(){
        final Server server = new Server();
        final int port = 8080;
        final HttpConfiguration httpConfig = new HttpConfiguration();
        final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);
        final ServerConnector serverConnector = new ServerConnector(server, httpConnectionFactory);
        serverConnector.setHost("localhost");
        serverConnector.setPort(port);
        server.setConnectors(new Connector[]{serverConnector});
        return server;
    }
    public void initServer() throws Exception {
        final Server server = build();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        context.addServlet(new ServletHolder("players", playerServlet), "/players/*");
        context.addServlet(new ServletHolder("items", itemServlet), "/items/*");
        context.addServlet(new ServletHolder("progress", progressServlet), "/progress/*");
        context.addServlet(new ServletHolder("currency", currencyServlet), "/currency/*");

        server.setHandler(context);
        server.start();
    }
}
