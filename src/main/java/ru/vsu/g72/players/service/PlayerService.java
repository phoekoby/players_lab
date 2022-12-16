package ru.vsu.g72.players.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.dto.PlayerDTO;
import ru.vsu.g72.players.mapper.PlayerMapper;
import ru.vsu.g72.players.repository.PlayerRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class PlayerService {
    private final ObjectMapper objectMapper = new ObjectMapper();


    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    private Map<Long, PlayerDTO> cache;

    @Inject
    public PlayerService(PlayerRepository playerRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        upload();
    }


    public void upload() {
        try {
//            List<Player> players = parseJsonFile();
//            players = playerRepository.saveAll(players);
//            log.info("Players was successful uploaded {}", players);
            log.info("Wait for reading all players from Database");
            cache = playerMapper
                    .toDto(playerRepository.getAll())
                    .stream()
                    .collect(Collectors.toMap(PlayerDTO::getId, Function.identity()));
            log.info("Players was successful load to cache map");
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }


    public List<Player> parseJsonFile() throws IOException {
        log.info("Parse Json File");
        String jsonFileName = "players.json";
        InputStream jsonFile = Objects.requireNonNull(PlayerService.class.getClassLoader().getResourceAsStream(jsonFileName));
        return objectMapper.readValue(jsonFile, objectMapper.getTypeFactory().constructCollectionType(List.class, Player.class));
    }

    public List<PlayerDTO> getAll(int count){
        return cache.values().
                stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<PlayerDTO> getPlayerById(Long id){
        if(!cache.containsKey(id)){
            System.err.println("NOT Found Player With id " + id);
        }
        return Optional.ofNullable(cache.get(id));
    }

    public void delete(Long id){
        if(!cache.containsKey(id)){
            System.err.println("NOT Found Player With id " + id);
//            throw new RuntimeException("NOT Found Player With id " + id);
        }
        cache.remove(id);
        playerRepository.delete(id);
    }

    public PlayerDTO save(PlayerDTO playerDTO){
        if(playerDTO.getId() == null || playerDTO.getId() <= 0 || cache.containsKey(playerDTO.getId())){
            System.err.println("Not valid id " + playerDTO.getId());
//            throw new RuntimeException("Not valid id " + playerDTO.getId());
        }
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        PlayerDTO result =  playerMapper.toDto(player);
        cache.put(result.getId(), result);
        return result;
    }

    public PlayerDTO update(PlayerDTO playerDTO){
        if(playerDTO.getId() == null || playerDTO.getId() <= 0 || !cache.containsKey(playerDTO.getId())){
            System.err.println("Not valid id " + playerDTO.getId());
//            throw new RuntimeException("Not valid id " + playerDTO.getId());
        }
        Player player = playerMapper.toEntity(playerDTO);
        player = playerRepository.save(player);
        PlayerDTO result =  playerMapper.toDto(player);
        cache.put(result.getId(), result);
        return result;
    }

    public Map<Long, PlayerDTO> getCache() {
        return cache;
    }
}
