package ru.vsu.g72.players.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ru.vsu.g72.players.domain.Currency;
import ru.vsu.g72.players.dto.CurrencyDTO;
import ru.vsu.g72.players.mapper.CurrencyMapper;
import ru.vsu.g72.players.repository.CurrencyRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
@Singleton
public class CurrencyService {
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;
    private final PlayerService playerService;

    @Inject
    public CurrencyService(CurrencyMapper currencyMapper, CurrencyRepository currencyRepository, PlayerService playerService) {
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
        this.playerService = playerService;
    }

    public CurrencyDTO createNewCurrency(CurrencyDTO currencyDTO){
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        CurrencyDTO result = currencyMapper.toDto(currency);
        playerService.getCache().get(currency
                .getPlayerId()
                .getId())
                .getCurrencies()
                .put(currency.getId(),result);
        return result;
    }
    public CurrencyDTO update(CurrencyDTO currencyDTO){
        if(currencyDTO.getId() == null || currencyDTO.getId() <= 0 ||
                !playerService.getCache().get(currencyDTO.getPlayerId()).getCurrencies().containsKey(currencyDTO.getId())){
            System.err.println("Not valid id");
            throw new RuntimeException("Not valid id " + currencyDTO.getId());
        }
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency = currencyRepository.save(currency);
        CurrencyDTO result = currencyMapper.toDto(currency);
        playerService.getCache().get(currency
                        .getPlayerId()
                        .getId())
                .getCurrencies()
                .put(currency.getId(),result);
        return result;
    }

    public List<CurrencyDTO> getAll(Long playerId,int count){
        return playerService.getCache()
                .get(playerId)
                .getCurrencies()
                .values().
                stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<CurrencyDTO> getCurrencyById(Long id, Long playerId){
        Map<Long, CurrencyDTO> currencyCache = playerService
                .getCache()
                .get(playerId)
                .getCurrencies();
        if(!currencyCache.containsKey(id)){
            System.err.println("NOT Found Player With id " + id);
        }
        return Optional.ofNullable(currencyCache.get(id));
    }

    public void delete(Long id, Long playerId){
        Map<Long, CurrencyDTO> currencyCache = playerService
                .getCache()
                .get(playerId)
                .getCurrencies();
        if(!currencyCache.containsKey(id)){
            System.err.println("NOT Found Player With id " + id);
        }
        currencyCache.remove(id);
        currencyRepository.delete(id);
    }
}
