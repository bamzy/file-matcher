package com.bamzy.insurance.ws.caller.output;

import java.util.List;

/**
 * Created by alireza ghassemi on 1/10/2015.
 */
public class GetDepositBalanceResult extends AbstractOutput{
	public GetDepositBalanceData Data;

	public static class GetDepositBalanceData{
		public String DepositNumber;
		public List<DepositAmount> Amounts;
		public List<DepositAmount> WithdrawableAmounts;
	}
	public static class DepositAmount{
		public Long Amount;
		public String CurrencySwiftCode;
		public Long CurrencyISOCode;
		public String CurrencyName;
	}
}
