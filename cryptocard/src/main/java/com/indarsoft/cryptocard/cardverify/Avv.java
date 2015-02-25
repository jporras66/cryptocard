package com.indarsoft.cryptocard.cardverify;

import com.indarsoft.cryptocard.symmetrickey.DesKey;
import com.indarsoft.cryptocard.types.AvvType;
import com.indarsoft.cryptocard.utl.Hash;
import com.indarsoft.utl.Binary;

/**
 * <i><b>TO BE COMPLETED</b></i> 
 * 
 * @author fjavier.porras@gmail.com
 */
public class Avv  {
	/**
	 * controlByte:
	 * The control byte is used to indicate the format and content of the associated AAV structure. The following hexadecimal values have been defined for MasterCard�s implementation of 3-D Secure:
		x'8C' for an AAV created as the result of ibmoff successful cardholder authentication.
		x'86' for an AAV created as the result of Attempts processing
		
			Length 		--> 1
			Byte Number	--> Byte 1
							
		merchantHashName : 	The left most 8 bytes of the SHA-1 hash of the Merchant Name field from the PAReq.
		
			Length 		--> 2
			Byte Number	--> Bytes 2-9
		
		acsIdentifier :		Allows an issuer to use up to 256 different ACS facilities.
			Values for this field are defined based on the algorithm used to create the MAC:
			0 � 7 Reserved for HMAC
			8 � 15 Reserved for CVC2
			16 � 255 � Reserved for future use	
			
			Length 		--> 1
			Byte Number	--> Byte 10		
			
		authenticationMethod: Indicates how the cardholder was authenticated to the ACS:
			0 = No Cardholder Authentication Performed (This is only valid for an AAV created using control byte value x�86� � Attempts processing.)
			1 = Password
			2 = Secret Key (e.g. Chip Card)
			3 = PKI (pending further discussions)		

			Length 		--> 1/2 (4 bits) 
			Byte Number	--> Byte 11	(firt digit)			
			
		binKeyIdentifier :	Indicates which one of the possible 16 issuer-known secret keys for ibmoff given BIN range was used by the ACS identified by the ACS identifier to create the MAC.

			Length 		--> 1/2 (4 bits) 
			Byte Number	--> Byte 11	(second digit)
			
		transactionSequenceNumber :	Unique number that can be used to identify the transaction within the ACS identified by the ACS Identifier field.
			Once the maximum value has been reached, the number must recycle back to 0.		
			
			Length 		--> 4 
			Byte Number	--> Byte 12-15

		MAC :	Message Authentication Code, created by ACS.		
			
			Length 		--> 5 
			Byte Number	--> Byte 16-20							

	 */
	private static final byte AUTHENTICATED = (byte) 0x8C ;
	private static final byte ATTEMP		= (byte) 0x86 ;
	private byte binKeyId ; 
	private byte formatVersionNumber ;
	private byte macAlgorithmId ;			// 0x00-0x07 --> HMAC algorithm for MAC  	
											// 0x08-0x0F --> CVC2 algorithm for MAC
	private DesKey cvk ;
	
	@SuppressWarnings("unused")
	private boolean isHmac = false ;
	
	@SuppressWarnings("unused")
	private boolean isCvc2 = false ;
	
	@SuppressWarnings("unused")
	private byte authenticationMethod ;
 
	
	public Avv(DesKey cvk, int binKeyId,  AvvType.FormatVersionNumber formatVersionNumber, AvvType.MacAlgorithmType macAlgorithmId )  {
		
		this.setCvk(cvk) ;
		
		if ( binKeyId < 0 || binKeyId > 15) {
			this.binKeyId = (byte) 0x00 ;
		}else{
			this.binKeyId = (byte) binKeyId ;
		}
			
		if ( formatVersionNumber == AvvType.FormatVersionNumber.AUTHENTICATED ) {
			this.formatVersionNumber 	= AUTHENTICATED ;	//This means AVV-Authenticated
			this.authenticationMethod	= (byte) 0x01 ; 	//1 = Password
		}else{
			this.formatVersionNumber	= ATTEMP ;			//This means AVV-Attemp
			this.authenticationMethod	= (byte) 0x00 ; 	//0 = No Cardholder Authentication
		}
		
		//if ( macAlgorithmId >= 0 && macAlgorithmId <= 7){
		if ( macAlgorithmId == AvvType.MacAlgorithmType.HMAC ){
			isHmac = true ;
			this.macAlgorithmId = (byte) 0x07 ;
		}else
		if ( macAlgorithmId == AvvType.MacAlgorithmType.CVC2 ){ 
			isCvc2 = true ;
			this.macAlgorithmId = (byte) 0x07 ;
		}else{
			// TO BE COMPLETED
		}
	}
	
