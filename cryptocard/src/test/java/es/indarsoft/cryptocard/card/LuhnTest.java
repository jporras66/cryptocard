package es.indarsoft.cryptocard.card;

import static org.junit.Assert.*;

import org.junit.Test;

import es.indarsoft.cryptocard.utl.CheckDigit;


public class LuhnTest {
	
	@Test
	public void validate() {

		String b = new String("5200000000000007");
		boolean returnedValue = CheckDigit.validate ( b ) ;	

		if ( returnedValue ){
			System.out.printf("testValidateLuhnCheckDigitVisa()    OK PAN %s \n", b );
			assertTrue( true) ;
		}else{
			System.out.printf("testValidateLuhnCheckDigitVisa()    KO PAN %s \n", b );
			assertFalse( true) ;
		}		
	}	
	
	@Test
	public void calculate() {

		String b = new String("520000000000000");
		String returnedValue ="";
		String expectedValue = "7";
		returnedValue = CheckDigit.calculate ( b ) ;
		if ( expectedValue.equals(returnedValue) ){
			System.out.printf("testCalculateLuhnCheckDigitVisa()   OK PAN %s expected is %s returned is %s \n", b, expectedValue, returnedValue);
			assertTrue( true) ;
		}else{  
			System.out.printf("testCalculateLuhnCheckDigitVisa()   KO PAN %s expected is %s returned is %s \n", b, expectedValue, returnedValue);
			assertFalse( true) ;
		}	
	}	
	
}
