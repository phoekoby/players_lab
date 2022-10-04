package ru.vsu.g72.players.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.ToString;

@Data
public class Item {
    private Long id;
    @ToString.Exclude
    @JsonIdentityReference(alwaysAsId = true)
    private Player playerId;

    private Long resourceId;

    private int count;

    private int level;
}
