package com.indarsoft.cryptocard.utl;

import com.indarsoft.cryptocard.customerverify.CustomerVerifyException;
import com.indarsoft.utl.Ascii;
/**
 * Utilities for Pin Validation Data - IBM3624 Offset.
 *   
 * @author fjavier.porras@gmail.com
 *
 */
public class PinValidationData {

	/**
	 * Build a "customized" PIN validation data block conforming Thales 7000, from an input Pan Number. 
	 * <pre>
	 * Refer to Thales 7000 manual - 9.4 IBM PIN Offset (command code value 'DE' )
	 * - Computes Account Number : Takes the 12 right-most digits of the account number, excluding check digit.
	 * - Inserts the last 5 digits of the account number (previous data) in the position (pos).
	 * - Returns this data
	 * </pre>
	 * @param panNumber Pan Number from which to build Pin Validation Data
	 * @param index position to insert the last 5 digits of the account number 
	 * @return Pin Validation Data
	 * @exception CustomerVerifyException if a exception arrives
	 */
	public static String build7000(String panNumber, int index ) throws CustomerVerifyException {
		/*
		 * accountNumber : The 12 right-most digits of the account number, excluding the
		 * check digit.
		 */
		int length = panNumber.length();
		String accountNumber = panNumber.substring(length - 1 - 12 , length - 1);		
		if ( accountNumber.length() != 12 ) throw new CustomerVerifyException("build7000: accountNumber must be 12 right-most digits of the pan number");
		if ( index < 1 | index > 12 ) throw new CustomerVerifyException("build7000: index must be between 1 and 12");
		/*
		 * Build Pin Validation Data
		 */
		String last5digits =  accountNumber.substring( accountNumber.length() - 5 ) ; 
		String pinvaldat = accountNumber.substring(0, index - 1 ) + last5digits + accountNumber.substring( index ) ;
		return pinvaldat ;
	}
	/**
	 * Build a "customized" PIN validation data block conforming Thales 8000, from an input Pan Number. 
	 * <pre>
	 * Refer to Thales HSM 8000 Host Command Reference Manual - Generate an IBM PIN Offset (command code value 'DE' )
	 * - Takes characters from Pan Number starting at position sp and ending at ep ( 1 {@literal<}= sp {@literal<} ep {@literal<}= 15 ) 
	 * - Add pad character until a 16 characters length is completed.
	 * - Returns this data
	 * </pre>
	 * @param panNumber Pan Number from which to build Pin Validation Data
	 * @param sp start position from Pan Number
	 * @param ep end position from Pan Number  
	 * @param pad padding character
	 * @return Pin Validation Data
	 * @exception CustomerVerifyException if a exception arrives
	 */
	public static String build8000(String panNumber, int sp , int ep, char pad ) throws CustomerVerifyException {

		if ( sp < 1 | ep > 15 | sp >= ep ) throw new CustomerVerifyException("build8000: sp-start ep-end ( 1 <= sp < ep <= 15 ) "+sp+"-"+ep);
		if ( !Ascii.isNumericHex( pad)) throw new CustomerVerifyException("build8000: pad character must be hexadecimal "+pad);
		
		/*
		 * Build Pin Validation Data
		 */
		String pinvaldat = panNumber.substring(sp-1, ep )  ;
		for (int i=pinvaldat.length(); i<16;i++) pinvaldat = pinvaldat + pad ;
		return pinvaldat ;
	}	
}
