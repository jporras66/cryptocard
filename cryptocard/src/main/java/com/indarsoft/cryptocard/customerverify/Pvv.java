package com.indarsoft.cryptocard.customerverify;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.symmetrickey.Pvk;
import com.indarsoft.cryptocard.types.DesKeyType;
import com.indarsoft.utl.Binary;
import com.indarsoft.utl.Des;
/**
 * PVV performs pvv calculation and pin from a given pvv.    
 * <pre>
 * See VISA-Payment Technology Standards Manual - 6.5 Computing the PVV
 * The PVV computation process consists of the following steps:
 * 1.Build a 64-bit Transformed Security Parameter (TSP) from the PAN, PIN, and PVKI.
 * 2. Execute the PVV algorithm on the TSP and the PVK pair.
 * 	 2.1 Set the DES function to encrypt. Encrypt the TSP under PVK-A.
 *   2.2 Set the DES function to decrypt. Using PVK-B, decrypt the result of step 1.1
 *   2.3.Set the DES function to encrypt. Encrypt the result of step 2 under PKV-A.
 * 3. Select four numeric digits as the PVV from the resulting ciphertext.
 *    From the encrypted TSP, select the four decimal digits, as follows:
 *   3.1 Scan the encrypted TSP block from left to right, skipping any digits
 *       greater than nine, until the four decimal digits are found. If the four
 *        decimal digits can be obtained from this scan, those digits are the PVV.
 *       The computation process is complete.
 *   3.2 If, at the end of the scan, fewer than four decimal digits are selected, scan
 *       from left to right again, as follows:
 *       a. Scan only those digits greater than nine.
 *       b. Subtract 10 from the scanned digit.
 *       c. Select the result as a PVV digit.
 *       d. Continue the scan until all four PVV digits are selected.
 * </pre>
 * @author fjavier.porras@gmail.com   
 */
public class Pvv   {

	
	private static final int  BLK16  		= 	16  ; //  (16 * 8 = 128 bits )
	private static final int  BLK08  		= 	8   ; //  (8  * 8 = 64 bits )
	/**
	 * Computes pvv for a Card and pvk DES key.
	 * <p>
	 * @param card to compute his pvv
	 * @param pvk DES key
	 * @return pvv 4 decimal digits 
	 * @throws Exception if a exception arrives
	 */
    public static String compute ( Card card , Pvk pvk ) throws Exception {
    	
    	return compute( card.getPanNumber(), card.getPin(), card.getPvki() , pvk ) ;
    }
	/**
	 * Computes pvv for a given  pan number, pin number, dki and pvk.
	 * <pre>
	 * See VISA-Payment Technology Standards Manual - 6.4 Data Elements
	 * The following data elements are required to generate a PVV:
	 *  - Primary Account Number (PAN)
     *  - PIN associated with the PAN
	 *  - 1-digit PIN Verification Key Index (PVKI)
	 *  - A PVK 
	 * </pre>
	 * @param panNumber input pan number
	 * @param pinNumber input pin number
	 * @param dki 		input dki
	 * @param pvk		input DES pvk
	 * @return 			pvv 4 decimal digits
	 * @throws Exception if a exception arrives
	 */
	
