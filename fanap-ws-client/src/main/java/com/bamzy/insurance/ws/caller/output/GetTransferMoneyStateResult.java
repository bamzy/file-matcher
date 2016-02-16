package com.bamzy.insurance.ws.caller.output;

/**
 * User: alireza ghassemi
 */
public class GetTransferMoneyStateResult extends AbstractOutput{
	public State Data;

	public static class State{
		public String Key;
		public String Value;
	}
}
