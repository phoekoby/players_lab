package ru.vsu.g72.players.mapper;

import jakarta.inject.Named;
import ru.vsu.g72.players.domain.Currency;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.dto.CurrencyDTO;

import java.util.List;
import java.util.stream.Collectors;

@Named
public class CurrencyMapper implements EntityMapper<Currency, CurrencyDTO> {
    @Override
    public Currency toEntity(CurrencyDTO currencyDTO) {
        return Currency
                .builder()
                .playerId(Player
                        .builder()
                        .id(currencyDTO.getPlayerId())
                        .build())
                .count(currencyDTO.getCount())
                .name(currencyDTO.getName())
                .resourceId(currencyDTO.getResourceId())
                .id(currencyDTO.getId())
                .build();
    }

    @Override
    public List<Currency> toEntity(List<CurrencyDTO> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public CurrencyDTO toDto(Currency currency) {
        return CurrencyDTO
                .builder()
                .id(currency.getId())
                .name(currency.getName())
                .playerId(currency.getPlayerId().getId())
                .count(currency.getCount())
                .resourceId(currency.getResourceId())
                .build();
    }

    @Override
    public List<CurrencyDTO> toDto(List<Currency> entity) {
        return entity.stream().map(this::toDto).collect(Collectors.toList());
    }
}
