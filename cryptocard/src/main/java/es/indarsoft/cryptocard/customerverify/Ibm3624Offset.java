package es.indarsoft.cryptocard.customerverify;

import es.indarsoft.cryptocard.symmetrickey.Pvk;
import es.indarsoft.cryptocard.types.DesKeyType;
import es.indarsoft.utl.Ascii;
import es.indarsoft.utl.Binary;
import es.indarsoft.utl.Des;
/**
 * Performs IBM3624 offset calculations.
 * 
 * @author fjavier.porras@gmail.com
 *
 */
public class Ibm3624Offset  {

	private static final int  BLK16  = 	16  ; //  (16 * 8 = 128 bits )
	private static final int  BLK08  = 	8   ; //  (8  * 8 = 64 bits )
	private Pvk pvk ;
	private byte[] 	decimalizationTable = new byte[16]; 
	/**
	 * Ibm3624Offset Constructor.
	 * <p>	
	 * @param pvk PVK Deskey 
	 * @param dectable	decimalization table binary array ( 16 length values in 0x00 to 0x09 ) 
	 * @throws CustomerVerifyException
	 */
	public Ibm3624Offset ( Pvk pvk,  byte[] dectable )  throws CustomerVerifyException {	
		
		for (int i=0; i < dectable.length; i++){
			if ( dectable[i] > 0x09 ) {
				throw new CustomerVerifyException("decimalizationTable must be 16 long, with values in 0x00 to 0x09");
			}
		}
		setState ( pvk, dectable  ) ;
	}	
	/**
	 * Ibm3624Offset Constructor.
	 * <p>
	 * @param pvk PVK Desky   	
	 * @param dectable	decimalization table ( 16 decimal digits ) 
	 * @throws CustomerVerifyException
	 */	
	public Ibm3624Offset (  Pvk pvk, String dectable )  throws CustomerVerifyException {
		
		byte[] abytearr = Ascii.string2byteArray(dectable);
		for (int i=0; i < abytearr.length; i++){
			if ( ! Ascii.isNumeric( abytearr[i]  ) ) {
				throw new CustomerVerifyException("decimalizationTable must be 16 long, with decimal digits");
			}
			abytearr[ i ] = (byte) ( abytearr[i] - 0x30 );
		}
		setState ( pvk, abytearr  ) ;
	}
	/**
	 * Save pvk and decimalization table values.
	 * <p>	
	 * @param pvk PVK Deskey
	 * @param dectable binary array
	 * @throws CustomerVerifyException
	 */
	private void setState( Pvk pvk, byte[] dectable ) throws CustomerVerifyException {
		
		for (int i=0; i < dectable.length; i++){
			this.decimalizationTable[i] = dectable[i] ;
		}
		this.pvk = pvk;
	}
	/**
	 * Generate customer pin for a given offset. 
	 * <p>
	 * <pre>
	 * - Calculate Natural Pin  
	 * - Compute ( Natural Pin + Offset  ) modulus 10 
	 * - pin --> Return previous result as string  
	 * </pre> 
	 * @param pinValidationData pin validation data
	 * @param offset input pan offset
	 * @return pin generated
	 * @throws Exception
	 */
	public String generatePin ( String pinValidationData, String offset ) throws Exception {
			
		String naturalPin = calculateNaturalPin( pinValidationData, offset.length() ) ;
		
		char[] charArray = new char[ offset.length() ] ; 
		
		for (int i = 0; i < offset.length(); i++){	// ( naturalPin + offset ) mod 10
			int nInt 	= Character.getNumericValue( naturalPin.charAt(i) ) ;
			int oInt 	= Character.getNumericValue( offset.charAt(i) ) ;
			int pInt 	= ( nInt + oInt ) % 10 ;   // modulus 10
			charArray[i] = Character.forDigit( pInt , 10 );
		}
		String pinNumber = new String(charArray);
		return pinNumber ;
	}
	/**
	 * Generate offset for a given Pin. 
	 * <p>
	 * <pre>
	 * - Calculate Natural Pin  
	 * - Subtract ( Customer Pin - Natural Pin ) modulus 10 
	 * - Offset --> Return previous result as string  
	 * </pre> 
	 * @param pinValidationData pin validation data
	 * @param pin input pin
	 * @return offset generated
	 * @throws Exception
	 */
	public String generateOffset ( String pinValidationData,  String pin  ) throws Exception {
		
		String naturalPin = calculateNaturalPin(  pinValidationData , pin.length() ) ;

		char[] charArray = new char[ pin.length() ] ; 
		
		for (int i = 0; i < pin.length(); i++){  
			int nInt 	= Character.getNumericValue( naturalPin.charAt(i) ) ;
			int pInt 	= Character.getNumericValue( pin.charAt(i) ) ;
			int oInt 	= ( 10 - nInt + pInt ) % 10 ;   // modulus 10
			charArray[i] = Character.forDigit( oInt , 10 );
		}
		String offset = new String(charArray);
		return offset ; 
	}
	
