package com.bamzy.insurance.fanap;

import com.bamzy.insurance.model.TotanData;
import com.bamzy.insurance.model.TransferSingularFromIntermediateRequest;
import com.bamzy.insurance.model.TransferSingularFromIntermediateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

/**
 * @author Bamdad
 */
public class FanapPaymentService {
    @Value("${fanap.service.url}")
    private String fanapServiceUrl;
    public TransferSingularFromIntermediateResponse pay(TotanData totanData){
        RestTemplate restTemplate = new RestTemplate();
        TransferSingularFromIntermediateRequest request = new TransferSingularFromIntermediateRequest();
        request.setAmount(totanData.getAmount());
        request.setDepositNumber(totanData.getAccountNumber());
        request.setReferenceId(totanData.getRrn());
        request.setSourceComment(totanData.getContent());
        request.setDestComment(totanData.getContent());
        return restTemplate.postForEntity(fanapServiceUrl, request, TransferSingularFromIntermediateResponse.class).getBody();
    }
}
