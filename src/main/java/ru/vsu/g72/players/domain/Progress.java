package ru.vsu.g72.players.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.ToString;

@Data
public class Progress {
    private Long id;

    @ToString.Exclude
    @JsonIdentityReference(alwaysAsId = true)
    private Player playerId;

    private Long resourceId;

    private int score;

    private int maxScore;
}
