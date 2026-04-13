package bank.adapter.in.web.controller;

import bank.adapter.in.web.dto.OperationDTO;
import bank.application.domain.Operation;
import bank.application.domain.compte.CompteCourant;
import bank.application.usecase.IDepotUseCase;
import bank.application.usecase.IGetCompteUseCase;
import bank.application.usecase.IGetOperationsUseCase;
import bank.application.usecase.IRetraitUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BankController.class)
class BankControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IGetCompteUseCase getCompteUseCase;
    @MockitoBean
    private IDepotUseCase depotUseCase;
    @MockitoBean
    private IRetraitUseCase retraitUseCase;
    @MockitoBean
    private IGetOperationsUseCase getOperationsUseCase;

    @Test
    @DisplayName("GET /bank/compte/{n} - Succès")
    void getCompte_Success() throws Exception {
        CompteCourant compte = new CompteCourant("123", new BigDecimal("100"), false, BigDecimal.ZERO);
        when(getCompteUseCase.getCompte("123")).thenReturn(compte);

        mockMvc.perform(get("/bank/compte/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCompte").value("123"))
                .andExpect(jsonPath("$.solde").value(100));
    }

    @Test
    @DisplayName("GET /bank/compte/{n} - 404 Not Found")
    void getCompte_NotFound() throws Exception {
        when(getCompteUseCase.getCompte("999")).thenThrow(new IllegalArgumentException("Compte introuvable"));

        mockMvc.perform(get("/bank/compte/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Compte introuvable"));
    }

    @Test
    @DisplayName("POST /operation/depot - Succès")
    void deposer_Success() throws Exception {
        OperationDTO dto = OperationDTO.builder()
                .numeroCompte("123")
                .montant(new BigDecimal("50"))
                .type("DEPOT")
                .libelle("test")
                .build();

        mockMvc.perform(post("/bank/operation/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /operation/depot - 400 Bad Request")
    void deposer_BadRequest() throws Exception {
        OperationDTO dto = OperationDTO.builder()
                .numeroCompte("123")
                .montant(new BigDecimal("-50"))
                .build();

        mockMvc.perform(post("/bank/operation/depot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Operation.ERR_MONTANT_STRICT_POSITIF));
    }

    @Test
    @DisplayName("POST /operation/retrait - 500 Internal Error")
    void retirer_ServerError() throws Exception {
        OperationDTO dto = OperationDTO.builder()
                .numeroCompte("123")
                .montant(new BigDecimal("50"))
                .build();

        doThrow(new RuntimeException("Crash")).when(retraitUseCase).retrait(any());

        mockMvc.perform(post("/bank/operation/retrait")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500));
    }

    @Test
    @DisplayName("GET /bank/compte/{n}/releve - Succès")
    void getOperations_Success() throws Exception {
        when(getOperationsUseCase.getOperations("123")).thenReturn(List.of());

        mockMvc.perform(get("/bank/compte/123/releve"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}