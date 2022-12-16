package ru.vsu.g72.players.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import ru.vsu.g72.players.dto.ItemDTO;
import ru.vsu.g72.players.service.ItemService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class ItemServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

    private final ItemService itemService;

    @Inject
    public ItemServlet(ItemService itemService) {
        this.itemService = itemService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long playerId = Long.parseLong(request.getParameter("player_id"));
        Long itemId = Long.parseLong(request.getParameter("id"));
        Optional<ItemDTO> itemDTO = itemService.getItemById(itemId,playerId);
        response.setContentType("application/json");
        if (itemDTO.isEmpty()) {
            response.setStatus(404);
        } else {
            PrintWriter printWriter = response.getWriter();
            printWriter.println(objectWriter.writeValueAsString(itemDTO.get()));
            printWriter.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        ItemDTO itemDTO = objectMapper.readValue(json, ItemDTO.class);
        Optional<ItemDTO> currencyDTOOptional = itemService.getItemById(itemDTO.getId(), itemDTO.getPlayerId());
        resp.setContentType("application/json");
        if (currencyDTOOptional.isEmpty()) {
            resp.setStatus(404);
        } else {
            itemDTO = itemService.update(itemDTO);
            PrintWriter printWriter = resp.getWriter();
            printWriter.println(objectWriter.writeValueAsString(itemDTO));
            printWriter.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(req.getReader());
        ItemDTO itemDTO = objectMapper.readValue(json, ItemDTO.class);
        Optional<ItemDTO> itemDTOOptional = itemService.getItemById(itemDTO.getId(), itemDTO.getPlayerId());
        if (itemDTOOptional.isEmpty()) {
            resp.setStatus(404);
        } else {
            itemService.delete(itemDTOOptional.get().getId(), itemDTOOptional.get().getPlayerId());
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String json = IOUtils.toString(request.getReader());
        ItemDTO itemDTO = objectMapper.readValue(json, ItemDTO.class);
        response.setContentType("application/json");
        itemDTO = itemService.save(itemDTO);
        PrintWriter printWriter = response.getWriter();
        printWriter.println(objectWriter.writeValueAsString(itemDTO));
        printWriter.close();

    }
}
