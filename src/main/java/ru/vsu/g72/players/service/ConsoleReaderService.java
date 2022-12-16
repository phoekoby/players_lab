package ru.vsu.g72.players.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import lombok.SneakyThrows;
import ru.vsu.g72.players.dto.CurrencyDTO;
import ru.vsu.g72.players.dto.ItemDTO;
import ru.vsu.g72.players.dto.PlayerDTO;
import ru.vsu.g72.players.dto.ProgressDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleReaderService implements ConsoleReader {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String SAVE_ACTION = "--save";

    private final String UPDATE_ACTION = "--update";

    private final String READ_ACTION = "--read";

    private final String DELETE_ACTION = "--delete";

    private final PlayerService playerService;

    @Inject
    public ConsoleReaderService(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    public void listen() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting for new lines. Key in Ctrl+D to exit.");
            while (true) {
                String action = scanner.next();
                switch (action) {
                    case READ_ACTION:
                        read(scanner);
                        break;
                    case SAVE_ACTION:
                        save(scanner);
                        break;
                    case DELETE_ACTION:
                        delete(scanner);
                        break;
                    case UPDATE_ACTION:
                        update(scanner);
                        break;
                    default:
                        help();
                }
            }
        } catch (IllegalStateException | NoSuchElementException e) {
        }
    }

    @SneakyThrows
    private void printPlayerDTO(PlayerDTO playerDTO) {
        System.out.println(objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(playerDTO));
    }

    @SneakyThrows
    public void update(Scanner scanner) {
        final String nickname = "--nickname";
        final String progresses = "--progresses";
        final String currencies = "--currencies";
        final String items = "--items";
        final String exit = "--exit";
        System.out.print("Enter id: ");
        Long id = scanner.nextLong();
        PlayerDTO playerDTO = playerService.getPlayerById(id).orElseThrow();
        System.out.print("What do you want update: --nickname, --progresses, --currencies, --items");

        String action = scanner.next();
        while (!action.equals(exit)) {
            switch (action) {
                case nickname -> {
                    System.out.print("Enter New NickName: ");
                    String nickNameValue = scanner.next();
                    playerDTO.setNickname(nickNameValue);
                }
                case progresses -> {
                    String progressesValues = objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(playerDTO.getProgresses().values());
                    System.out.println("All progresses of this player: " + progressesValues);
                    updateProgresses(playerDTO.getProgresses(), scanner, playerDTO.getId());
                }
                case currencies -> {
                    String currencyValues = objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(playerDTO.getCurrencies().values());
                    System.out.println("All currencies of this player: " + currencyValues);
                    updateCurrencies(playerDTO.getCurrencies(), scanner, playerDTO.getId());
                }
                case items -> {
                    String itemValues = objectMapper
                            .writerWithDefaultPrettyPrinter()
                            .writeValueAsString(playerDTO.getItems().values());
                    System.out.println("All items of this player: " + itemValues);
                    updateItems(playerDTO.getItems(), scanner, playerDTO.getId());
                }
            }
            action = scanner.next();
        }
        playerDTO = playerService.update(playerDTO);
        printPlayerDTO(playerDTO);
    }

    public void updateProgresses(Map<Long, ProgressDTO> progresses, Scanner scanner, Long playerId) {
        final String addAction = "--add";
        final String updateAction = "--update";
        final String deleteAction = "--delete";
        final String exitAction = "--exit";
        System.out.println("Print --add or --update  or --delete, --exit");
        String action = scanner.next();
        while (!action.equals(exitAction)) {
            switch (action) {
                case addAction -> {
                    progresses.putAll(readProgresses(scanner, playerId));
                    break;
                }
                case updateAction -> {
                    System.out.println("How many progresses do you want to update: ");
                    int n = scanner.nextInt();
                    for (int i = 0; i < n; i++) {
                        System.out.println("Get me Id: ");
                        Long progressId = scanner.nextLong();
                        updateProgress(progresses.get(progressId), scanner);
                    }
                }
                case deleteAction -> {
                    System.out.println("How many n do you want to delete: ");
                    int n = scanner.nextInt();
                    if (n < progresses.size()) {
                        throw new RuntimeException();
                    }
                    for (int i = 0; i < n; i++) {
                        Long id = scanner.nextLong();
                        progresses.remove(id);
                    }
                }
            }
        }
    }

    public void updateCurrencies(Map<Long, CurrencyDTO> currencies, Scanner scanner, Long playerId) {
        final String addAction = "--add";
        final String updateAction = "--update";
        final String deleteAction = "--delete";
        final String exitAction = "--exit";
        System.out.println("Print --add or --update  or --delete, --exit");
        String action = scanner.next();
        while (!action.equals(exitAction)) {
            switch (action) {
                case addAction -> {
                    currencies.putAll(readCurrencies(scanner, playerId));
                    break;
                }
                case updateAction -> {
                    System.out.println("How many progresses do you want to update: ");
                    int n = scanner.nextInt();
                    for (int i = 0; i < n; i++) {
                        System.out.println("Get me Id: ");
                        Long currencyId = scanner.nextLong();
                        updateCurrency(currencies.get(currencyId), scanner);
                    }
                }
                case deleteAction -> {
                    System.out.println("How many n do you want to delete: ");
                    int n = scanner.nextInt();
                    if (n < currencies.size()) {
                        throw new RuntimeException();
                    }
                    for (int i = 0; i < n; i++) {
                        Long id = scanner.nextLong();
                        currencies.remove(id);
                    }
                }
            }
        }
    }

    public void updateItems(Map<Long, ItemDTO> items, Scanner scanner, Long playerId) {
        final String addAction = "--add";
        final String updateAction = "--update";
        final String deleteAction = "--delete";
        final String exitAction = "--exit";
        System.out.println("Print --add or --update  or --delete, --exit");
        String action = scanner.next();
        while (!action.equals(exitAction)) {
            switch (action) {
                case addAction -> {
                    items.putAll(readItems(scanner, playerId));
                    break;
                }
                case updateAction -> {
                    System.out.println("How many progresses do you want to update: ");
                    int n = scanner.nextInt();
                    for (int i = 0; i < n; i++) {
                        System.out.println("Get me Id: ");
                        Long itemId = scanner.nextLong();
                        updateItem(items.get(itemId), scanner);
                    }
                }
                case deleteAction -> {
                    System.out.println("How many n do you want to delete: ");
                    int n = scanner.nextInt();
                    if (n < items.size()) {
                        throw new RuntimeException();
                    }
                    for (int i = 0; i < n; i++) {
                        Long id = scanner.nextLong();
                        items.remove(id);
                    }
                }
            }
            action = scanner.next();
        }
    }

    public void read(Scanner scanner) {
        System.out.print("Enter Id: ");
        Long id = scanner.nextLong();
        playerService
                .getPlayerById(id)
                .ifPresent(this::printPlayerDTO);
        System.out.println();
    }

    public void delete(Scanner scanner) {
        System.out.print("Enter id: ");
        Long id = scanner.nextLong();
        playerService.delete(id);
        System.out.println();
    }

    public void save(Scanner scanner) {
        System.out.print("Enter id: ");
        Long id = scanner.nextLong();
        System.out.print("\nEnter nickname: ");
        String nickname = scanner.next();
        PlayerDTO playerDTO =
                PlayerDTO
                        .builder()
                        .id(id)
                        .nickname(nickname)
                        .progresses(readProgresses(scanner, id))
                        .currencies(readCurrencies(scanner, id))
                        .items(readItems(scanner, id))
                        .build();
        playerDTO = playerService.save(playerDTO);
        printPlayerDTO(playerDTO);
    }


    private Map<Long, ItemDTO> readItems(Scanner scanner, Long playerId) {
        Map<Long, ItemDTO> result = new HashMap<>();
        System.out.print("How many items do you want to add: ");
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("\n-------------------");
            System.out.print("id: ");
            Long id = scanner.nextLong();
            System.out.print("resourceId: ");
            Long resourceId = scanner.nextLong();
            System.out.print("count: ");
            int count = scanner.nextInt();
            System.out.print("level: ");
            int level = scanner.nextInt();

            ItemDTO itemDTO = ItemDTO
                    .builder()
                    .id(id)
                    .playerId(playerId)
                    .resourceId(resourceId)
                    .level(level)
                    .count(count)
                    .build();
            result.put(id, itemDTO);
        }
        return result;
    }

    private Map<Long, CurrencyDTO> readCurrencies(Scanner scanner, Long playerId) {
        Map<Long, CurrencyDTO> result = new HashMap<>();
        System.out.print("How many currencies do you want to add: ");
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("n-------------------");
            System.out.print("id: ");
            Long id = scanner.nextLong();
            System.out.print("resourceId: ");
            Long resourceId = scanner.nextLong();
            System.out.print("name: ");
            String name = scanner.next();
            System.out.print("count: ");
            int count = scanner.nextInt();

            CurrencyDTO currencyDTO = CurrencyDTO
                    .builder()
                    .id(id)
                    .playerId(playerId)
                    .resourceId(resourceId)
                    .name(name)
                    .count(count)
                    .build();
            result.put(id, currencyDTO);
        }
        return result;
    }

    public void updateProgress(ProgressDTO progressDTO, Scanner scanner) {
        System.out.println("Which fields do you want to update: --resource --score --maxScore, --exit");
        int amount = 0;
        String action = scanner.next();
        while (!action.equals("--exit") && amount < 3) {
            switch (action) {
                case "--resource":
                    progressDTO.setResourceId(scanner.nextLong());
                case "--score":
                    progressDTO.setScore(scanner.nextInt());
                case "--maxScore":
                    progressDTO.setScore(scanner.nextInt());
            }
            amount++;
            action = scanner.next();

        }
    }
    public void updateCurrency(CurrencyDTO currencyDTO, Scanner scanner) {
        System.out.println("Which fields do you want to update: --resource --name --count, --exit");
        int amount = 0;
        String action = scanner.next();
        while (!action.equals("--exit") && amount < 3) {
            switch (action) {
                case "--resource":
                    currencyDTO.setResourceId(scanner.nextLong());
                case "--name":
                    currencyDTO.setName(scanner.next());
                case "--count":
                    currencyDTO.setCount(scanner.nextInt());
            }
            amount++;
            action = scanner.next();

        }
    }

    public void updateItem(ItemDTO item, Scanner scanner) {
        System.out.println("Which fields do you want to update: --resource --level --count, --exit");
        int amount = 0;
        String action = scanner.next();
        while (!action.equals("--exit") && amount < 3) {
            switch (action) {
                case "--resource":
                    item.setResourceId(scanner.nextLong());
                case "--level":
                    item.setLevel(scanner.nextInt());
                case "--count":
                    item.setCount(scanner.nextInt());
            }
            amount++;
            action = scanner.next();

        }
    }

    private Map<Long, ProgressDTO> readProgresses(Scanner scanner, Long playerId) {
        Map<Long, ProgressDTO> result = new HashMap<>();
        System.out.print("How many progresses do you want to add: ");
        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            System.out.println("-------------------");
            System.out.print("id: ");
            Long id = scanner.nextLong();
            System.out.print("resourceId: ");
            Long resourceId = scanner.nextLong();
            System.out.print("score: ");
            int score = scanner.nextInt();
            System.out.print("maxScore: ");
            int maxScore = scanner.nextInt();

            ProgressDTO progressDTO = ProgressDTO
                    .builder()
                    .id(id)
                    .playerId(playerId)
                    .resourceId(resourceId)
                    .score(score)
                    .maxScore(maxScore)
                    .build();
            result.put(id, progressDTO);
        }
        return result;
    }

    public void help() {
        System.out.println("print '--save' for save");
        System.out.println("print '--update' for update");
        System.out.println("print '--delete' for delete");
        System.out.println("print '--read' for read");
    }


    //    public void read(Scanner scanner) {
//        System.out.println("Print '--all' to read ALL or '--id' for read one by Id");
//        final String readAll = "--all";
//        final String readById = "--id";
//        final String nextAction = scanner.next();
//        switch (nextAction) {
//            case readAll -> {
//                System.out.print("How many: ");
//                int amount = scanner.nextInt();
//                playerService.getAll(amount).forEach(this::printPlayerDTO);
//            }
//            case readById -> {
//                System.out.print("Enter Id: ");
//                Long id = scanner.nextLong();
//                playerService.getPlayerById(id).ifPresent(this::printPlayerDTO);
//                System.out.println();
//            }
//        }
//    }
}
