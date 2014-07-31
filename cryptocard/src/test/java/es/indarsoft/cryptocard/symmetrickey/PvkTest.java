package es.indarsoft.cryptocard.symmetrickey;

import static org.junit.Assert.*;
import org.junit.Test;
import es.indarsoft.cryptocard.symmetrickey.DesKeyException;
import es.indarsoft.cryptocard.symmetrickey.Pvk;

public class PvkTest {

	public String simpleKeyStr = "0123456789ABCDEF" ;
	public String doubleKeyStr = "F1FD8AC2EFCE3BC8F1FD8AC2EFCE3BC8" ;
	public String simpleKeyCheckValue = "D5D44F";
	public String doubleKeyCheckValue = "13F5CC";
	
	@Test
	public void testPvkCheckValueDouble() {
		Pvk a = null ;
		try{
			a = new Pvk(doubleKeyStr, 1 ) ;	 
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
	
		
		try {
			if ( a.getCheckValueAsString().equals( doubleKeyCheckValue)  ){
				System.out.printf("testPvkCheckValueDouble() for Key %s expected is %s returned is %s \n", doubleKeyStr, doubleKeyCheckValue, a.getCheckValueAsString() );
				assertTrue( true) ;
			}else{
				System.out.printf("testPvkCheckValueDouble() for Key %s expected is %s returned is %s \n", doubleKeyStr, doubleKeyCheckValue, a.getCheckValueAsString() );
				assertFalse( true) ;
			}				
		}catch ( DesKeyException e  ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}

	}

	@Test
	public void testPvkCheckValueSimple() {
		Pvk a = null ;
		try{
			a = new Pvk(simpleKeyStr, 1 ) ;	 
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
	
		
		try {
			if ( a.getCheckValueAsString().equals( simpleKeyCheckValue)  ){
				System.out.printf("testPvkCheckValueSimple() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertTrue( true) ;
			}else{
				System.out.printf("testPvkCheckValueSimple() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertFalse( true) ;
			}				
		}catch ( DesKeyException e  ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}

	}


	
	@Test
	public void testPvkDouble() {
		Pvk a = null ;
		try{
			a = new Pvk(doubleKeyStr, 1 ) ;	
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
		if ( a.getKeyAsString().equals (doubleKeyStr) ){
			System.out.printf("testPvkDouble() for Key expected is %s returned is %s \n", doubleKeyStr, a.getKeyAsString());
			assertTrue( true) ;
		}else{
			System.out.printf("testPvkDouble() for Key expected is %s returned is %s \n", doubleKeyStr, a.getKeyAsString());
			assertFalse( true) ;
		}	
	}

	@Test
	public void testPvkSimple() {
		Pvk a = null ;
		try{
			a = new Pvk(simpleKeyStr, 1 ) ;	
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
		if ( a.getKeyAsString().equals (simpleKeyStr) ){
			System.out.printf("testPvkSimple() for Key expected is %s returned is %s \n", simpleKeyStr, a.getKeyAsString());
			assertTrue( true) ;
		}else{
			System.out.printf("testPvkSimple() for Key expected is %s returned is %s \n", simpleKeyStr, a.getKeyAsString());
			assertFalse( true) ;
		}	
	}	
}
