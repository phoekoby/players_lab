package ru.vsu.g72.players.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import ru.vsu.g72.players.dto.ProgressDTO;
import ru.vsu.g72.players.service.ProgressService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class ProgressServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    private final ProgressService progressService;

    @Inject
    public ProgressServlet(ProgressService progressService) {
        this.progressService = progressService;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long playerId = Long.parseLong(request.getParameter("player_id"));
        Long currencyId = Long.parseLong(request.getParameter("id"));
        Optional<ProgressDTO> currencyDTO = progressService.getProgressById(currencyId,playerId);
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
        ProgressDTO progressDTO = objectMapper.readValue(json, ProgressDTO.class);
        Optional<ProgressDTO> progressDTOOPtional = progressService.getProgressById(progressDTO.getId(), progressDTO.getPlayerId());
        resp.setContentType("application/json");
        if (progressDTOOPtional.isEmpty()) {
            resp.setStatus(404);
        } else {
            progressDTO = progressService.update(progressDTO);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(objectWriter.writeValueAsString(progressDTO));
            printWriter.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        ProgressDTO progressDTO = objectMapper.readValue(json, ProgressDTO.class);
        Optional<ProgressDTO> progressDTOOptional = progressService.getProgressById(progressDTO.getId(), progressDTO.getPlayerId());
        if (progressDTOOptional.isEmpty()) {
            resp.setStatus(404);
        } else {
            progressService.delete(progressDTOOptional.get().getId(), progressDTOOptional.get().getPlayerId());
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(request.getReader());
        ProgressDTO progressDTO = objectMapper.readValue(json, ProgressDTO.class);
        response.setContentType("application/json");
        progressDTO = progressService.save(progressDTO);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(objectWriter.writeValueAsString(progressDTO));
        printWriter.close();

    }
}
