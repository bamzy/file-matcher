package com.bamzy.insurance.ws.caller.output;

import java.util.List;

/**
 * Created by alireza ghassemi on 1/10/2015.
 */
public class GetDepositInvoiceResult{
	public List<Transaction> Data;

	public static class Transaction{
		public String TransactionDate;
		public Long DeptorAmount;
		public Long CreditorAmount;
		public String Description;
		public String DocNumber;
		public String ChqNumber;
		public Long Amount;
		public String PaymentId;
		public String BranchCode;
		public String BranchName;
	}
}
