package com.bamzy.insurance.ws.caller.output;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by alireza ghassemi on 1/10/2015.
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
public abstract class AbstractOutput{
	public Boolean IsSuccess;
	public String Message;
	public Integer MessageCode; // i'm not sure about this, this used to be String, must be tested
}
