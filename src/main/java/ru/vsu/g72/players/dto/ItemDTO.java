package ru.vsu.g72.players.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ItemDTO implements Serializable {
    private Long id;

    private Long playerId;

    private Long resourceId;

    private int count;

    private int level;
}
