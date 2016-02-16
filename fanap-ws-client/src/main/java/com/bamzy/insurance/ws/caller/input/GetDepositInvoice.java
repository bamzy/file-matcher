package com.bamzy.insurance.ws.caller.input;

import com.bamzy.insurance.ws.caller.WebServiceCaller;

import java.util.Date;

/**
 * Created by door on 1/6/2015.
 */
public class GetDepositInvoice extends AbstractUserInput{
	public String DepositNumber;
	public String StartDate;
	public String EndDate;
	public GetDepositInvoice(String username, String depositNumber, Date startDate, Date endDate){
		super(username);
		DepositNumber = depositNumber;
		StartDate = WebServiceCaller.getSimpleDateFormat().format(startDate);
		EndDate = WebServiceCaller.getSimpleDateFormat().format(endDate);
	}
}
