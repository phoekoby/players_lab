import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import ru.vsu.g72.players.service.PlayerService;

public class Application {

    public static void main(String[] args) {
        Weld weld = new Weld();
        WeldContainer weldContainer = weld.initialize();
        PlayerService playerService = weldContainer.select(PlayerService.class).get();
        weldContainer.close();
    }

}