	public byte getFormatVersionNumber(){
		return this.formatVersionNumber ;
	}
	

	public byte[] compute (	String 	panNumber, String merchantName, String transactionSequenceNumber  )  
	{		
		int aCounter = 0 ;
		byte[] aBlka 	= new byte[15] ;
		
		// Add formatVersionNumber to avv
		aBlka[aCounter] = this.formatVersionNumber ;
		aCounter ++ ;
		
		// Add Merchant Hash Name
		byte[] hashedMerchantName = hashMerchantName(merchantName) ;
		for (int i=0; i < hashedMerchantName.length; i++){
			aBlka[aCounter] = hashedMerchantName[i];
			aCounter ++ ;
		}
		
		// Add macAlgorithmId to avv
		aBlka[aCounter] = this.macAlgorithmId ;
		aCounter ++ ;
		
		// Add Authentication Method to avv (upper half byte) and BIN Key Identifier (lower half byte)
		byte aByte 		 = 0x00 ;
		byte upperNibble = 0x00 ;
		byte lowerNibble = 0x00 ;
		
		upperNibble 	= (byte) ( (this.macAlgorithmId << 4 ) & 0xF0 ) ;
		lowerNibble 	= (byte) ( (this.binKeyId ) & 0x0F ) ;
		aByte			= (byte) ( upperNibble | lowerNibble );  // Add upper and lower (OR Operator)
		
		aBlka[aCounter] 	= aByte ;
		aCounter ++ ;

		// Add Transaction Sequence Number
		byte [] aBlkb = new byte[ transactionSequenceNumber.length() ];
		for (int i=0; i <  transactionSequenceNumber.length() ; i++){
			aBlkb[i] = (byte) Character.getNumericValue( transactionSequenceNumber.charAt(i) ) ;
		}
		byte [] aBlkOUT = Binary.compressBlock(aBlkb) ;
		
		for (int i=0; i < aBlkOUT.length ; i++){
			aBlka[aCounter] = aBlkOUT[i];
			aCounter ++ ;
		}
		
		// Build Block for MAC
		int bCounter = 0 ;
		byte[] aBlkc	= new byte[20] ;
		for (int i=0; i <  panNumber.length() ; i++){
			aBlkc[i] = (byte) Character.getNumericValue( panNumber.charAt(i) ) ;
			bCounter++ ;
		}
		for (int i=bCounter; i <  20 ; i++){
			aBlkc[i] = (byte) 0x0F ;
			bCounter++ ;
		}
		byte [] aBlkcOUT = Binary.compressBlock(aBlkc) ;
		
		//System.out.println("aBlkcOUT is : " + Binary.toHexStr( aBlkcOUT ) ) ;
		
		byte [] aBlkfMac = new byte[25];
		for (int i=0; i <  aBlkcOUT.length  ; i++){
			aBlkfMac [i] = aBlkcOUT[i] ;
		}
		
		for (int i=0; i <  aBlka.length  ; i++){
			aBlkfMac [i + aBlkcOUT.length ] = aBlka[i] ;
		}	
		
		//System.out.println("aBlka       is : " + Binary.toHexStr( aBlka ) ) ;
		//System.out.println("aBlkfMac    is : " + Binary.toHexStr( aBlkfMac ) ) ;
		
		return aBlka ;
		
	}
	
	public byte[] cvc2(){
		
		byte[] cvc2 = new byte[5] ;
		
		return cvc2;
		
	}
	
	
	private byte[] hashMerchantName( String merchantName ){
		
		byte[] hashedMerchantName = new byte[8];
		try{
			hashedMerchantName = Hash.get8BytesDigestSHA1( merchantName ) ;	
		}catch (Exception e){
			return hashedMerchantName ;
		}
						
		return hashedMerchantName ;
	}

	public DesKey getAvvCvk() {
		return cvk;
	}

	public void setCvk(DesKey cvk) {
		this.cvk = cvk;
	}
	
}
