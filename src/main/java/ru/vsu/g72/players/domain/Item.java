package ru.vsu.g72.players.domain;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.Data;
import lombok.ToString;

@Data
//@Builder
public class Item {
    private Long id;
    @ToString.Exclude
    @JsonIdentityReference(alwaysAsId = true)
    private Player playerId;

    private Long resourceId;

    private int count;

    private int level;

    public static Item.ItemBuilder builder(){
        return new Item.ItemBuilder();
    }

    public static class ItemBuilder{
        private final Item item;
        public ItemBuilder(){
            item = new Item();
        }
        public Item.ItemBuilder id(Long id){
            item.setId(id);
            return this;
        }
        public Item.ItemBuilder playerId(Player player){
            item.setPlayerId(player);
            return this;
        }
        public Item.ItemBuilder resourceId(Long resourceId){
            item.setResourceId(resourceId);
            return this;
        }
        public Item.ItemBuilder level(int level){
            item.setLevel(level);
            return this;
        }
        public Item.ItemBuilder count(int count){
            item.setCount(count);
            return this;
        }
        public Item build(){
            return item;
        }
    }
}
