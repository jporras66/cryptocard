package com.indarsoft.cryptocard.pinblock;

import com.indarsoft.cryptocard.card.Card;
import com.indarsoft.cryptocard.symmetrickey.Zpk;
import com.indarsoft.cryptocard.types.DesKeyType;
import com.indarsoft.cryptocard.types.PinBlockFormatType;
import com.indarsoft.utl.Binary;
import com.indarsoft.utl.Des;

/**
 * Computes ISO Pinblock calculation.
 * <p>
 * @author fjavier.porras@gmail.com
 *
 */
public class PinBlock {
	

	private static final int  BLK16  = 16  ;
	private static final int  BLK08  = 8   ; 
	private static final byte ZERO 	 = 0x00 ;

	@SuppressWarnings("unused") 
	private Zpk zpk;
	private PinBlockFormatType 	pinBlockFormatType ;
	private byte[]  			encryptedPinBlock ;
	private byte[]  			clearPinBlock ;
	/**
	 * PinBlock constructor Factory, builds an encrypted Pinblock for the card and zpk. 
	 * 
	 * @param card	input card
	 * @param zpk	input zpk
	 * @return	IsoPinBlockFormat0 or IsoPinBlockFormat3 depending on card pinblock format type
	 * @throws Exception if a exception arrives
	 */
	public static PinBlock getEncrypted ( Card card, Zpk zpk ) throws Exception {
		 
		if (card.getPinBlockFormatType() == PinBlockFormatType.ISOFORMAT0){
			return new IsoPinBlockFormat0 ( card , zpk ) ;
		}
		if (card.getPinBlockFormatType() == PinBlockFormatType.ISOFORMAT3){
			return new IsoPinBlockFormat3 ( card , zpk ) ;
		}
		return null ;
	}
	/**
	 * PinBlock constructor Factory, builds a decrypted Pinblock from an encrypted pinblock, card an zpk. 
	 * 
	 * @param encryptedPinBlock	input encrypted pinblock
	 * @param card	input card
	 * @param zpk	input zpk
	 * @return	IsoPinBlockFormat0 or IsoPinBlockFormat3 depending on card pinblock format type
	 * @throws Exception if a exception arrives
	 */	
	public static PinBlock getDecrypted ( String encryptedPinBlock, Card card, Zpk zpk ) throws Exception {
		
		byte[] blkA		= new byte[ encryptedPinBlock.length() ];
		byte[] blkB		= new byte[ BLK08 ];
		for (int i=0; i < encryptedPinBlock.length() ; i++){
			 blkA[i] = (byte) Character.getNumericValue( encryptedPinBlock.charAt(i) ) ;
		} 
		
		blkB = Binary.compressBlock( blkA );
		if (card.getPinBlockFormatType() == PinBlockFormatType.ISOFORMAT0){
			return new IsoPinBlockFormat0 ( blkB, card , zpk ) ;
		}
		if (card.getPinBlockFormatType() == PinBlockFormatType.ISOFORMAT3){
			return new IsoPinBlockFormat3 ( blkB, card , zpk ) ;
		}
		return null ;	
	}	
	/**
	 * Encrypted PinBlock constructor.
	 * <p>
	 * @param card	input card
	 * @param zpk	input zpk
	 */
	protected PinBlock ( Card card, Zpk zpk ) {
		this.zpk = zpk ;
		this.pinBlockFormatType = card.getPinBlockFormatType() ;
	}
	/**
	 * Decrypted PinBlock constructor.
	 * <p>
	 * @param encryptedPinBlock	input encrypted pinblock 
	 * @param card	input card
	 * @param zpk	input zpk
	 * @throws Exception if a exception arrives
	 */
	protected PinBlock ( byte[] encryptedPinBlock, Card card, Zpk zpk ) throws Exception {
		
		this.zpk = zpk ;
		this.pinBlockFormatType = card.getPinBlockFormatType() ;		
		this.encryptedPinBlock 	= encryptedPinBlock ;
	
	}
	
	protected void setClearPinBlock ( byte[] clearpinBlock ) {
		
		this.clearPinBlock		=  clearpinBlock  ; 
		
	}
	
	private void setEncryptedPinBlock ( byte[] encryptedPinBlock ) {

		this.encryptedPinBlock	=  encryptedPinBlock ;
		
	}
	
