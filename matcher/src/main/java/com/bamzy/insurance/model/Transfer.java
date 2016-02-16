package com.bamzy.insurance.model;

/**
 * User: alireza ghassemi
 */
public class Transfer{
	private String fromDeposit;
	private String toDeposit;
	private Long amount;
	private String referenceId;
	public String getFromDeposit(){
		return fromDeposit;
	}
	public void setFromDeposit(String fromDeposit){
		this.fromDeposit = fromDeposit;
	}
	public String getToDeposit(){
		return toDeposit;
	}
	public void setToDeposit(String toDeposit){
		this.toDeposit = toDeposit;
	}
	public Long getAmount(){
		return amount;
	}
	public void setAmount(Long amount){
		this.amount = amount;
	}
	public String getReferenceId(){
		return referenceId;
	}
	public void setReferenceId(String referenceId){
		this.referenceId = referenceId;
	}
}
