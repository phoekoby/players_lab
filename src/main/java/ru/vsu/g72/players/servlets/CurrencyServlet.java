package ru.vsu.g72.players.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import ru.vsu.g72.players.dto.CurrencyDTO;
import ru.vsu.g72.players.service.CurrencyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class CurrencyServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    private final CurrencyService currencyService;

    @Inject
    public CurrencyServlet(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long playerId = Long.parseLong(request.getParameter("player_id"));
        Long currencyId = Long.parseLong(request.getParameter("id"));
        Optional<CurrencyDTO> currencyDTO = currencyService.getCurrencyById(currencyId,playerId);
        response.setContentType("application/json");
        if (currencyDTO.isEmpty()) {
            response.setStatus(404);
        } else {
            PrintWriter printWriter = response.getWriter();
            printWriter.println(objectWriter.writeValueAsString(currencyDTO.get()));
            printWriter.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        CurrencyDTO currencyDTO = objectMapper.readValue(json, CurrencyDTO.class);
        Optional<CurrencyDTO> currencyDTOOptional = currencyService.getCurrencyById(currencyDTO.getId(), currencyDTO.getPlayerId());
        resp.setContentType("application/json");
        if (currencyDTOOptional.isEmpty()) {
            resp.setStatus(404);
        } else {
            currencyDTO = currencyService.update(currencyDTO);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(objectWriter.writeValueAsString(currencyDTO));
            printWriter.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        CurrencyDTO currencyDTO = objectMapper.readValue(json, CurrencyDTO.class);
        Optional<CurrencyDTO> currencyDTOOptional = currencyService.getCurrencyById(currencyDTO.getId(), currencyDTO.getPlayerId());
        if (currencyDTOOptional.isEmpty()) {
            resp.setStatus(404);
        } else {
            currencyService.delete(currencyDTOOptional.get().getId(), currencyDTOOptional.get().getPlayerId());
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(request.getReader());
        CurrencyDTO currencyDTO = objectMapper.readValue(json, CurrencyDTO.class);
        response.setContentType("application/json");
        currencyDTO = currencyService.createNewCurrency(currencyDTO);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(objectWriter.writeValueAsString(currencyDTO));
        printWriter.close();

    }
}
