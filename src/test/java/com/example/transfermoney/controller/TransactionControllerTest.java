package com.example.transfermoney.controller;

import com.example.transfermoney.model.Account;
import com.example.transfermoney.model.CurrencyEnum;
import com.example.transfermoney.model.MessageEnum;
import com.example.transfermoney.model.dtos.TransactionRequest;
import com.example.transfermoney.model.dtos.TransactionResponse;
import com.example.transfermoney.service.impl.TransactionServiceImpl;
import com.example.transfermoney.util.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author George Lykoudis
 * @date 4/6/2023
 */
@WebMvcTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    Long ACCOUNT_ID_1 = 1L;
    Long ACCOUNT_ID_2 = 2L;
    Long TRANSACTION_ID = 1L;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    TransactionServiceImpl transactionService;
    @Autowired
    ObjectMapper objectMapper;

    JsonParser<TransactionRequest> jsonParser;
    Account source;
    Account target;
    TransactionRequest successfulTransactionRequest;
    TransactionRequest insufficientBalanceTransactionRequest;
    TransactionRequest sameAccountTransactionRequest;
    TransactionRequest accountDoNotExistsTransactionRequest;

    @BeforeEach
    void setUp() throws Exception {
        source = Account.builder()
                .id(ACCOUNT_ID_1)
                .balance(100d)
                .currency(CurrencyEnum.EUR)
                .createdAt(new Date())
                .build();
        target = Account.builder()
                .id(ACCOUNT_ID_2)
                .balance(50d)
                .currency(CurrencyEnum.EUR)
                .createdAt(new Date())
                .build();
        jsonParser = new JsonParser<>();
        successfulTransactionRequest = jsonParser.loadJson("testData/successful_transaction.json", TransactionRequest.class);
        insufficientBalanceTransactionRequest = jsonParser.loadJson("testData/insufficient_balance.json", TransactionRequest.class);
        sameAccountTransactionRequest = jsonParser.loadJson("testData/same_account.json", TransactionRequest.class);
        accountDoNotExistsTransactionRequest = jsonParser.loadJson("testData/non_existent_account.json", TransactionRequest.class);
    }

    @Test
    void TransactionController_ExecuteSuccessfulTransaction_ReturnsOkResponseEntity() throws Exception {
        String message = MessageEnum.TRANSACTION_COMPLETED.getMessage();
        TransactionResponse response = TransactionResponse.builder()
                .transactionId(TRANSACTION_ID)
                .message(message)
                .build();
        when(transactionService.execute(any())).thenReturn(response);
        TransactionResponse serviceResponse = transactionService.execute(new TransactionRequest());

        mockMvc.perform(post("/api/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(successfulTransactionRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId", CoreMatchers.is(TRANSACTION_ID.intValue())));
    }

    @Test
    void TransactionController_ExecuteTransactionWithInsufficientBalance_ReturnsBadRequestResponseEntity() throws Exception {
        String json = objectMapper.writeValueAsString(insufficientBalanceTransactionRequest);
        String message = MessageEnum.NOT_SUFFICIENT_BALANCE.getMessage();
        TransactionResponse response = TransactionResponse.builder()
                .message(message)
                .build();

        when(transactionService.execute(any())).thenReturn(response);
        TransactionResponse serviceResponse = transactionService.execute(new TransactionRequest());

        mockMvc.perform(post("/api/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    void TransactionController_ExecuteTransactionWithSameAccount_ReturnsBadRequestResponseEntity() throws Exception {
        String json = objectMapper.writeValueAsString(sameAccountTransactionRequest);
        String message = MessageEnum.NOT_SUFFICIENT_BALANCE.getMessage();
        TransactionResponse response = TransactionResponse.builder()
                .message(message)
                .build();

        when(transactionService.execute(any())).thenReturn(response);

        mockMvc.perform(post("/api/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));
    }

    @Test
    void TransactionController_ExecuteTransactionWithNonExistingAccount_ReturnsBadRequestResponseEntity() throws Exception {
        String json = objectMapper.writeValueAsString(accountDoNotExistsTransactionRequest);
        String message = MessageEnum.NOT_SUFFICIENT_BALANCE.getMessage();
        TransactionResponse response = TransactionResponse.builder()
                .message(message)
                .build();

        when(transactionService.execute(any())).thenReturn(response);

        mockMvc.perform(post("/api/transaction")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", CoreMatchers.is(message)));
    }
}