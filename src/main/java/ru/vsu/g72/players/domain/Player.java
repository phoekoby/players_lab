package ru.vsu.g72.players.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.List;

@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "playerId")
public class Player {

    @JsonProperty("playerId")
    private Long id;
//    @NotNull
    private String nickname;

    private List<Progress> progresses;

    private List<Currency> currencies;

    private List<Item> items;
}