    public static String compute ( String panNumber, String pinNumber, int dki , Pvk pvk) throws Exception {
	
		byte[] blkTSP		= new byte[BLK16];
		byte[] blkB			;
		
		int panLength = panNumber.length() ;
		String pan11RigthPosition = panNumber.substring(panLength - 11 - 1  , panLength - 1 ) ;
		int aCounter = 0 ;
		 for (int i=0; i < pan11RigthPosition.length() ; i++){
			 blkTSP[aCounter] = (byte) Character.getNumericValue( pan11RigthPosition.charAt(i) ) ;
			 aCounter ++ ;
		} 
		
		blkTSP[aCounter] = (byte) dki ;
		aCounter ++ ;

		/* 
		 * The PIN associated with the PAN is used to generate the PVV. The digits used
			are selected according to PIN length.
			When the PIN is four digits, the entire PIN is used to generate the PVV.
			When the PIN has more than four digits, only the leftmost four digits are used
			to generate the PVV.
		 */
		// only the leftmost four digits
		//
		for (int k = 0; k < 4 ; k++){			
			blkTSP[aCounter] = (byte) Character.getNumericValue( pinNumber.charAt(k) ) ;
			 aCounter ++ ;
		}
		
		blkB = Binary.compressBlock( blkTSP  ) ; 		
		
	
		/* Encrypt the TSP under PVK-A 	
		  Using PVK-B, decrypt the result of previous step 
		  Using PVK-A, encrypt the result of previous step */
				
		byte [] blkEncrypted = new byte[ BLK08 ];
		if ( pvk.getKeyType() == DesKeyType.SIMPLE ){
			blkEncrypted = Des.encrypt(blkB, pvk.getKey() ) ;
		}else {
			blkEncrypted = Des.tdesEncrypt(blkB, pvk.getKey() ) ;
		}
			
		/* 	Scan the encrypted TSP block from left to right, skipping any digits
		greater than nine, until the four decimal digits are found. If the four
		decimal digits can be obtained from this scan, those digits are the PVV.
		The computation process is complete */
				
		byte[] blkDigitsIN  = new byte[ BLK16 ];
		byte[] blkDigitsOUT = new byte[ BLK08 ];
		aCounter = 0;
		byte aByteA = 0, aByteB = 0 ;
		
		/* 	Scan the encrypted TSP block from left to right, skipping any digits
			greater than nine, until the four decimal digits are found. If the four
			decimal digits can be obtained from this scan, those digits are the PVV.
			The computation process is complete */
		
		for (int i=0; i < BLK08 ; i++){
			aByteA = (byte) ( (blkEncrypted[i] & 0xF0 ) >> 4 ); //Upper nibble
			aByteB = (byte) ( (blkEncrypted[i] & 0x0F ) );		//Lower nibble
			if ( aByteA >= 0x00 && aByteA < 0x0A ){
				blkDigitsIN [ aCounter ] = aByteA ;
				aCounter++;
			}
			if ( aByteB >= 0x00 && aByteB < 0x0A ){
				blkDigitsIN [ aCounter ] = aByteB ;
				aCounter++;
			}			
		} 

		/* 	If, at the end of the scan, fewer than four decimal digits are selected, scan
			from left to right again, as follows: 
				ibmoff. Scan only those digits greater than nine.
				b. Subtract 10 from the scanned digit.
				c. Select the result as ibmoff PVV digit.
				d. Continue the scan until all four PVV digits are selected. */
		
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
		String returnedValue =   resultDigitsOUT.substring(0, 4) ;
		return returnedValue ;
	}
    /**
     * Find a pin number (if exists) for a Card and DES pvk
     * <p>
     * @param card	input card
     * @param pvk	inpt DES pvk 
     * @return	pin value if found, or "N/F" in other case
     * @throws Exception if a exception arrives
     */
	public static String findPin ( Card card, Pvk pvk ) throws Exception {
		
		String panNumber 	= card.getPanNumber();
		int    pvki			= card.getPvki();
		String cardPvv 		= card.getPvv();
		return findPin( panNumber, cardPvv, pvki, pvk ) ; 
	}
	/**
	 * Find a pin number (if exists) for a given pan number, pvv, dki and DES pvk
	 * <p>
	 * @param panNumber input pan number
	 * @param pvv		input pvv
	 * @param dki		input dki
	 * @param pvk		input DES pvk
	 * @return pin value if found, or "N/F" in other case
	 * @throws Exception if a exception arrives
	 */
	public static String findPin ( String panNumber, String pvv, int dki , Pvk pvk ) throws Exception {
		
		String triedPin 	= "";	
		String foundPin 	= "N/F";
	
		for (int i = 0; i < 10000; i++){			
			String aVar 	= Integer.toString(i);			
			int k = Integer.toString(i).length() ;
			if ( k == 1 ){ 					//adding 0's on the left until length = 4
				triedPin = "000" + aVar;
			}else
			if ( k == 2 ) {
				triedPin = "00" + aVar;				
			}else
			if ( k == 3 ) {
				triedPin = "0" + aVar;	 				
			}else
			if ( k == 4 ) {
				triedPin = aVar;					
			}	
			String bPvv = compute ( panNumber , triedPin, dki , pvk ) ;
			if ( pvv.equals( bPvv)  ){
				foundPin = triedPin ;
				break ;
			}			
		}

		return foundPin  ; 	
	}
	
}
