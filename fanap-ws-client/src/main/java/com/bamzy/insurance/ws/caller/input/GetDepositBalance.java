package com.bamzy.insurance.ws.caller.input;

/**
 * @author alireza ghassemi
 */
public class GetDepositBalance extends AbstractUserInput{
	public String DepositNumber;
	public GetDepositBalance(String username, String depositNumber){
		super(username);
		DepositNumber = depositNumber;
	}
}
