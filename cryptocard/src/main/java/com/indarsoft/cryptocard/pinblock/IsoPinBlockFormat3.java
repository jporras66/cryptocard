package com.indarsoft.cryptocard.pinblock;


import java.util.Random;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.symmetrickey.Zpk;
import com.indarsoft.utl.Binary;

/**
 * ISO PIN Block Format 3. 
 * <pre>
 * See VISA - Payment Technology Standards Manual - 5.2.4 ISO PIN Block Format 3
 * 
 * The ISO PIN Block Format 3 is the same as the ISO PIN Block Format 0
 * except for the fill digits. In ISO PIN Block Format 3, the fill digit is 4-bit
 * field, with values from 1010 (10) to 1111 (15), where the fill-digit values are
 * randomly or sequentially selected from this set of six possible values, such
 * that it is highly unlikely that the identical configuration of fill digits will be
 * used more than once with the same account number field by the same PIN
 * encipherment device.
 * ISO format 3 should be used for encryption zones where the PIN encryption
 * key is static for the productive life of the device in which it resides.
 * </pre>
 * @author fjavier.porras@gmail.com
 *
 */
public class IsoPinBlockFormat3  extends PinBlock {
	
	/**
	 * IsoPinBlockFormat3 constructor.
	 * <p>
	 * @param card input card
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */	
	protected IsoPinBlockFormat3 ( Card card, Zpk zpk ) throws Exception {
		
		super(card,zpk) ; 
		byte[] clearPinBlock = null ;
		clearPinBlock = compute ( card.getPanNumber(), card.getPin()  ) ;
		super.setClearPinBlock ( clearPinBlock ) ;
		super.computeEncriptedPinBlock(clearPinBlock, zpk);
	
	}
	/**
	 * IsoPinBlockFormat3 constructor.
	 * <p>
	 * @param encryptedPinBlock input decrypted pinblock 
	 * @param card input card
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */
	protected IsoPinBlockFormat3 ( byte[] encryptedPinBlock, Card card, Zpk zpk ) throws Exception {
		
		super( encryptedPinBlock, card, zpk );
		super.computeClearPinBlock( encryptedPinBlock, zpk);
	
	}

	/**
	 * Compute pinblock ISO PIN Block Format 3
	 * <p> 
	 * @param panNumber input pan number
	 * @param pin	input pin
	 * @return pinblock byte array 
	 */
	private byte[] compute (String panNumber, String pin ) {
		
		byte[] padByteISO3  =  	new byte[] { (byte) 0x0A, (byte) 0x0B, (byte) 0x0C, (byte) 0x0D, (byte) 0x0E, (byte) 0x0F } ;
		
		int BLK16 = super.getBlk16();
		int BLK08 = super.getBlk08();
		byte[] blkA 	= new byte[BLK16];
		byte[] blkB 	= new byte[BLK16]; 	
		byte[] blkXOR	= new byte[BLK16];
		byte[] blkOUT	= new byte[BLK08];

		//fill blkA
		int pinLength = pin.length() ;
		blkA[0] = 0x00 ;					//ISOFORMAT3
		blkA[1] = (byte) pinLength;
		
		for (int i=0; i < pinLength ; i++){
			blkA[2+i] = (byte) Character.getNumericValue( pin.charAt(i) ) ;
		}

		Random rand = new Random();
		for (int i=2 + pinLength ; i < BLK16 ; i++){ 			//Padding Values
			int x = rand.nextInt( padByteISO3.length - 1 );  	//Assing padding value randomly from padByteISO3
			blkA[i] = (byte) padByteISO3[x];					
		}

		
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
		
		//  XOR(blkA,blkB) 
		for (int i=0 ; i < BLK16 ; i++){
			blkXOR[i] =  (byte) ( blkA[i] ^ blkB[i] ) ;
		}
		
		// blkOUT (PINBLOCK) 
		blkOUT = Binary.compressBlock ( blkXOR ) ;
		return  blkOUT ;
	}	

}
