package ru.vsu.g72.players.mapper;

import jakarta.inject.Named;
import ru.vsu.g72.players.domain.Player;
import ru.vsu.g72.players.domain.Progress;
import ru.vsu.g72.players.dto.ProgressDTO;

import java.util.List;
import java.util.stream.Collectors;

@Named
public class ProgressMapper implements EntityMapper<Progress, ProgressDTO> {

    @Override
    public Progress toEntity(ProgressDTO progressDTO) {
        return Progress
                .builder()
                .id(progressDTO.getId())
                .score(progressDTO.getScore())
                .maxScore(progressDTO.getMaxScore())
                .resourceId(progressDTO.getResourceId())
                .playerId(Player
                        .builder()
                        .id(progressDTO.getId())
                        .build())
                .build();
    }

    @Override
    public List<Progress> toEntity(List<ProgressDTO> dto) {
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public ProgressDTO toDto(Progress progress) {
        return ProgressDTO
                .builder()
                .id(progress.getId())
                .score(progress.getScore())
                .maxScore(progress.getMaxScore())
                .resourceId(progress.getResourceId())
                .playerId(progress.getPlayerId().getId())
                .build();
    }

    @Override
    public List<ProgressDTO> toDto(List<Progress> entity) {
        return entity.stream().map(this::toDto).collect(Collectors.toList());
    }
}
