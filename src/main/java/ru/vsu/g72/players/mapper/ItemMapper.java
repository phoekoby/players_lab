package ru.vsu.g72.players.mapper;

import jakarta.inject.Named;
import ru.vsu.g72.players.domain.Item;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.dto.ItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Named
public class ItemMapper implements EntityMapper<Item, ItemDTO> {
    @Override
    public Item toEntity(ItemDTO itemDTO) {
        return Item
                .builder()
                .id(itemDTO.getId())
                .count(itemDTO.getCount())
                .level(itemDTO.getLevel())
                .resourceId(itemDTO.getResourceId())
                .playerId(Player
                        .builder()
                        .id(itemDTO.getPlayerId())
                        .build())
                .build();
    }

    @Override
    public List<Item> toEntity(List<ItemDTO> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public ItemDTO toDto(Item item) {
        return ItemDTO
                .builder()
                .id(item.getId())
                .count(item.getCount())
                .level(item.getLevel())
                .resourceId(item.getResourceId())
                .playerId(item.getPlayerId().getId())
                .build();
    }

    @Override
    public List<ItemDTO> toDto(List<Item> entity) {
        return entity.stream().map(this::toDto).collect(Collectors.toList());
    }
}
