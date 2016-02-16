package com.bamzy.insurance.model;


import com.bamzy.insurance.fanap.FanapPaymentService;
import com.bamzy.insurance.matcher.BatchFilePayment;
import com.bamzy.insurance.ws.caller.WebServiceCaller;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Bamdad
 */
@Component
public class SettlementMatcher {
    private static Logger log = getLogger(SettlementMatcher.class);
    private Repository repository;
    private FanapPaymentService fanapPaymentService;

    @Autowired
    private WebServiceCaller webServiceCaller;

    @Autowired
    private MatcherDbHandler matcherDbHandler;

    @Autowired
    private BatchFilePayment batchFilePayment;

    @Autowired
    public void setRepository(Repository repository) {
        this.repository = repository;
    }
    @Autowired
    public void setFanapPaymentService(FanapPaymentService fanapPaymentService){
        this.fanapPaymentService = fanapPaymentService;
    }

    @ServiceActivator(inputChannel = "trigger-match-channel")
    public void match() {
        log.debug("I'm matching");

        FullRecord record;
        final ArrayList<FullRecord> items = new ArrayList<FullRecord>();

        while ((record = repository.next()) != null) {
            log.debug("record: " + record.getShaparakRecord().getKey());
            queuePayment(record);
            items.add(record);
        }
//        batchFilePayment.toFile(items);
        payUnpaid();
        log.debug("match is done");
    }
    /**
     * Searches for rows of successfullData with 'unpaid' status and tries to pay them using totanData. The ones that were successfully paid are changed to 'paid' and their paymentId is set
     */
    private void payUnpaid(){
        List<TotanData> totanDataList = matcherDbHandler.getUnpaidSuccessfullData();
        List<TotanData> paidTotanData = new LinkedList<>();
        for(TotanData totanData : totanDataList)
            if(fanapPaymentService.pay(totanData).getStatus().equals("ok"))
                paidTotanData.add(totanData);
        matcherDbHandler.changeSuccessfullDataToPaid(paidTotanData);
    }
    @Transactional
    public void queuePayment(FullRecord record) {

        try {
            matcherDbHandler.updateShaparakToMatched(record.getShaparakRecord());
            matcherDbHandler.updateTotanToMatched(record.getTotanRecord());
            matcherDbHandler.addToSuccessFull(record);
        } catch (RuntimeException e) {
            System.out.println("Payment Problem");
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
