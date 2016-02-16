package com.bamzy.insurance.ws.caller.input;

/**
 * Created by door on 1/7/2015.
 */
public class UnblockDepositAndTransfer extends AbstractBrokerInput{
	public String UnblockNumber;
	public String SourceDepositNumber;
	public String DestDepositNumber;
	public String IsUnblockRemainAmount;
	public String Amount;
	public UnblockDepositAndTransfer(String brokerId, String unblockNumber, String sourceDepositNumber, String destDepositNumber, String isUnblockRemainAmount, String amount){
		super(brokerId);
		UnblockNumber = unblockNumber;
		SourceDepositNumber = sourceDepositNumber;
		DestDepositNumber = destDepositNumber;
		IsUnblockRemainAmount = isUnblockRemainAmount;
		Amount = amount;
	}
}
