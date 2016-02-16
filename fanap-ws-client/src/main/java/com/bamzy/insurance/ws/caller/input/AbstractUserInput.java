package com.bamzy.insurance.ws.caller.input;

import com.bamzy.insurance.ws.caller.WebServiceCaller;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Date;

/**
 * @author alireza ghassemi
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public abstract class AbstractUserInput{
	public String Username;
	public String Timestamp = WebServiceCaller.getSimpleDateFormat().format(new Date());
	public AbstractUserInput(String username){
		Username = username;
	}
}
