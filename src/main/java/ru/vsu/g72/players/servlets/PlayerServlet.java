package ru.vsu.g72.players.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import ru.vsu.g72.players.dto.PlayerDTO;
import ru.vsu.g72.players.service.PlayerService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class PlayerServlet extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    private final PlayerService playerService;


    @Inject
    public PlayerServlet(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long playerId = Long.parseLong(request.getParameter("id"));
        Optional<PlayerDTO> playerDTO = playerService.getPlayerById(playerId);
        response.setContentType("application/json");
        if (playerDTO.isEmpty()) {
            response.setStatus(404);
        } else {
            PrintWriter printWriter = response.getWriter();
            printWriter.println(objectWriter.writeValueAsString(playerDTO.get()));
            printWriter.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        PlayerDTO playerDTO = objectMapper.readValue(json, PlayerDTO.class);
        Optional<PlayerDTO> playerDTOOpt = playerService.getPlayerById(playerDTO.getId());
        resp.setContentType("application/json");
        if (playerDTOOpt.isEmpty()) {
            resp.setStatus(404);
        } else {
            playerDTO = playerService.update(playerDTO);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(objectWriter.writeValueAsString(playerDTO));
            printWriter.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = String.valueOf(req.getRequestURL());
        int index = url.lastIndexOf("/");
        Long id = Long.valueOf(url.substring(index + 1));
        Optional<PlayerDTO> playerDTOOpt = playerService.getPlayerById(id);
        if (playerDTOOpt.isEmpty()) {
            resp.setStatus(404);
        } else {
            playerService.delete(id);
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String requestUrl = String.valueOf(request.getPathInfo());
        request.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(request.getReader());
//        if(requestUrl.matches(".*([0-9]*)/currency")){
//            CurrencyDTO currencyDTO = objectMapper.readValue(json, CurrencyDTO.class);
//            Long playerId = requestUrl.
//        }
        PlayerDTO playerDTO = objectMapper.readValue(json, PlayerDTO.class);
        response.setContentType("application/json");
        playerDTO = playerService.update(playerDTO);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(objectWriter.writeValueAsString(playerDTO));
        printWriter.close();
    }
}
