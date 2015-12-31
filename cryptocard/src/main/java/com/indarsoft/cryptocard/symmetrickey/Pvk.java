package com.indarsoft.cryptocard.symmetrickey;
/**
 * Pvk Pin Verification key.
 * <pre>
 * 6.4.4 PIN Verification Key (PVK) Pair
 * The PVK pair consists of two odd-parity DES keys (DOUBLE length) . 
 * The two keys should not be identical. Greater security is provided when each key is a unique value.
 * 
 * 6.4.3 PIN Verification Key Index (PVKI)
 * The PVKI is a 1-digit value that identifies which pair of keys is to be used for
 * the PVV computation. The PVKI is encoded on the magnetic stripe as part of
 * the PIN Verification field or stored in an online database.
 * A PVKI can be any value between zero and six.
 * A zero indicates that the PIN cannot be verified through the PIN Verification
 * Service (PVS). If the issuer specified to Visa that the PVS is to be performed
 * by Visa for a specified account range, and an individual card has a PVKI of
 * zero, Visa will decline transactions with PINs for that card. If the PVKI is
 * zero, the PVV need not be generated unless the issuer elects to do so for on-us
 * transaction use.
 * A PVKI value between 1 and 6 identifies the appropriate PVK pair.
 * </pre>
 * @author fjavier.porras2gmail.com
 *
 */
public class Pvk extends DesKey {

	private int pvki ;
	/**
	 * PVK constructor
	 * @param aKey	DES key double length.
	 * @param pvki	pvki
	 * @throws DesKeyException if a exception arrives
	 */
	public Pvk (String aKey ,  int pvki  ) throws DesKeyException  {
		
		 super(aKey );
		 if ( ! validatePvki ( pvki ) ) throw new DesKeyException("Wrong Key - pvki must be Decimal between 1 and six !!") ;
		 /*
		  * Do not enforce this, because PVK is also used for IBM3264offset method
		  */
		 //if ( aKey.length() != 32 )  	throw new DesKeyException("Wrong Key - DOUBLE deskey Must be 32 long Hexadecimal data!!");
		 this.pvki = pvki ;
	}
	/**
	 * PVK constructor with default pvki = 1.
	 * @param aKey	DES key double length
	 * @throws DesKeyException if a exception arrives
	 */
	public Pvk (String aKey  ) throws DesKeyException  {
		
		 this (aKey, 1 );
	}
	
	public int getPvki () {
		return this.pvki ;				
	}
	
	private static Boolean validatePvki ( int pvki ){
		
		if ( pvki < 1 || pvki > 6 ){
			return false ;	
		}
		return true ;
	}		
	
}
