package com.indarsoft.cryptocard.pinblock;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.indarsoft.cryptocard.card.*;
import com.indarsoft.cryptocard.pinblock.IsoPinBlockFormat0;
import com.indarsoft.cryptocard.pinblock.PinBlock;
import com.indarsoft.cryptocard.symmetrickey.Zpk;
import com.indarsoft.cryptocard.types.PinBlockFormatType;
import com.indarsoft.utl.Binary;

public class PinBlockISO0VisaTest {

	public static String 		pin  				= "1111";
	public static String 		panNumber 			= "4000000000000002";
	public static String    	iwk_01	  			= "0123456789ABCDEF0123456789ABCDEF";
	public static String    	clearPinBlock		= "041111FFFFFFFFFF";
	public static String    	encryptedPinBlock	= "C117B58700DAE402";
	
	public static Card card ;
	public static Zpk  zpk ;
	public static PinBlock a ;
	public static PinBlock b ;
	public static PinBlockFormatType pinBlockFormatType ;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		try{
			card = new Card( panNumber ) ;
			card.setPinBlockFormatType(  PinBlockFormatType.ISOFORMAT0 );
			card.setPin( pin );
			zpk = new Zpk(iwk_01);
			a = IsoPinBlockFormat0.getEncrypted(card, zpk) ;
			b = IsoPinBlockFormat0.getDecrypted(encryptedPinBlock, card, zpk) ;
		}
		catch (Exception e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}	
	}
	
	@Test 
	public void testClearPinBlock() {
		
	
		byte[] pinBlock = a.getClearPinBlock();	
		
		String returnedValue = Binary.toHexStr(pinBlock);
		
		if ( clearPinBlock.equals(returnedValue) ){
			System.out.printf("PinBlock.testClearPinBlock() OK for PAN : %s pin : %s expected is %s returned is %s \n", panNumber, pin, clearPinBlock, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("PinBlock.testClearPinBlock() KO for PAN : %s pin : %s expected is %s returned is %s \n", panNumber,  pin, clearPinBlock, returnedValue);
			assertFalse( true) ;
		}			
		
	}
	
	@Test 
	public void testEncryptedPinBlock() {
		
	
		byte[] pinBlock = a.getEncryptedPinBlock();	
		
		String returnedValue = Binary.toHexStr(pinBlock);
		
		if ( encryptedPinBlock.equals(returnedValue) ){
			System.out.printf("PinBlock.testEncryptedPinBlock() OK for PAN : %s pin : %s expected is %s returned is %s \n", panNumber, pin, encryptedPinBlock, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("PinBlock.testEncryptedPinBlock() KO for PAN : %s pin : %s expected is %s returned is %s \n", panNumber,  pin, encryptedPinBlock, returnedValue);
			assertFalse( true) ;
		}			
		
	}	
	
	@Test 
	public void testClearPinBlock_B() {
		
	
		byte[] pinBlock = b.getClearPinBlock();	
		
		String returnedValue = Binary.toHexStr(pinBlock);
		
		if ( clearPinBlock.equals(returnedValue) ){
			System.out.printf("PinBlock.testClearPinBlock_B() OK for PAN : %s pin : %s expected is %s returned is %s \n", panNumber, pin, clearPinBlock, returnedValue);
			assertTrue( true) ;
		}else{
			System.out.printf("PinBlock.testClearPinBlock_B() KO for PAN : %s pin : %s expected is %s returned is %s \n", panNumber,  pin, clearPinBlock, returnedValue);
			assertFalse( true) ;
		}			
		
	}	
	
}
