package ru.vsu.g72.players.mapper;

import java.util.List;

public interface EntityMapper<ENTITY, DTO> {
    ENTITY toEntity(DTO dto);

    List<ENTITY> toEntity(List<DTO> dto);

    DTO toDto(ENTITY entity);

    List<DTO> toDto(List<ENTITY> entity);
}
