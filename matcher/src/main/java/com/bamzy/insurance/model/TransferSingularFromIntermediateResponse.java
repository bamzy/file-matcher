package com.bamzy.insurance.model;

import java.util.List;

/**
 * User: alireza ghassemi
 */
public class TransferSingularFromIntermediateResponse{
	private String operationType;
	private String status;
	private List<String> reasons;
	private Transfer transfer;
	private String referenceId;
	public String getOperationType(){
		return operationType;
	}
	public void setOperationType(String operationType){
		this.operationType = operationType;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public List<String> getReasons(){
		return reasons;
	}
	public void setReasons(List<String> reasons){
		this.reasons = reasons;
	}
	public Transfer getTransfer(){
		return transfer;
	}
	public void setTransfer(Transfer transfer){
		this.transfer = transfer;
	}
	public String getReferenceId(){
		return referenceId;
	}
	public void setReferenceId(String referenceId){
		this.referenceId = referenceId;
	}
}
