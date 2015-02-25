package com.indarsoft.cryptocard.cardverify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.cardverify.CardVerification;
import com.indarsoft.cryptocard.symmetrickey.DesKey;

public class CvvVisaTest {

	public static String 	panNumber 			= "4000000000000002";
	public static String 	expiredDate			= "1601";	
	public static String    serviceCode			= "201";
	public static String    cvv					= "481";	
	public static String    icvv				= "177";
	public static String    cvv2				= "779";	
	public static CardVerification 		a ;
	public static DesKey 	b ;
	public static Card 		c ;
	public static String cvvKeyA = "0123456789ABCDEF";
	public static String cvvKeyB = "0123456789ABCDEF";	
	
	
	@BeforeClass
	public static void setUpBeforeClass() {
		

		try {
			c = new Card( panNumber ) ;
			c.setExpirationDate( expiredDate );
			c.setServiceCode( serviceCode );			
			b = new DesKey(cvvKeyA+cvvKeyB);
			a = new CardVerification(b, c);
		}
		catch ( Exception e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}	
	}

	@Test
	public void testGetCvv() {

		String returnedValue = "";
		try {
			returnedValue = a.getCvv( ) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( cvv.equals(returnedValue) ){
			System.out.printf("Cvv.GetCvv()  for PAN : %s FecCad : %s ServiceCode : %s expected is %s returned is %s \n", panNumber, expiredDate, serviceCode, cvv, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Cvv.GetCvv()  for PAN : %s FecCad : %s ServiceCode : %s expected is %s returned is %s \n", panNumber, expiredDate, serviceCode, cvv, returnedValue);
			assertFalse( true) ;
		}			
	}


	
	@Test
	public void testGetIcvv() {
 
		String returnedValue = "";
		try {
			returnedValue = a.getIcvv( ) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( icvv.equals(returnedValue) ){
			System.out.printf("Cvv.GetIcvv() for PAN : %s FecCad : %s expected is %s returned is %s \n", panNumber, expiredDate, icvv, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Cvv.GetIcvv() for PAN : %s FecCad : %s expected is %s returned is %s \n", panNumber, expiredDate, icvv, returnedValue);
			assertFalse( true) ;
		}			
	}
	
	@Test
	public void testGetCvv2() {
 
		String returnedValue = "";
		try {
			returnedValue = a.getCvv2 ( ) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( cvv2.equals(returnedValue) ){
			System.out.printf("Cvv.getCvv2() for PAN : %s FecCad : %s expected is %s returned is %s \n", panNumber, expiredDate, cvv2, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Cvv.getCvv2() for PAN : %s FecCad : %s expected is %s returned is %s \n", panNumber, expiredDate, cvv2, returnedValue);
			assertFalse( true) ;
		}			
	}	
	
}
