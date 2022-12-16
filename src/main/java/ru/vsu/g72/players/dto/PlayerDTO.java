package ru.vsu.g72.players.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@Jacksonized
public class PlayerDTO implements Serializable {
    private Long id;

    private String nickname;

    private Map<Long, ProgressDTO> progresses;

    private Map<Long,CurrencyDTO> currencies;

    private Map<Long, ItemDTO> items;
}
