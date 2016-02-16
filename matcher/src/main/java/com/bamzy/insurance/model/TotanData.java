package com.bamzy.insurance.model;

/**
 * User: alireza ghassemi
 */
public class TotanData{
	private String id;
	private String accountNumber;
	private Long amount;
	private String content;
	private String localDateTime;
	private String rrn;
	private String acceptorCode;
	private String terminalCode;
	private String traceCode;
	private String status;
	private String jsonData;
	public String getId(){
		return id;
	}
	public void setId(String id){
		this.id = id;
	}
	public String getAccountNumber(){
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber){
		this.accountNumber = accountNumber;
	}
	public Long getAmount(){
		return amount;
	}
	public void setAmount(Long amount){
		this.amount = amount;
	}
	public String getContent(){
		return content;
	}
	public void setContent(String content){
		this.content = content;
	}
	public String getLocalDateTime(){
		return localDateTime;
	}
	public void setLocalDateTime(String localDateTime){
		this.localDateTime = localDateTime;
	}
	public String getRrn(){
		return rrn;
	}
	public void setRrn(String rrn){
		this.rrn = rrn;
	}
	public String getAcceptorCode(){
		return acceptorCode;
	}
	public void setAcceptorCode(String acceptorCode){
		this.acceptorCode = acceptorCode;
	}
	public String getTerminalCode(){
		return terminalCode;
	}
	public void setTerminalCode(String terminalCode){
		this.terminalCode = terminalCode;
	}
	public String getTraceCode(){
		return traceCode;
	}
	public void setTraceCode(String traceCode){
		this.traceCode = traceCode;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getJsonData(){
		return jsonData;
	}
	public void setJsonData(String jsonData){
		this.jsonData = jsonData;
	}
}
