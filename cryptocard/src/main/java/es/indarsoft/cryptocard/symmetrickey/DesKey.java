package es.indarsoft.cryptocard.symmetrickey;

import es.indarsoft.cryptocard.types.DesKeyType;
import es.indarsoft.utl.Ascii;
import es.indarsoft.utl.Binary;
import es.indarsoft.utl.Des;
/**
 * Deskey
 *
 * @author fjavier.porras@gmail.com
 * 
 */
public class DesKey  {

	private static final int SIMPLE = 8 ;
	private static final int DOUBLE = 16 ;
	private static final int TRIPLE = 24 ;
	
	private DesKeyType  keyType ;
	private byte [] key  ;

    /**
	 * Deskey constructor
	 * <p>
	 * @param 	deskey input string value to build the key 
	 * @exception es.indarsoft.cryptocard.symmetrickey.DesKeyException
	 */	
	public DesKey(String deskey ) throws DesKeyException {
	
		byte[] abytearr = Ascii.string2byteArray( deskey );
		if ( ! isvalidKey(abytearr) ) throw new DesKeyException("Wrong Key - Simple DESKEY Must be 16 long Hexadecimal data !!") ;
		setEstate ( abytearr ) ;
	} 
    /**
	 * Deskey constructor
	 * <p>
	 * @param 	deskey input byte array to build the key 
	 * @exception es.indarsoft.cryptocard.symmetrickey.DesKeyException
	 */
	public DesKey( byte[] deskey  ) throws DesKeyException {
	
		if ( ! isvalidKey(deskey) ) throw new DesKeyException("aKeyEx1") ;
		setEstate ( deskey ) ;
	}
	
	private void setEstate( byte[] akey ) throws DesKeyException {
		
		int keyLength = akey.length ;
		if (  keyLength == SIMPLE*2  ) 	this.keyType = DesKeyType.SIMPLE ;			
		else 
		if (  keyLength == DOUBLE*2  )  this.keyType = DesKeyType.DOUBLE ;
		else
		if (  keyLength == TRIPLE*2  ) 	this.keyType = DesKeyType.TRIPLE ;
		else throw new DesKeyException("Wrong Key - DESKEY must be 16/32/48 long Hexadecimal data for SIMPLE/DOUBLE/TRIPLE keys !!");
//		
		this.key = Binary.compressBlock ( Ascii.getNumericValue( akey ) ) ;
	}
    /**
	 * Return the check value for as DES key
	 * <p>
	 * <pre>
	 * - Build ibmoff 8 bytes block filled of 0x00 --> BLK0
	 * - Encrypt this Block with the key (SINGLE or DOUBLE) --> KEY
	 * - Return 3 first bytes of DES(BLK0,KEY) or TDES(BLK0,KEY) 
	 * </pre>
	 * @return  check value byte array  (3 bytes)
	 * @exception es.indarsoft.cryptocard.symmetrickey.DesKeyException
	 */		
	public byte[] getCheckValue () throws DesKeyException  {
		
		byte[]  abytearr = null;
		byte[]  aBlk = new byte[ SIMPLE ] ; 		
		try{ 
	
		if ( getKeyType() == DesKeyType.SIMPLE ){  
			abytearr = Des.encrypt( aBlk , this.getKey() ) ;
		}else 
			{
				abytearr = Des.tdesEncrypt( aBlk , this.getKey() ) ;
			}	
		}catch ( Exception e ){
			throw new DesKeyException( e.getMessage() ) ;
		}
		byte[] check = new byte[3];
		for (int i=0;i<3;i++){
			check[i] = abytearr[i];
		}
		return check ;
	}
    /**
	 * Return 6 first digits of the check value for as DES key in ibmoff string format
	 * <p>
	 * @return  check value  (6 digits)
	 * @exception es.indarsoft.cryptocard.symmetrickey.DesKeyException
	 */	
	public String getCheckValueAsString () throws DesKeyException {
		
		byte[]  abytearr ;
		abytearr = getCheckValue() ;
		String checkValue = Binary.toHexStr( abytearr );
		return checkValue ;
	}
	
    /**
	 * Return an string representation of the key 
	 * <p>
	 * @return  DES key in string format
	 */	
	public String getKeyAsString () {
		
		String keyValue = Binary.toHexStr( this.key ) ;
		return keyValue ;
	}
	
	public byte[] getKey () {
		return this.key ;			
	}		
	
	public DesKeyType getKeyType () {
		return this.keyType ;				
	}
	
    /**
	 * Validates that ibmoff DesKey byte array has only hexadecimal values from 0x00 to 0x0F
	 * <p>
	 * 
	 * @param 	deskey input value 
	 * @return	byte[] ascii coded
	 */
	private boolean isvalidKey ( byte[] deskey ){
		
		return Ascii.isNumericHex( deskey );
		
	}
}
