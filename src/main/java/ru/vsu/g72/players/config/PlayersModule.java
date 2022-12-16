package ru.vsu.g72.players.config;

import com.google.inject.AbstractModule;
import ru.vsu.g72.players.mapper.CurrencyMapper;
import ru.vsu.g72.players.mapper.ItemMapper;
import ru.vsu.g72.players.mapper.PlayerMapper;
import ru.vsu.g72.players.mapper.ProgressMapper;
import ru.vsu.g72.players.repository.CurrencyRepository;
import ru.vsu.g72.players.repository.ItemRepository;
import ru.vsu.g72.players.repository.PlayerRepository;
import ru.vsu.g72.players.repository.ProgressRepository;
import ru.vsu.g72.players.repository.impl.CurrencyRepositoryImpl;
import ru.vsu.g72.players.repository.impl.ItemRepositoryImpl;
import ru.vsu.g72.players.repository.impl.PlayerRepositoryImpl;
import ru.vsu.g72.players.repository.impl.ProgressRepositoryImpl;
import ru.vsu.g72.players.server.ServerConfig;
import ru.vsu.g72.players.service.*;
import ru.vsu.g72.players.servlets.CurrencyServlet;
import ru.vsu.g72.players.servlets.ItemServlet;
import ru.vsu.g72.players.servlets.PlayerServlet;
import ru.vsu.g72.players.servlets.ProgressServlet;

public class PlayersModule extends AbstractModule {
    @Override
    protected void configure() {
       bind(CurrencyMapper.class).asEagerSingleton();
       bind(ItemMapper.class).asEagerSingleton();
       bind(PlayerMapper.class).asEagerSingleton();
       bind(ProgressMapper.class).asEagerSingleton();

       bind(CurrencyRepository.class).to(CurrencyRepositoryImpl.class).asEagerSingleton();
       bind(ItemRepository.class).to(ItemRepositoryImpl.class).asEagerSingleton();
       bind(PlayerRepository.class).to(PlayerRepositoryImpl.class).asEagerSingleton();
       bind(ProgressRepository.class).to(ProgressRepositoryImpl.class).asEagerSingleton();

       bind(ServerConfig.class).asEagerSingleton();

       bind(ConsoleReader.class).to(ConsoleReaderService.class);
       bind(PlayerService.class);
       bind(ItemService.class);
       bind(CurrencyService.class);
       bind(ProgressService.class);
       bind(PlayerServlet.class);
       bind(ItemServlet.class);
       bind(ProgressServlet.class);
       bind(CurrencyServlet.class);

    }
}
