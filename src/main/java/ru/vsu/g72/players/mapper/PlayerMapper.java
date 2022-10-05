package ru.vsu.g72.players.mapper;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.dto.CurrencyDTO;
import ru.vsu.g72.players.dto.ItemDTO;
import ru.vsu.g72.players.dto.PlayerDTO;
import ru.vsu.g72.players.dto.ProgressDTO;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;


@Named
public class PlayerMapper implements EntityMapper<Player, PlayerDTO> {

    private final ItemMapper itemMapper;

    private final ProgressMapper progressMapper;

    private final CurrencyMapper currencyMapper;

    @Inject
    public PlayerMapper(ItemMapper itemMapper, ProgressMapper progressMapper, CurrencyMapper currencyMapper) {
        this.itemMapper = itemMapper;
        this.progressMapper = progressMapper;
        this.currencyMapper = currencyMapper;
    }

    @Override
    public Player toEntity(PlayerDTO playerDTO) {

        return Player
                .builder()
                .id(playerDTO.getId())
                .nickname(playerDTO.getNickname())
                .currencies(playerDTO
                        .getCurrencies()
                        .values()
                        .stream()
                        .map(currencyMapper::toEntity)
                        .collect(Collectors.toList()))
                .progresses(playerDTO
                        .getProgresses()
                        .values()
                        .stream()
                        .map(progressMapper::toEntity)
                        .collect(Collectors.toList())
                )
                .items(playerDTO
                        .getItems()
                        .values()
                        .stream()
                        .map(itemMapper::toEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<Player> toEntity(List<PlayerDTO> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public PlayerDTO toDto(Player player) {
        return PlayerDTO
                .builder()
                .id(player.getId())
                .nickname(player.getNickname())
                .currencies(player
                        .getCurrencies()
                        .stream()
                        .map(currencyMapper::toDto)
                        .collect(Collectors.toMap(CurrencyDTO::getId, Function.identity())))
                .progresses(player
                        .getProgresses()
                        .stream()
                        .map(progressMapper::toDto)
                        .collect(Collectors.toMap(ProgressDTO::getId, Function.identity())))
                .items(player
                        .getItems()
                        .stream()
                        .map(itemMapper::toDto)
                        .collect(Collectors.toMap(ItemDTO::getId, Function.identity())))
                .build();
    }

    @Override
    public List<PlayerDTO> toDto(List<Player> entity) {
        return entity.stream().map(this::toDto).collect(Collectors.toList());
    }
}
