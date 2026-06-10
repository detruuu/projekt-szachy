package com.uep.wap.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uep.wap.config.SecurityConfig;
import com.uep.wap.dto.PlayerDTO;
import com.uep.wap.dto.TournamentCreateRequestDTO;
import com.uep.wap.dto.TournamentDTO;
import com.uep.wap.service.PlayerService;
import com.uep.wap.service.TournamentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PlayerController.class, TournamentController.class})
@Import(SecurityConfig.class)
class SecurityEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @org.springframework.boot.test.mock.mockito.MockBean
    private PlayerService playerService;

    @org.springframework.boot.test.mock.mockito.MockBean
    private TournamentService tournamentService;

    @Test
    void getPlayers_isPublic() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of());

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk());
    }

    @Test
    void createPlayer_withoutAuth_returns401() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("Jan");

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPlayer_withAdminRole_returns201() throws Exception {
        PlayerDTO playerDTO = new PlayerDTO();
        playerDTO.setFirstName("Jan");
        playerDTO.setLastName("Kowalski");

        when(playerService.createPlayer(any(PlayerDTO.class))).thenReturn(playerDTO);

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ARBITER")
    void createTournament_withArbiterRole_returns201() throws Exception {
        TournamentCreateRequestDTO requestDTO = new TournamentCreateRequestDTO();
        requestDTO.setName("Open A");
        requestDTO.setStartDate(LocalDate.now());

        TournamentDTO responseDTO = new TournamentDTO();
        responseDTO.setName("Open A");

        when(tournamentService.createTournament(any(TournamentCreateRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/tournaments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }
}