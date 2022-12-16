package ru.vsu.g72.players.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.vsu.g72.players.domain.Progress;
import ru.vsu.g72.players.dto.ProgressDTO;
import ru.vsu.g72.players.mapper.ProgressMapper;
import ru.vsu.g72.players.repository.ProgressRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ProgressService {
    private final ProgressMapper progressMapper;
    private final ProgressRepository progressRepository;
    private final PlayerService playerService;

    @Inject
    public ProgressService(ProgressMapper progressMapper, ProgressRepository progressRepository, PlayerService playerService) {
        this.progressMapper = progressMapper;
        this.progressRepository = progressRepository;
        this.playerService = playerService;
    }

    public ProgressDTO save(ProgressDTO progressDTO) {
        Progress progress = progressMapper.toEntity(progressDTO);
        progress = progressRepository.save(progress);
        ProgressDTO result = progressMapper.toDto(progress);
        playerService.getCache().get(progress
                        .getPlayerId()
                        .getId())
                .getProgresses()
                .put(progress.getId(), result);
        return result;
    }

    public ProgressDTO update(ProgressDTO progressDTO) {
        if (progressDTO.getId() == null || progressDTO.getId() <= 0 ||
                !playerService.getCache().get(progressDTO.getPlayerId()).getProgresses().containsKey(progressDTO.getId())) {
            System.err.println("Not valid id");
            throw new RuntimeException("Not valid id " + progressDTO.getId());
        }
        Progress progress = progressMapper.toEntity(progressDTO);
        progress = progressRepository.save(progress);
        ProgressDTO result = progressMapper.toDto(progress);
        playerService.getCache().get(progress
                        .getPlayerId()
                        .getId())
                .getProgresses()
                .put(progress.getId(), result);
        return result;
    }

    public List<ProgressDTO> getAll(Long playerId, int count) {
        return playerService.getCache()
                .get(playerId)
                .getProgresses()
                .values().
                stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<ProgressDTO> getProgressById(Long id, Long playerId) {
        Map<Long, ProgressDTO> progressCache = playerService
                .getCache()
                .get(playerId)
                .getProgresses();
        if (!progressCache.containsKey(id)) {
            System.err.println("NOT Found Player With id " + id);
        }
        return Optional.ofNullable(progressCache.get(id));
    }

    public void delete(Long id, Long playerId) {
        Map<Long, ProgressDTO> progressCache = playerService
                .getCache()
                .get(playerId)
                .getProgresses();
        if (!progressCache.containsKey(id)) {
            System.err.println("NOT Found Player With id " + id);
        }
        progressCache.remove(id);
        progressRepository.delete(id);
    }
}
