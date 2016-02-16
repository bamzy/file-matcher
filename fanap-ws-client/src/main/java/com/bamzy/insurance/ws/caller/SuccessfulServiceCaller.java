package com.bamzy.insurance.ws.caller;

import com.bamzy.insurance.ws.caller.output.GetTransferMoneyStateResult;
import com.bamzy.insurance.ws.caller.output.TransferMoneySetOrderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: alireza ghassemi
 */
public class SuccessfulServiceCaller implements ServiceCaller {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public GetTransferMoneyStateResult getTransferMoneyState(String date, String paymentId) throws Exception {
        logger.info("getTransferMoneyState(date = '{}', paymentId = '{}'", date, paymentId);
        logger.info("getTransferMoneyState -> IsSuccess = true");
        GetTransferMoneyStateResult result = new GetTransferMoneyStateResult();
        result.IsSuccess = true;
        result.MessageCode = 0;
        result.Message = "";
        result.Data.Key = "confirmed";
        result.Data.Value = "ارسالی - تایید شده";
        return result;
    }

    @Override
    public TransferMoneySetOrderResult transferMoneySetOrder(String sourceDepositNumber, String destinationDepositNumber, Long amount, String sourceComment, String destComment, String paymentId) throws Exception {
        logger.info("transferMoneySetOrder(sourceDepositNumber = '{}', destinationDepositNumber = '{}', amount = {}, sourceComment = '{}', destComment = '{}', paymentId = '{}'", sourceDepositNumber, destinationDepositNumber, amount, sourceComment, destComment, paymentId);
        logger.info("transferMoneySetOrder -> IsSuccess = true");
        TransferMoneySetOrderResult result = new TransferMoneySetOrderResult();
        result.IsSuccess = true;
        result.MessageCode = 0;
        result.Message = "";
        result.Data = "123456798321654";
        return result;
    }
}
