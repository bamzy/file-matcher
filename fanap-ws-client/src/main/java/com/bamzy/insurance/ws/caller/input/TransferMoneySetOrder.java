package com.bamzy.insurance.ws.caller.input;

/**
 * User: alireza ghassemi
 */
public class TransferMoneySetOrder extends AbstractUserInput{
	public String SourceDepositNumber;
	public String DestDepositNumber;
	public String Amount;
	public String SourceComment;
	public String DestComment;
	public String PaymentId;
	public TransferMoneySetOrder(String username, String sourceDepositNumber, String destDepositNumber, String amount, String sourceComment, String destComment, String paymentId){
		super(username);
		SourceDepositNumber = sourceDepositNumber;
		DestDepositNumber = destDepositNumber;
		Amount = amount;
		SourceComment = sourceComment;
		DestComment = destComment;
		PaymentId = paymentId;
	}
}
