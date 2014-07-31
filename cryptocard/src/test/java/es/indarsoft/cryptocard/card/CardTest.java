package es.indarsoft.cryptocard.card;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import es.indarsoft.cryptocard.card.Card;
import es.indarsoft.cryptocard.card.CardException;

 
public class CardTest {

	public static String panNumber  		="5200000000000007";
	public static String expirationDate 	="1303";
	public static String serviceCode 		="201";		
	public static Card	 card; 

	@BeforeClass
	public static void setUpBeforeClass() {
		

		try {
			card = new Card(panNumber);
		}
		catch ( Exception e ){
			System.out.println("Exception : " + e.getMessage() ) ;
		}	
	}
	@Test
	public void testNumericPanNumber() {
		
		@SuppressWarnings("unused")
		Card a = null ;
		String returnedValue = "" ; 
		String aPanNumber = "523236003157102F";
		try{
			a = new Card( aPanNumber );
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testNumericPanNumber()          : OK " + aPanNumber + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testNumericPanNumber()          : KO "  );
			assertFalse( true) ;
		}
	}	
	

	@Test
	public void testValidateLuhnCheckDigit() {

		@SuppressWarnings("unused")
		Card a = null ;
		String returnedValue = "" ;
		String aPanNumber = "5489011000226602";   // wrong check digit --> chech digit is 1
		try{
			a = new Card( aPanNumber );
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testValidateLuhnCheckDigit()    : OK " +  aPanNumber + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testValidateLuhnCheckDigit()    : KO " +  aPanNumber + " - " + returnedValue );
			assertFalse( true) ;
		}
		
	}	
	
	@Test
	public void testValidateLuhnCheckDigit2() {

		@SuppressWarnings("unused")
		Card a = null ;
		String returnedValue = "" ;
		String aPanNumber = "5489011000226601";  
		try{
			a = new Card( aPanNumber );
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if ( returnedValue.isEmpty() ){
			System.out.println("testValidateLuhnCheckDigit2()   : OK " +  aPanNumber  );
			assertTrue( true) ;
		}else{
			System.out.println("testValidateLuhnCheckDigit2()   : KO " +  aPanNumber + " - " + returnedValue );
			assertFalse( true) ;
		}
		
	}
	
	@Test
	public void testExpirationDate() {
		
		String returnedValue = "" ; 
		String aexpirationDate  = "1399";
		
		try{
			card.setExpirationDate( aexpirationDate ) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testExpirationDate()            : OK " +  aexpirationDate + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testExpirationDate()            : KO "  );
			assertFalse( true) ;
		}
	}
	
	@Test
	public void testExpirationDateLength() {
		
		String returnedValue = "" ; 
		String aexpirationDate  = "13035";

		try{
			card.setExpirationDate( aexpirationDate  );
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testExpirationDateLength()      : OK " +  aexpirationDate + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testExpirationDateLength()      : KO "  );
			assertFalse( true) ;
		}
		
	}

	@Test
	public void testExpirationDateNumeric() {
		
		String returnedValue = "" ; 
		String aexpirationDate  = "13FF";
		try{
			card.setExpirationDate( aexpirationDate ) ;	
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testExpirationDateNumeric()     : OK " +  aexpirationDate + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testExpirationDateNumeric()     : KO "  );
			assertFalse( true) ;
		}
		
	}

	
	@Test
	public void testCardBrand() {
		
		Card a = null ;
		String returnedValue = "" ; 
		String aPanNumber = "3232360031571021"; //must start 4 for VISA or 51-55 for MASTERCARD !!

		try{
			a = new Card( aPanNumber );
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if ( returnedValue.isEmpty() ){
			System.out.println("testCardBrand()                 : OK " +  aPanNumber + " card brand : " + a.getCardBrand() + " " +returnedValue);
			assertTrue( true) ;
		}else{
			System.out.println("testCardBrand()                 : KO " +  aPanNumber + " card brand : " + a.getCardBrand() + " " +returnedValue);
			assertFalse( true) ;
		}
	}	

	@Test
	public void testSetServiceCode() {
		
		Card a = null ;
		String returnedValue = "" ; 
		String serviceCode = "AAA";
		
		try{
			a = new Card( panNumber );
			a.setServiceCode(serviceCode) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetServiceCode()            : OK " +  panNumber + " - " + serviceCode + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetServiceCode()            : KO "  );
			assertFalse( true) ;
		}
	}	
	
	@Test
	public void testSetServiceCode2() {
		
		Card a = null ;
		String returnedValue = "" ; 
		String serviceCode = "2014";
		
		try{
			a = new Card( panNumber );
			a.setServiceCode(serviceCode) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetServiceCode2()           : OK " +  panNumber + " - " + serviceCode + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetServiceCode2()           : KO " +  panNumber + " - " + serviceCode + " - " + returnedValue );
			assertFalse( true) ;
		}
	}	
	@Test
	public void testSetPvki() {
		
		Card a = null ;
		String returnedValue = "" ; 
		int pvki = 7 ;
		
		try{
			a = new Card( panNumber );
			a.setPvki(pvki);
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetPvki()                   : OK " +  panNumber + " - " + pvki + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetPvki()                   : KO " +  panNumber + " - " + pvki + " - " + returnedValue );
			assertFalse( true) ;
		}
	}	
	
	
	@Test
	public void testSetPvv() {
		
		String returnedValue="";
		String pvv = "000A" ; 
		try{
			card.setPvv(pvv) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetPvv()                    : OK " +  pvv + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetPvv()                    : KO " +  pvv + " - " + returnedValue );
			assertFalse( true) ;
		}
	}
	
	@Test
	public void testSetPvv2() {
		
		String returnedValue="";
		String pvv = "12345" ; 
		try{
			card.setPvv(pvv) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetPvv2()                   : OK " +  pvv + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetPvv2()                   : KO " +  pvv + " - " + returnedValue );
			assertFalse( true) ;
		}
	}
	
	@Test
	public void testSetOffset() {
		
		String returnedValue = "" ; 
		String offset  = "000A";
		try{
			card.setPin ("1234");
			card.setOffset(offset ) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetOffset()                 : OK pinlength " + card.getPinLength() + " - offset " +  offset + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetOffset()                 : KO pinlength " + card.getPinLength() + " - offset " +  offset + " - " + returnedValue );
			assertFalse( true) ;
		}
	}

	@Test
	public void testSetOffset2() {
		
		String returnedValue = "" ; 
		String offset  = "1111";
		try{
			card.setPin ("123499");
			card.setOffset(offset ) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (returnedValue.equals("") ){
			System.out.println("testSetOffset2()                : OK pinlength " + card.getPinLength() + " - offset " +  offset + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetOffset2()                : KO pinlength " + card.getPinLength() + " - offset " +  offset + " - " + returnedValue );
			assertFalse( true) ;
		}
	}
	
	@Test
	public void testSetPin() {
		
		Card a = null ;
		String returnedValue = "" ; 
		String pin  = "012F";
		try{
			a = new Card( panNumber );
			a.setPin(pin ) ;
		}
		catch ( CardException e ){
			returnedValue = e.getMessage() ;
		}	
		
		if (! returnedValue.isEmpty() ){
			System.out.println("testSetPin()                    : OK " +  pin + " - " + returnedValue );
			assertTrue( true) ;
		}else{
			System.out.println("testSetPin()                    : KO " +  pin + " - " + returnedValue );
			assertFalse( true) ;
		}
	}	
}
