package com.bamzy.insurance.fanap;

import com.bamzy.insurance.Application;
import com.bamzy.insurance.model.SettlementMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * User: alireza ghassemi
 */
@Ignore
public class FanapPaymentServiceTest{
	private static SettlementMatcher settlementMatcher;
	public static void main(String[] args) throws Exception{
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
		settlementMatcher = applicationContext.getBean(SettlementMatcher.class);
		payTest();
		applicationContext.close();
	}
	@Test
	public static void payTest(){
//		settlementMatcher.payUnpaid();
	}
}
