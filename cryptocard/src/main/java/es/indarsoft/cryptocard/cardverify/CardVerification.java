package es.indarsoft.cryptocard.cardverify;

import es.indarsoft.cryptocard.card.Card;
import es.indarsoft.cryptocard.symmetrickey.DesKey;
import es.indarsoft.utl.Binary;
import es.indarsoft.utl.Des;
/**
 * CardVerification performs CVV/CVV2/ICVV verification.  
 * <p>
 * <ul>
 * <li>Issuers must not use the same verification keys for CVV and CVV2 as
 * those used for PIN Verification values (PVVs) with Visa’s PIN Verification
 * Service. If the common keys were to be compromised, it would affect both
 * the issuer’s PVVs and CVVs.
 * <li> CVV and CVV2 verification keys should be different from any other DES
 * keys used by the issuer. Because each issuer has unique keys, ibmoff breach of
 * security will be limited to ibmoff particular issuer rather than affecting all
 * issuers using the CVV
 * </ul>
 * @author fjavier.porras@gmail.com   
 */

public class CardVerification  {

	private static final int  BLK08  		= 	8   ; //  (8  * 8 = 64 bits )
	private static final int  BLK16  		= 	16  ; //  (16 * 8 = 128 bits )	
	private static final int  BLK24  		= 	24  ; //  (24 bytes for PAN + Expired Date + Service Code	
	private DesKey 	cvk	;
	private Card	card	;
	
	/**
	 * CardVerification constructor.
	 * <p>
	 * @param cvk DOUBLE deskey
	 * @param card input Card to be processed
	 */
	
	public CardVerification(DesKey cvk, Card card)  {
		this.cvk 	= cvk ;				// Same CVK pair for Cvv & ICvv
		this.card	= card;				// Another CVK pair for Cvv2
	}

	/**
	 * Gets cvv value, default date expiration format assumed as "YYMM".
	 * <p>
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */	

