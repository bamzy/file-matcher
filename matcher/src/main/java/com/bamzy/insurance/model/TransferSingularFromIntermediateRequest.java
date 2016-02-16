package com.bamzy.insurance.model;

/**
 * User: alireza ghassemi
 */
public class TransferSingularFromIntermediateRequest{
	private Long amount;
	private String depositNumber;
	private String referenceId;
	private String sourceComment;
	private String destComment;

	public String getDestComment() {
		return destComment;
	}

	public void setDestComment(String destComment) {
		this.destComment = destComment;
	}

	public Long getAmount(){
		return amount;
	}
	public void setAmount(Long amount){
		this.amount = amount;
	}
	public String getDepositNumber(){
		return depositNumber;
	}
	public void setDepositNumber(String depositNumber){
		this.depositNumber = depositNumber;
	}
	public String getReferenceId(){
		return referenceId;
	}
	public void setReferenceId(String referenceId){
		this.referenceId = referenceId;
	}

	public String getSourceComment() {
		return sourceComment;
	}

	public void setSourceComment(String sourceComment) {
		this.sourceComment = sourceComment;
	}
}
