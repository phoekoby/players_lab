package ru.vsu.g72.players.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.ToString;

@Data
public class Currency {
    private Long id;

    @ToString.Exclude
    @JsonIdentityReference(alwaysAsId = true)
    private Player playerId;

    private Long resourceId;

    private String name;

    private int count;
}
