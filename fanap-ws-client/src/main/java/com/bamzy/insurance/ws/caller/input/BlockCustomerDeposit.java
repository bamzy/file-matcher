package com.bamzy.insurance.ws.caller.input;

/**
 * Created by alireza ghassemi on 1/11/2015.
 */
public class BlockCustomerDeposit extends AbstractBrokerInput{
	public String BlockNumber;
	public String Sheba;
	public String Amount;
	public String Comment;
	public BlockCustomerDeposit(String brokerId, String blockNumber, String sheba, String amount, String comment){
		super(brokerId);
		BlockNumber = blockNumber;
		Sheba = sheba;
		Amount = amount;
		Comment = comment;
	}
}
