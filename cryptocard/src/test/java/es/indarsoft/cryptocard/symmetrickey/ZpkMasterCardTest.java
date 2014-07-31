package es.indarsoft.cryptocard.symmetrickey;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.indarsoft.cryptocard.symmetrickey.DesKeyException;
import es.indarsoft.cryptocard.symmetrickey.Zpk;

public class ZpkMasterCardTest {

	public String simpleKeyStr = "ABCDEF2222220101" ;
	public String doubleKeyStr = "ABCDEF6666660101ABCDEF6666660202" ;
	public String simpleKeyCheckValue = "76A6C0";
	public String doubleKeyCheckValue = "5D8F82";
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testZpkSimple() {
		Zpk a = null ;
		try{
			a = new Zpk(simpleKeyStr) ;	
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
		if ( a.getKeyAsString().equals (simpleKeyStr) ){
			System.out.printf("testZpkSimple() for Key expected is %s returned is %s \n", simpleKeyStr, a.getKeyAsString());
			assertTrue( true) ;
		}else{
			System.out.printf("testZpkSimple() for Key expected is %s returned is %s \n", simpleKeyStr, a.getKeyAsString());
			assertFalse( true) ;
		}	
	}

	@Test
	public void testZpkCheckValueSimple() {
		Zpk a = null ;
		try{
			a = new Zpk(simpleKeyStr) ;	 
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
	
		
		try {
			if ( a.getCheckValueAsString().equals( simpleKeyCheckValue)  ){
				System.out.printf("testZpkCheckValueSimple() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertTrue( true) ;
			}else{
				System.out.printf("testZpkCheckValueSimple() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertFalse( true) ;
			}				
		}catch ( DesKeyException e  ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}

	}


	
	@Test
	public void testZpkDouble() {
		Zpk a = null ;
		try{
			a = new Zpk(doubleKeyStr) ;	
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
		if ( a.getKeyAsString().equals (doubleKeyStr) ){
			System.out.printf("testZpkDouble() for Key expected is %s returned is %s \n", doubleKeyStr, a.getKeyAsString());
			assertTrue( true) ;
		}else{
			System.out.printf("testZpkDouble() for Key expected is %s returned is %s \n", doubleKeyStr, a.getKeyAsString());
			assertFalse( true) ;
		}	
	}

	
	@Test
	public void testZpkCheckValueDouble() {
		Zpk a = null ;
		try{
			a = new Zpk(doubleKeyStr) ;	 
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
	
		
		try {
			if ( a.getCheckValueAsString().equals( doubleKeyCheckValue)  ){
				System.out.printf("testZpkCheckValueDouble() for Key %s expected is %s returned is %s \n", doubleKeyStr, doubleKeyCheckValue, a.getCheckValueAsString() );
				assertTrue( true) ;
			}else{
				System.out.printf("testZpkCheckValueDouble() for Key %s expected is %s returned is %s \n", doubleKeyStr, doubleKeyCheckValue, a.getCheckValueAsString() );
				assertFalse( true) ;
			}				
		}catch ( DesKeyException e  ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}

	}
	
}
