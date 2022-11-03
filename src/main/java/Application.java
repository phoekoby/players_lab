import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import ru.vsu.g72.players.service.ConsoleReader;
import ru.vsu.g72.players.service.ConsoleReaderService;
import ru.vsu.g72.players.service.PlayerService;

public class Application {

    public static void main(String[] args) {
        Weld weld = new Weld();
        WeldContainer weldContainer = weld.initialize();
        ConsoleReader consoleReader = weldContainer.select(ConsoleReaderService.class).get();
        consoleReader.listen();
        weldContainer.close();
    }

}
