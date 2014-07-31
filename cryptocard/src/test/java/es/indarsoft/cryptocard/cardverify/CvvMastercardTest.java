package es.indarsoft.cryptocard.cardverify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.indarsoft.cryptocard.card.Card;
import es.indarsoft.cryptocard.cardverify.CardVerification;
import es.indarsoft.cryptocard.symmetrickey.DesKey;

public class CvvMastercardTest {

	public static String 	panNumber 			= "5200000000000007";
	public static String 	expiredDate			= "1601";	
	public static String    serviceCode			= "201";
	public static String    cvv					= "314";
	public static String    icvv				= "304";
	public static String    cvv2				= "517";	
	public static CardVerification 		a ;
	public static DesKey 	b ;
	public static String cvvKeyA = "0123456789ABCDEF";
	public static String cvvKeyB = "0123456789ABCDEF";	
	
	
	@BeforeClass
	public static void setUpBeforeClass() {
		

		try {
			b = new DesKey(cvvKeyA+cvvKeyB);
			Card c = new Card(panNumber);
			c.setExpirationDate( expiredDate );
			c.setServiceCode( serviceCode );
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
		String DATEFORMAT = "MMYY" ;
		try {
			returnedValue = a.getCvv2 ( ) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( cvv2.equals(returnedValue) ){
			System.out.printf("Cvv.getCvv2() for PAN : %s FecCad : %s DATEFORMAT is %s expected is %s returned is %s \n", panNumber, expiredDate, DATEFORMAT, cvv2, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Cvv.getCvv2() for PAN : %s FecCad : %s DATEFORMAT is %s expected is %s returned is %s \n", panNumber, expiredDate, DATEFORMAT, cvv2, returnedValue);
			assertFalse( true) ;
		}			
	}	
	
}
