package es.indarsoft.cryptocard.utl;

import static org.junit.Assert.*;

import org.junit.Test;

import es.indarsoft.cryptocard.customerverify.CustomerVerifyException;
import es.indarsoft.cryptocard.utl.PinValidationData;

public class PinValidationDataTest {

	public static String panNumber 			= "9704151300007029"; 
	public static String pinValidationData 	= "4151300007000702";
	public static String pinValidationData2 = "970415130000702F";
	String returnedValue 		= "";
	
	@Test
	public void build7000() {
		/**
		 * Insert last5digits of account number in position 12 of account number 
		 * 
		 */
		try {
			returnedValue = PinValidationData.build7000(panNumber, 12);
		} catch (CustomerVerifyException e) {
			e.printStackTrace();
			assertFalse( true) ;
		}
		if (returnedValue.equals( pinValidationData ) ){
			System.out.printf("build7000 - OK returnedValue %s --> pinValidationData %s\n", returnedValue, pinValidationData);
			assertTrue( true) ;
		}else{
			System.out.printf("build7000 - KO returnedValue %s --> pinValidationData %s\n", returnedValue, pinValidationData);
			assertFalse( true) ;
		}
	}

	@Test
	public void build8000() {
		/**
		 * Insert last5digits of account number in position 12 of account number 
		 * 
		 */
		try {
			returnedValue = PinValidationData.build8000(panNumber, 1,15,'F');
		} catch (CustomerVerifyException e) {
			e.printStackTrace();
			assertFalse( true) ;
		}
		if (returnedValue.equals( pinValidationData2 ) ){
			System.out.printf("build8000 - OK returnedValue %s --> pinValidationData %s\n", returnedValue, pinValidationData2);
			assertTrue( true) ;
		}else{
			System.out.printf("build8000 - KO returnedValue %s --> pinValidationData %s\n", returnedValue, pinValidationData2);
			assertFalse( true) ;
		}
	}
}
