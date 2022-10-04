package ru.vsu.g72.players.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.repository.PlayerRepository;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@ApplicationScoped
public class PlayerService {
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final PlayerRepository playerRepository;

    @Inject
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    private Map<Long, Player> cache;


    @PostConstruct
    public void upload() {
        try {
            List<Player> players = parseJsonFile();
            players = playerRepository.saveAll(players);
            log.info("Players was successful uploaded {}", players);
            cache = playerRepository.getAll()
                    .stream()
                    .collect(Collectors.toMap(Player::getId, Function.identity()));
            log.info("Players was successful load to cache map {}", cache);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


    public List<Player> parseJsonFile() throws IOException {
        log.info("Parse Json File");
        String jsonFileName = "players.json";
        File jsonFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(jsonFileName)).getFile());
        return objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Player.class));
    }

}
