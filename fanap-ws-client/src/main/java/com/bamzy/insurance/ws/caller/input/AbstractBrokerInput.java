package com.bamzy.insurance.ws.caller.input;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.bamzy.insurance.ws.caller.WebServiceCaller;

import java.util.Date;

/**
 * User: alireza ghassemi
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public abstract class AbstractBrokerInput{
	public String BrokerId;
	public String Timestamp = WebServiceCaller.getSimpleDateFormat().format(new Date());
	protected AbstractBrokerInput(String brokerId){
		BrokerId = brokerId;
	}
}
