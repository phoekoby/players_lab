package ru.vsu.g72.players.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.io.Serializable;

@Data
@Builder
@Jacksonized
public class ProgressDTO implements Serializable {
    private Long id;

    private Long playerId;

    private Long resourceId;

    private int score;

    private int maxScore;
}
