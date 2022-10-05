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

    private String nickname;

    private List<Progress> progresses;

    private List<Currency> currencies;

    private List<Item> items;

    public static Player.PlayerBuilder builder(){
        return new Player.PlayerBuilder();
    }

    public static class PlayerBuilder{
        private final Player player;
        public PlayerBuilder(){
            player = new Player();
        }
        public Player.PlayerBuilder id(Long id){
            player.setId(id);
            return this;
        }
        public Player.PlayerBuilder nickname(String nickname){
            player.setNickname(nickname);
            return this;
        }
        public Player.PlayerBuilder progresses(List<Progress> progresses){
            player.setProgresses(progresses);
            return this;
        }
        public Player.PlayerBuilder currencies(List<Currency> currencies){
            player.setCurrencies(currencies);
            return this;
        }
        public Player.PlayerBuilder items(List<Item> items){
            player.setItems(items);
            return this;
        }
        public Player build(){
            return player;
        }
    }

    public void setProgresses(List<Progress> progresses) {
        this.progresses = List.copyOf(progresses);
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = List.copyOf(currencies);
    }

    public void setItems(List<Item> items) {
        this.items = List.copyOf(items);
    }
}
