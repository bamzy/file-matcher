package com.bamzy.insurance.ws.caller.input;

/**
 * User: alireza ghassemi
 */
public class GetTransferMoneyState extends AbstractUserInput{
	public String Date;
	public String PaymentId;
	public GetTransferMoneyState(String username, String date, String paymentId){
		super(username);
		Date = date;
		PaymentId = paymentId;
	}
}
