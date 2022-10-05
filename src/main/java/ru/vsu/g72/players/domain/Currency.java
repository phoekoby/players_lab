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

    public static CurrencyBuilder builder(){
        return new CurrencyBuilder();
    }

    public static class CurrencyBuilder{
        private final Currency currency;
        public CurrencyBuilder(){
            currency = new Currency();
        }
        public CurrencyBuilder id(Long id){
            currency.setId(id);
            return this;
        }
        public CurrencyBuilder playerId(Player player){
            currency.setPlayerId(player);
            return this;
        }
        public CurrencyBuilder resourceId(Long resourceId){
            currency.setResourceId(resourceId);
            return this;
        }
        public CurrencyBuilder name(String name){
            currency.setName(name);
            return this;
        }
        public CurrencyBuilder count(int count){
            currency.setCount(count);
            return this;
        }
        public Currency build(){
            return currency;
        }
    }
}
