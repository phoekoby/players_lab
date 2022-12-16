package ru.vsu.g72.players.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.vsu.g72.players.domain.Item;
import ru.vsu.g72.players.dto.ItemDTO;
import ru.vsu.g72.players.mapper.ItemMapper;
import ru.vsu.g72.players.repository.ItemRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ItemService {
    private final ItemMapper itemMapper;
    private final ItemRepository itemRepository;
    private final PlayerService playerService;

    @Inject
    public ItemService(ItemMapper itemMapper, ItemRepository itemRepository, PlayerService playerService) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
        this.playerService = playerService;
    }

    public ItemDTO save(ItemDTO itemDTO) {
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        ItemDTO result = itemMapper.toDto(item);
        playerService.getCache().get(item
                        .getPlayerId()
                        .getId())
                .getItems()
                .put(item.getId(), result);
        return result;
    }

    public ItemDTO update(ItemDTO itemDTO) {
        if (itemDTO.getId() == null || itemDTO.getId() <= 0 ||
                !playerService.getCache().get(itemDTO.getPlayerId()).getItems().containsKey(itemDTO.getId())) {
            System.err.println("Not valid id");
            throw new RuntimeException("Not valid id " + itemDTO.getId());
        }
        Item item = itemMapper.toEntity(itemDTO);
        item = itemRepository.save(item);
        ItemDTO result = itemMapper.toDto(item);
        playerService.getCache().get(item
                        .getPlayerId()
                        .getId())
                .getItems()
                .put(item.getId(), result);
        return result;
    }

    public List<ItemDTO> getAll(Long playerId, int count) {
        return playerService.getCache()
                .get(playerId)
                .getItems()
                .values().
                stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<ItemDTO> getItemById(Long id, Long playerId) {
        Map<Long, ItemDTO> itemCache = playerService
                .getCache()
                .get(playerId)
                .getItems();
        if (!itemCache.containsKey(id)) {
            System.err.println("NOT Found Player With id " + id);
        }
        return Optional.ofNullable(itemCache.get(id));
    }

    public void delete(Long id, Long playerId) {
        Map<Long, ItemDTO> itemCache = playerService
                .getCache()
                .get(playerId)
                .getItems();
        if (!itemCache.containsKey(id)) {
            System.err.println("NOT Found Player With id " + id);
        }
        itemCache.remove(id);
        itemRepository.delete(id);
    }
}
