package com.bamzy.insurance.ws.caller;

import com.bamzy.insurance.ws.caller.output.GetTransferMoneyStateResult;
import com.bamzy.insurance.ws.caller.output.TransferMoneySetOrderResult;

/**
 * User: alireza ghassemi
 */
public interface ServiceCaller{
	public GetTransferMoneyStateResult getTransferMoneyState(String date, String paymentId) throws Exception;
	public TransferMoneySetOrderResult transferMoneySetOrder(String sourceDepositNumber, String destinationDepositNumber, Long amount, String sourceComment, String destComment, String paymentId) throws Exception;
}
