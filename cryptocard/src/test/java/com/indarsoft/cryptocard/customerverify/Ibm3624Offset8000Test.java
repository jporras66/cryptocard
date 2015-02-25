package com.indarsoft.cryptocard.customerverify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.customerverify.Ibm3624Offset;
import com.indarsoft.cryptocard.symmetrickey.Pvk;

public class Ibm3624Offset8000Test {

	public static String panNumber 		= "5200000000000007"; 
	public static String offset			= "6441";
	public static String pin			= "1111";
	public static String naturalPin		= "5770";
	public static String pvkKey 		= "0123456789ABCDEF0123456789ABCDEF";	
	public static Ibm3624Offset ibmoff ;
	public static Pvk 			pvk ;
	public static Card 			card;
	String returnedValue 		= "";
	
	public static byte[] decimaliationTable = {
			0x09,  0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00 } ;

	@BeforeClass
	public static void setUpBeforeClass() {
		

		try {
			card = new Card( panNumber ) ;
			card.setPin( pin ); // pinLengtg same length as offset			
			card.setOffset( offset );			
			card.setPin( pin );
			pvk = new Pvk(pvkKey) ;
			ibmoff = new Ibm3624Offset( pvk, decimaliationTable ) ;	
			
		}
		catch ( Exception e ){
			System.out.println("Exception " + e.getMessage() ) ;
			assertFalse( true) ;
		}	
	}
	
	@Test
	public void testGeneratePin() {
		
		try{
			String valdata 		= card.getPanNumber().substring( 0  , 15 )+"F" ;
			returnedValue = ibmoff.generatePin(  valdata ,  card.getOffset()  ) ;
		}catch (Exception e){
			returnedValue = e.getMessage() ;
		}

		if (returnedValue.equals( pin ) ){
			System.out.printf("testGeneratePin() - OK panNumber %s  offset %s  PIN expected is %s returned is %s \n" , panNumber, offset, pin , returnedValue );
			assertTrue( true) ;
		}else{
			System.out.printf("testGeneratePin() - KO panNumber %s  offset %s  PIN expected is %s returned is %s \n" , panNumber, offset, pin , returnedValue );
			assertFalse( true) ;
		}
		
	}
	
	@Test
	public void testGenerateOffset() {
		
		try{
			String valdata 		= card.getPanNumber().substring( 0  , 15 )+"F" ;
			returnedValue = ibmoff.generateOffset( valdata,  card.getPin()   ) ;
		}catch (Exception e){
			returnedValue = e.getMessage() ;
		}

		if (returnedValue.equals( offset ) ){
			System.out.printf("testGenerateOffset() - OK panNumber %s  PIN %s  offset expected is %s returned is %s \n" , panNumber, pin , offset , returnedValue );
			assertTrue( true) ;
		}else{
			System.out.printf("testGenerateOffset() - panNumber %s  PIN %s  offset expected is %s returned is %s \n" , panNumber, pin , offset , returnedValue );
			assertFalse( true) ;
		}
		
	}
	
	@Test
	public void testGetNaturalPin() {
		
		try{
			String valdata 		= card.getPanNumber().substring( 0  , 15 )+"F" ;
			returnedValue = ibmoff.calculateNaturalPin( valdata ,  card.getPinLength()  ) ; 
		}
		catch ( Exception e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (returnedValue.equals( naturalPin ) ){
			System.out.printf("testGetNaturalPin() - OK panNumber %s  offset %s  Natural PIN expected is %s returned is %s \n" , panNumber, offset, naturalPin, returnedValue );
			assertTrue( true) ;
		}else{
			System.out.printf("testGetNaturalPin() - KO panNumber %s  offset %s  Natural PIN expected is %s returned is %s \n" , panNumber, offset, naturalPin, returnedValue );
			assertFalse( true) ;
		}
		
	}		
}
