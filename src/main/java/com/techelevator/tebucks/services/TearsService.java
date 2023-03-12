package com.techelevator.tebucks.services;

import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.TxLogDto;
import com.techelevator.tebucks.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class TearsService {

    private static final String API_BASE_URL = "https://te-pgh-api.azurewebsites.net/";
    private static final String API_KEY = "181";
    private final RestTemplate restTemplate = new RestTemplate();

    //wow!
    public String getToken() {
        String json = "{\"username\":\"WeCantLogin\",\"password\":\"assword\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        ResponseEntity<Map> response = restTemplate.exchange("https://te-pgh-api.azurewebsites.net/api/login", HttpMethod.POST, entity, Map.class);
        return (String) response.getBody().get("token");
    }

    public void reportTransferToTEARS(Transfer transfer, String description) {
        TxLogDto txLogDto = new TxLogDto();
        txLogDto.setAmount(transfer.getAmount());
        txLogDto.setUsername_from(transfer.getUserFrom().getUsername());
        txLogDto.setUsername_to(transfer.getUserTo().getUsername());
        txLogDto.setDescription(description);

        HttpHeaders headers = new HttpHeaders();
        String token = getToken();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TxLogDto> entity = new HttpEntity<>(txLogDto, headers);

        TxLogDto returnedTxlogDto = null;

        try {
            returnedTxlogDto = restTemplate.postForObject(API_BASE_URL + "/api/TxLog", entity, TxLogDto.class );
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
    }
}
