package ru.vsu.g72.players.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CurrencyDTO implements Serializable {
    private Long id;

    private Long playerId;

    private Long resourceId;

    private String name;

    private int count;
}