	public String getCvv () throws Exception {		
		
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate() , card.getServiceCode(), "YYMM" ) ;
	}
	/**
	 * Gets cvv value.
	 * <p>
	 * @param  DATEFORMAT for expiration date ( "YYMM" or "MMYY" )	
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */	
	public String getCvv ( String DATEFORMAT) throws Exception {		
		
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate() , card.getServiceCode(), DATEFORMAT ) ;
	}
	/**
	 * Gets cvv2 value, default service code "000" and date expiration format assumed as "YYMM".
	 * <p>
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */	
	public String getCvv2 ( ) throws Exception {		
		
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate(), "000" , "YYMM" ) ;
	}
	/**
	 * Gets cvv2 value, default service code "000".
	 * <p>
	 * @param  DATEFORMAT for expiration date ( "YYMM" or "MMYY" )	
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */
	public String getCvv2 ( String DATEFORMAT)  throws Exception {		
		
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate(), "000" , DATEFORMAT ) ;
	}
	/**
	 * Gets icvv value, default service code "999" and date expiration format assumed as "YYMM".
	 * <p>
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */		
	public String getIcvv ( ) throws Exception {
	
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate(), "999" , "YYMM" ) ;
	}
	/**
	 * Gets icvv value, default service code "999".
	 * <p>
	 * @param  DATEFORMAT for expiration date ( "YYMM" or "MMYY" )	
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */
	public String getIcvv ( String DATEFORMAT)  throws Exception {		
		
		return cvvAlgorithm ( card.getPanNumber(), card.getExpirationDate(), "999" , DATEFORMAT ) ;
	}
	
	/**
	 * Performs cvv calculation.
	 * <p>
	 * @param panNumber	 	Card pan number
	 * @param expiredDate	Card expiration date
	 * @param serviceCode	Card service code
	 * @param DATEFORMAT	expiration date Format ( "YYMM"-default or "MMYY" )
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */			
	private String cvvAlgorithm (String panNumber, String expiredDate, String serviceCode, String DATEFORMAT) throws Exception {
			
		String aExpiredDate = expiredDate ;
		if (DATEFORMAT.equals("MMYY")){
			String year  	= expiredDate.substring(2, 4) ;
			String month  	= expiredDate.substring(0, 2) ;
			aExpiredDate = year + month ;
		}
		String cvvValue = cvvAlgorithm(panNumber, aExpiredDate, serviceCode, cvk.getKey() ) ;
		return cvvValue ;
	}
	
	/**
	 * Performs cvv calculation.
	 * <p>
	 * @param panNumber	 	Card pan number
	 * @param expiredDate	Card expiration date
	 * @param serviceCode	Card service code
	 * @return cvv calculated
	 * @throws Exception	java.lang.Exception
	 */	
	private String cvvAlgorithm (String panNumber, String expiredDate, String serviceCode, byte [] desKey) throws Exception {

		byte[] blkA			= new byte[BLK24];
		byte[] blkB			;
		byte[] blkC			= new byte[BLK16];
		//
		byte[] blkCA		= new byte[BLK08];
		byte[] blkCB		= new byte[BLK08];
		
		int aCounter = 0 ;
		 for (int i=0; i < panNumber.length() ; i++){
			 blkA[aCounter] = (byte) Character.getNumericValue( panNumber.charAt(i) ) ;
			 aCounter ++ ;
		} 
		
		for (int j = 0 ; j < expiredDate.length() ; j++){
			 blkA[ aCounter ] = (byte) Character.getNumericValue( expiredDate.charAt(j) ) ;
			 aCounter ++ ;
		} 

		for (int k = 0; k < serviceCode.length() ; k++){
			 blkA[aCounter] = (byte) Character.getNumericValue( serviceCode.charAt(k) ) ;
			 aCounter ++ ;
		}
		
		blkB = Binary.compressRigthNibbles( blkA  ) ; 		
		for (int i=0; i < blkB.length; i++){
			blkC[i] = blkB[i];
		}

		for (int i=0; i < BLK08 ; i++){
			blkCA[i] = blkC[i];
		}
		
		aCounter = BLK08 ;
		for (int i=aCounter; i < BLK16 ; i++){
			blkCB[i - aCounter] = blkC[i];
		}
		
		/* Encrypt blkCA using Key A */
		byte [] blkEncrypted = new byte[ BLK08 ];
		
		byte [] keyA =  new byte [BLK08];  	//keyA for DES
		byte [] keyB =  new byte [BLK08];  	//keyB for DES		
		for (int i = 0; i < BLK08; i++ ){
			keyA[i] = (byte) desKey[i];
			keyB[i] = (byte) desKey[i+BLK08];
		}

		blkEncrypted = Des.encrypt(blkCA, keyA );
	
		/* Exclusive-OR (XOR) the result of previous step with Block blkCB. */ 
		byte [] blkXOR = new byte[ BLK08 ] ;
		for (int i=0 ; i < BLK08 ; i++){
			blkXOR[i] =  (byte) ( blkEncrypted[i] ^ blkCB[i] ) ;
		}

		blkEncrypted = Des.encrypt(blkXOR, keyA );

		/* Encrypt this value with Key A 		
		   Decrypt the previous result using Key B
		   Encrypt the previous result using Key A. */

		blkEncrypted = Des.decrypt( blkEncrypted, keyB );	
		blkEncrypted = Des.encrypt( blkEncrypted, keyA );
		
		/* Use the result of previous step and, beginning with the leftmost digit, extract all
		the digits from 0 through 9. Left-justify these digits in ibmoff 64-bit field. */
				
		byte[] blkDigitsIN  = new byte[ BLK16 ];
		byte[] blkDigitsOUT = new byte[ BLK08 ];
		aCounter = 0;
		byte aByteA = 0, aByteB = 0 ;
		
		for (int i=0; i < BLK08 ; i++){
			aByteA = (byte) ( (blkEncrypted[i] & 0xF0 ) >> 4 ); //Upper nibble
			aByteB = (byte) ( (blkEncrypted[i] & 0x0F ) );		//Lower nibble
			if ( aByteA >= 0x00 && aByteA <= 0x09 ){
				blkDigitsIN [ aCounter ] = aByteA ;
				aCounter++;
			}
			if ( aByteB >= 0x00 && aByteB <= 0x09 ){
				blkDigitsIN [ aCounter ] = aByteB ;
				aCounter++;
			}			
		}

		/* Use the result of previous step and, beginning with the leftmost digit, extract the
		hexadecimal digits from A to F. Then, convert each extracted digit to ibmoff
		decimal digit by subtracting 10. */
		
		for (int i=0; i < BLK08 ; i++){
			aByteA = (byte) ( (blkEncrypted[i] & 0xF0 ) >> 4 ); //Upper nibble
			aByteB = (byte) ( (blkEncrypted[i] & 0x0F ) );		//Lower nibble
			if ( aByteA >= 0x0A && aByteA <= 0x0F ){
				blkDigitsIN [ aCounter ] = (byte) (aByteA - 0x0A); 
				aCounter++;
			}
			if ( aByteB >= 0x0A && aByteA <= 0x0F ){
				blkDigitsIN [ aCounter ] = (byte) (aByteB - 0x0A) ;
				aCounter++;
			}			
		}		
		
		blkDigitsOUT = Binary.compressBlock ( blkDigitsIN ) ;
		String resultDigitsOUT = Binary.toHexStr(blkDigitsOUT);		
		String returnedValue = resultDigitsOUT.substring(0, 3);		
		return returnedValue ; 
	}	
}