	public byte[] getEncryptedPinBlock() {
		
		return this.encryptedPinBlock ;
		
	}
	
	public String getEncryptedPinBlockasString() {
		
		String returnedValue = Binary.toHexStr(this.encryptedPinBlock);
		return returnedValue ;		
	}
		
	public byte[] getClearPinBlock() {
		
		return this.clearPinBlock ;
		
	}
	
	public String getClearPinBlockasString() {
		
		String returnedValue = Binary.toHexStr(this.clearPinBlock);
		return returnedValue ;		
	}	

	public PinBlockFormatType getPinBlockFormatType() {
		
		return this.pinBlockFormatType ;
		
	}

	
	/*protected String getPin(String panNumber, int pinLength) {
		
		byte[] blkB			= new byte[BLK16];
		byte[] blkPbl		= new byte[BLK16];
		byte[] blkXOR		= new byte[BLK16];
		byte[] blkOUT		= new byte[BLK08];
		
		blkB[0] = ZERO ;
		blkB[1] = ZERO;
		blkB[2] = ZERO;		
		blkB[3] = ZERO;
		
		String pan12RigthPosition = get12RigthPosition( panNumber );
		for (int i = 4 ; i < BLK16 ; i++ ){
			blkB[i] = (byte) Character.getNumericValue ( pan12RigthPosition.charAt( i - pinLength ) ) ;
		}
	
		blkPbl = Binary.uncompressBlock( encryptedPinBlock )  ;
		
		//  XOR(blkB, blkPbl) Gives blkA (PIN in bytes 2 to 3)
		for (int i=0 ; i < BLK16 ; i++){
			blkXOR[i] =  (byte) ( blkB[i] ^ blkPbl[i] ) ;
		}
		
		blkOUT = Binary.compressBlock( blkXOR )  ;
		
		String returnedValue = Binary.toHexStr(blkOUT);
		String returnedPin = returnedValue.substring(2, pinLength) ;
		return returnedPin ;
		
	}*/
	/**
	 * Compute clear pinblock.
	 * 
	 * @param encryptedPinBlock input encrypted pinblock
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */
	protected void computeClearPinBlock ( byte[] encryptedPinBlock, Zpk zpk ) throws Exception {
	
		byte[] blkA		= new byte[ encryptedPinBlock.length  ];
		for (int i=0; i < encryptedPinBlock.length ; i++){
			blkA[i] = encryptedPinBlock [i] ;
		} 
		
		/* Decrypt the previous result  */
		byte [] blkDecrypted = null ; 
		if ( zpk.getKeyType() == DesKeyType.SIMPLE) {
			blkDecrypted = Des.decrypt( blkA , zpk.getKey() );
		}else{
			blkDecrypted = Des.tdesDecrypt( blkA , zpk.getKey() );
		}
		
		setClearPinBlock( blkDecrypted ) ;
		
	}
	/**
	 * Compute encrypted pinblock.
	 * <p>
	 * @param clearPinBlock input clear pinblock
	 * @param zpk input zpk
	 * @throws Exception if a exception arrives
	 */
	protected void computeEncriptedPinBlock ( byte[] clearPinBlock, Zpk zpk ) throws Exception {
		
		byte[] blkA		= new byte[ clearPinBlock.length ];	
		for (int i=0; i < clearPinBlock.length ; i++){
			blkA[i] = clearPinBlock[i] ;
		} 
		
		/* Encrypt the previous result  */
		byte [] blkEncrypted = null ; 
		if ( zpk.getKeyType() == DesKeyType.SIMPLE) {
			blkEncrypted = Des.encrypt( blkA , zpk.getKey() );
		}else{
			blkEncrypted = Des.tdesEncrypt( blkA , zpk.getKey()) ;
		}
		
		setEncryptedPinBlock( blkEncrypted ) ;
		
	}
	
	protected String get12RigthPosition(String panNumber){
		int LENGHT = panNumber.length() ;
		return panNumber.substring( LENGHT - 12 - 1  , LENGHT - 1 ) ;
	}

	public static int getBlk16() {
		return BLK16;
	}

	public static int getBlk08() {
		return BLK08;
	}

	public static byte getZero() {
		return ZERO;
	}
	
}
