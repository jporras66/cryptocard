package com.indarsoft.cryptocard.pinblock;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.symmetrickey.Zpk;
import com.indarsoft.utl.Binary;
/**
 * ISO PIN Block Format 0  aka  ISO 9564-1 {@literal&} ANSI X9.8 format 0
 * aka  ISO-0 (same as ANSI X9.8, VISA-1, and ECI-1).
 * <pre>
 * See VISA - Payment Technology Standards Manual - 5.2.1 ISO PIN Block Format 0
 * 
 * ISO PIN Block Format 0 is used to build a PIN block from two strings that
 * consist of 16 hexadecimal digits each. These strings are formed from the PIN,
 * PIN length, certain PAN digits, and pad characters. 
 * 1. Build STRING-1 as follows:
 *	 a. Zero-fill the first position.
 *	 b. Enter the PIN length (one of the hexadecimal values from 4 to 6 ) in the second position.
 *	 c. Enter the PIN beginning in the third position.
 *	 d. Pad the remaining positions with hexadecimal Fs.
 * 2. Build STRING-2 as follows:
 *	 a. Zero-fill the first four positions.
 *	 Enter the rightmost 12 digits of the PAN (excluding the account
 *	 number check digit) in the remaining positions.
 *	 Account #4000 001 456 900 = 0000 4000 0014 5690
 *	 Account #4000 0012 3456 9002 = 0000 0001 2345 6900
 * 3. Combine STRING-1 and STRING-2 through the XOR operation.
 * </pre>	 
 * @author fjavier.porras@gmail.com
 *
 */
public class IsoPinBlockFormat0  extends PinBlock {
	
	/**
	 * IsoPinBlockFormat0 constructor.
	 * <p>
	 * @param card input card
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */
	protected IsoPinBlockFormat0 ( Card card, Zpk zpk ) throws Exception {
		
		super(card,zpk) ; 
		byte[] clearPinBlock = null ;
		clearPinBlock = compute ( card.getPanNumber(), card.getPin()  ) ;
		super.setClearPinBlock ( clearPinBlock ) ;
		super.computeEncriptedPinBlock(clearPinBlock, zpk);
	
	}
	/**
	 * IsoPinBlockFormat0 constructor.
	 * <p>
	 * @param encryptedPinBlock input decrypted pinblock 
	 * @param card input card
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */	
	protected IsoPinBlockFormat0 ( byte[] encryptedPinBlock, Card card, Zpk zpk ) throws Exception {
		
		super( encryptedPinBlock, card, zpk );
		super.computeClearPinBlock( encryptedPinBlock, zpk);
	
	}

	/**
	 * Compute pinblock ISO PIN Block Format 0
	 * <p> 
	 * @param panNumber input pan number
	 * @param pin	input pin
	 * @return pinblock byte array 
	 */
	private byte[] compute(String panNumber, String pin ) {
		
		byte padByteISO0 	= 0x0F ;
		int BLK16 = super.getBlk16();
		int BLK08 = super.getBlk08();
		byte[] blkA 	= new byte[BLK16];
		byte[] blkB 	= new byte[BLK16];
		byte[] blkAc 	= new byte[BLK08];
		byte[] blkBc 	= new byte[BLK08];
		byte[] blkXOR	= new byte[BLK08];

		//fill blkA
		int pinLength = pin.length() ;
		blkA[0] = 0x00 ;					//ISOFORMAT0
		blkA[1] = (byte) pinLength;
		
		for (int i=0; i < pinLength ; i++){
			blkA[2+i] = (byte) Character.getNumericValue( pin.charAt(i) ) ;
		}

		for (int i=2 + pinLength ; i < BLK16 ; i++){ //Padding Values
			blkA[i] = (byte) padByteISO0 ;		
		}	
		
		blkAc = Binary.compressBlock(blkA) ;
		//
		//blkB = Block.buildBlkB( panNumber, blkB );
		//fill blkB
		byte ZERO = super.getZero();
		blkB[0] = ZERO ;
		blkB[1] = ZERO;
		blkB[2] = ZERO;		
		blkB[3] = ZERO;
		
		String pan12RigthPosition = super.get12RigthPosition( panNumber );
		for (int i = 4 ; i < BLK16 ; i++ ){
			blkB[i] = (byte) Character.getNumericValue ( pan12RigthPosition.charAt(i-4) ) ;
		}		
		//
		blkBc = Binary.compressBlock(blkB) ;
			
		//  XOR(blkAc,blkBc) 
		for (int i=0 ; i < BLK08 ; i++){
			blkXOR[i] =  (byte) ( blkAc[i] ^ blkBc[i] ) ;
		}
		
		//System.out.println( "isoFormat0 clearpinblock is : "+ org.inds.utl.translate.Translate.toHex( blkXOR));
		return  blkXOR ;
	}
}
