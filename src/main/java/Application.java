import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.vsu.g72.players.config.PlayersModule;
import ru.vsu.g72.players.server.ServerConfig;
import ru.vsu.g72.players.service.ConsoleReader;
import ru.vsu.g72.players.service.ConsoleReaderService;

public class Application {

    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new PlayersModule());
        if(args.length != 0 &&args[0].equals("servlet")){
            ServerConfig serverConfig = injector.getInstance(ServerConfig.class);
            serverConfig.initServer();
        }else {
            ConsoleReader consoleReader = injector.getInstance(ConsoleReaderService.class);
            consoleReader.listen();
        }
    }

}
