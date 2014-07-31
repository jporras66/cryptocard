package es.indarsoft.cryptocard.utl;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import es.indarsoft.utl.Ascii;
import es.indarsoft.utl.Binary;

public class Hmac {

	@SuppressWarnings("unused")
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
	
	public Hmac() {

	}
	
	public static byte[] HmacSHA1 ( String clearText , byte[] key ) throws Exception {
		
		byte[] blkIN = new byte[ clearText.length() ];
		for (int i = 0; i < clearText.length() ; i++){
			blkIN[i] = (byte) Character.getNumericValue( clearText.charAt(i) ) ;
		}
		 
		byte[] blkOUT = Ascii.compressBlock(blkIN);
		byte[] rawHmac = computeHmacSHA1 ( blkOUT , key ) ;
		return rawHmac ;

	}	


	
	public static byte[] get5BytesHmacSHA1 (  String clearText , String key ) throws Exception {
		
		byte[] rawHmac = computeHmacSHA1 ( clearText.getBytes() , key.getBytes() ) ;
		byte[] blkOUT = new byte[5];
		for (int i=0; i < 5; i++){
			blkOUT[i] = rawHmac[i];
		}
		return blkOUT ;

	}
	
/* ---------------------------------------------------------------------------------------------------- */
	
	
	private static byte[] computeHmacSHA1 (byte[] message, byte [] key ) throws Exception {
	       
		//SecretKeySpec keySpec = new SecretKeySpec( key , HMAC_SHA1_ALGORITHM );
		//SecretKeySpec keySpec = new SecretKeySpec( key  , "DESede" );
		
	
		String INSTANCE = "";
		if ( key.length == 8 ){
			INSTANCE = "DES";
		}else{
			INSTANCE = "DESede";
		}
		
		SecretKeySpec keySpec = new SecretKeySpec( key  , INSTANCE );
        Mac mac = Mac.getInstance( INSTANCE ); 
        mac.init(keySpec);
        
        System.out.println("Message is : " + Binary.toHexStr( message ) ) ;
        
        byte[] rawHmac = mac.doFinal( message );

        return rawHmac ;
        
	} 	 
	
}
