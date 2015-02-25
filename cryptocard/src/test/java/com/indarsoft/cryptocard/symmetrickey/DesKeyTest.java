package com.indarsoft.cryptocard.symmetrickey;

import static org.junit.Assert.*;

import org.junit.Test;

import com.indarsoft.cryptocard.symmetrickey.DesKey;
import com.indarsoft.cryptocard.symmetrickey.DesKeyException;

public class DesKeyTest {

	public String simpleKeyStr = "0123456789ABCDEF" ;
	public String doubleKeyStr = "F1FD8AC2EFCE3BC8F1FD8AC2EFCE3BC8" ;
	public String simpleKeyCheckValue = "D5D44F";
	public String doubleKeyCheckValue = "13F5CC";
	
	@Test
	public void testDesKeyString() {
		DesKey a = null ;
		try{
			a = new DesKey(simpleKeyStr ) ;	 
		}
		catch ( DesKeyException e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
		try {
			if ( a.getCheckValueAsString().equals( simpleKeyCheckValue)  ){
				System.out.printf("testDesKeyString() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertTrue( true) ;
			}else{
				System.out.printf("testDesKeyString() for Key %s expected is %s returned is %s \n", simpleKeyStr, simpleKeyCheckValue, a.getCheckValueAsString() );
				assertFalse( true) ;
			}				
		}catch ( DesKeyException e  ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}
	}


}
