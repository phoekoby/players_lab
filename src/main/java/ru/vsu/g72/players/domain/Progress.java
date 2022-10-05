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

    public static Progress.ProgressBuilder builder(){
        return new Progress.ProgressBuilder();
    }

    public static class ProgressBuilder{
        private final Progress progress;
        public ProgressBuilder(){
            progress = new Progress();
        }
        public Progress.ProgressBuilder id(Long id){
            progress.setId(id);
            return this;
        }
        public Progress.ProgressBuilder playerId(Player player){
            progress.setPlayerId(player);
            return this;
        }
        public Progress.ProgressBuilder resourceId(Long resourceId){
            progress.setResourceId(resourceId);
            return this;
        }
        public Progress.ProgressBuilder score(int score){
            progress.setScore(score);
            return this;
        }
        public Progress.ProgressBuilder maxScore(int maxScore){
            progress.setMaxScore(maxScore);
            return this;
        }
        public Progress build(){
            return progress;
        }
    }
}
