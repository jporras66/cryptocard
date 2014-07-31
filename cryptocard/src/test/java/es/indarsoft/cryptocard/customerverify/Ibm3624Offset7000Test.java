package es.indarsoft.cryptocard.customerverify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.indarsoft.cryptocard.card.Card;
import es.indarsoft.cryptocard.customerverify.Ibm3624Offset;
import es.indarsoft.cryptocard.symmetrickey.Pvk;
import es.indarsoft.cryptocard.utl.PinValidationData;

public class Ibm3624Offset7000Test {

	public static String panNumber 		= "9704151300007029"; 
	public static String offset			= "353235";
	public static String pin			= "345678";
	public static String naturalPin		= "092443";
	public static String pvkKey 		= "9B3D3BABF2EA0B7F136D49D36832FEC4";	
	public static Ibm3624Offset ibmoff ;
	public static Pvk 			pvk ;
	public static Card 			card;
	String returnedValue 		= "";
	
	public static String decimaliationTable = "0123456789012345";

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
			String valdata 		= PinValidationData.build7000(panNumber, 12);
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
			String valdata 		= PinValidationData.build7000(panNumber, 12);
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
			String valdata 		= PinValidationData.build7000(panNumber, 12);
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