	public boolean verify ( String pinValidationData,  String offset , String pin ) throws Exception {	

		String generatedPin = generatePin (pinValidationData,  offset  ) ;
		
		if ( generatedPin.equals( pin ) ){
			return true  ;
		}else{
			return false ;
		}
	}	

	/**
	 * Natural Pin calculation.
	 * <p>
	 * <pre>
	 * - Validate Pin Validation Data
	 * - DES or Tdes of Pin Validation data (depending of Pvk length)
	 * - Take N-bytes (N=pin length/2) and decimalize this data
	 * - Natural Pin --> Return previous result as string  
	 * </pre> 	
	 * @param pinValidationData input pin validation data 
	 * @param pinLength	pin length 
	 * @return natural pin generated
	 * @throws Exception
	 */	
	public String calculateNaturalPin (  String pinValidationData ,  int pinLength  ) throws Exception, CustomerVerifyException {

		byte[] blkA	= new byte[BLK16];
		byte[] blkB	;
		//	
		for (int i=0; i < pinValidationData.length() ; i++){
			 blkA[i] = (byte) Character.getNumericValue( pinValidationData.charAt(i) ) ;
		} 
		
		isvalidPinValidationData(pinValidationData);
		/*
		 * 	Compress the builded block to 64 bits
		 */		
		blkB = Binary.compressBlock( blkA  ) ; 		
		
		/* Encrypt the blkB  */
		byte [] blkEncrypted = new byte[ BLK08 ];
	
		if ( pvk.getKeyType() == DesKeyType.SIMPLE ){
			blkEncrypted = Des.encrypt(blkB, pvk.getKey() );
		}else {
			blkEncrypted = Des.tdesEncrypt(blkB, pvk.getKey() ) ;
		}
			
		/* Decimalize the blkA  */
		byte [] blkDecimalized = decimalize ( blkEncrypted );	


		String returnedValue = Binary.toHexStr( blkDecimalized ) ;				
		
		return returnedValue.substring(0, pinLength ) ;
	}

	private byte[] decimalize (byte[] decimalizeArr ) {

		byte[] blkOUT		= new byte[ decimalizeArr.length ];
		for (int i = 0; i < decimalizeArr.length; i++){
			
			byte aInputByte 	=  	decimalizeArr[i];
			byte aOutputByte	= 	0x00 ;
			
			byte inputUpperNibble =  (byte) ( (aInputByte & 0xF0) >>> 4 );
			byte inputLowerNibble =  (byte) (aInputByte & 0x0F) ;

			byte outputUpperNibble =  this.decimalizationTable[ inputUpperNibble ] ;
			byte outputLowerNibble =  this.decimalizationTable[ inputLowerNibble ] ;
			
			aOutputByte = (byte) ( ( outputUpperNibble & 0x0F ) << 4 ) ;
			aOutputByte = (byte) (  aOutputByte | ( outputLowerNibble & 0x0F )  ) ;
			
			blkOUT[i] = aOutputByte ;
		}
		return blkOUT ;
	}

	/**
	 * Validate Pin Validation Data.
	 * <p>
	 * @param  pinvaldata Pin Validation Data input
	 * @return true or throws an exception
	 * @throws CustomerVerifyException
	 */
	private boolean isvalidPinValidationData (String pinvaldata ) throws CustomerVerifyException {
		
		if ( pinvaldata.length() != 16 ) {
			throw new CustomerVerifyException("isvalidPinValidationData: pinvaldata must be 16 hexadecimal characters (hex '0' to 'F')");
		}
		for (int i=0;i<pinvaldata.length();i++){
			char ch = pinvaldata.charAt(i) ;
			if ( ! Ascii.isNumericHex(ch) ) throw new CustomerVerifyException("isvalidPinValidationData: pinvaldata must be 16 hexadecimal characters (hex '0' to 'F')");
		}
		return true ; 
	}		
}