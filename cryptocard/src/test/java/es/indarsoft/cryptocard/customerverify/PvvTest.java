package es.indarsoft.cryptocard.customerverify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.indarsoft.cryptocard.card.Card;
import es.indarsoft.cryptocard.customerverify.Pvv;
import es.indarsoft.cryptocard.symmetrickey.Pvk;

public class PvvTest {

	public static String 	panNumber 			= "4000000000000002";
	public static String 	pin  				= "1111";
	public static String 	pvv  				= "3309" ;
	public static int 		pvki				=  1;
	public static Pvv 		a ;
	public static Card		card ;	
	public static String 	pvkKey 				= "0123456789ABCDEF0123456789ABCDEF";
	public static Pvk  pvk ;

	@BeforeClass
	public static void setUpBeforeClass() {
		

		try {
			card = new Card( panNumber ) ;
			card.setPvki( pvki );
			card.setPin( pin );
			card.setPvv( pvv );		
			pvk = new Pvk( pvkKey, pvki );
		}
		catch ( Exception e ){
			System.out.println("Exception 1 : " + e.getMessage() ) ;
		}	
	}

	@Test
	public void testGetPvv() {

		String returnedValue = "";
		try {
			returnedValue = Pvv.compute ( card  , pvk ) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( pvv.equals( returnedValue)  ){
			System.out.printf("Pvv.getPvv()  OK for PAN : %s dki : %s PIN : %s - PVV expected is %s returned is %s \n", panNumber, pvki , pin  , pvv ,  returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Pvv.getPvv()  KO for PAN : %s dki : %s PIN : %s - PVV expected is %s returned is %s \n", panNumber, pvki, pin , pvv ,  returnedValue);
			assertFalse( true) ;
		}			
	}

	@Test
	public void testFindPin() {

		String returnedValue = "" ;
		try {
			returnedValue = Pvv.findPin ( card , pvk) ;
		}	
		catch ( Exception e ){
				System.out.println("Exception : " + e.getMessage() ) ;			
		}
		
		if ( pin.equals(returnedValue) ){
			System.out.printf("Pvv.findPin() OK for PAN : %s dki : %s PVV : %s - PIN expected is %s returned is %s \n", panNumber, pvki, pvv, pin , returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("Pvv.findPin() KO for PAN : %s dki : %s PVV : %s - PIN expected is %s returned is %s \n", panNumber, pvki, pvv, pin , returnedValue);
			assertFalse( true) ;
		}			
	}
	
}
