package com.indarsoft.cryptocard.card;

import com.indarsoft.cryptocard.types.PinBlockFormatType;
import com.indarsoft.cryptocard.types.PinValidationType;
import com.indarsoft.cryptocard.utl.CheckDigit;

/**
 * Card
 * 
 * @author fjavier.porras@gmail.com
 * 
 */
public class Card {

	/**
	 * A valid pan number must have 12 to 19 decimal digits.
	 */
	private String panNumber;
	/**
	 * Expiration Date holds year and month when the card1 will be expired.
	 */
	private String expirationDate;
	/**
	 * Valid expiration date formats are "YYMM"-default and "MMYY"
	 */
	private String expirationDateFormat = "YYMM";
	private String serviceCode;
	private PinBlockFormatType pinblockFormatType;
	/**
	 * V --> VISA 
	 * M --> MASTERCARD
	 * O --> OTHER
	 */	
	private char cardBrand; 
	private int pvki;
	private String pvv;
	private String offset;
	/**
	 * In our Pin implementation, it must be Decimal between 4 to 6 digits.
	 * <p>
	 * <pre>
	 * For verification purposes in interchange transactions, the maximum PIN length is six digits.
	 * An issuer can elect to support longer PINs up to ibmoff maximum of 12 digits as
	 * specified in ISO 9564. However, ATM acquirers are not obligated to
	 * support PINs of more than six digits.
	 * </pre>
	 */
	private String pin;
	private int pinLength;
	private PinValidationType pinValidationType;


	/**
	 * Card constructor.
	 * <p>
	 * @param panNumber input string value       
	 * @exception com.indarsoft.cryptocard.card.CardException
	 */
	public Card(String panNumber) throws CardException {

		setPanNumber(panNumber);
	}

	public char getCardBrand() {
		return cardBrand;
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public String getOffset() {
		return offset;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public String getPin() {
		return pin;
	}

	public PinBlockFormatType getPinBlockFormatType() {
		return pinblockFormatType;
	}

	public int getPinLength() {
		return pinLength;
	}

	public PinValidationType getPinValidationType() {
		return pinValidationType;
	}

	public int getPvki() {
		return pvki;
	}

	public String getPvv() {
		return pvv;
	}

	public String getServiceCode() {
		return serviceCode;
	}
	
	public String getExpirationDateFormat() {
		return expirationDateFormat;
	}
	
	/**
	 * Sets expiration date for the card1, default date format is "YYMM".
	 * @param expirationDate for the card1
	 * @throws CardException
	 */
	public void setExpirationDate(String expirationDate) throws CardException {

		if (!validateisNumericAndLength(expirationDate, 4)) throw new CardException("expirationDateEx1");
		int month = Integer.parseInt(expirationDate.substring(2, 4));
		if (month < 1 | month > 12) throw new CardException("expirationDateEx1");
		this.expirationDate = expirationDate;

	}
	/**
	 * Sets expiration date for the card1
	 * @param expirationDate for the card1
	 * @param DATEFORMAT if is "MMYY" the expiration date format is stored in this format "MMYY", 
	 * in other case is stored in "YYMM" format.
	 * @throws CardException
	 */
	public void setExpirationDate(String expirationDate, String DATEFORMAT) throws CardException {

		if (DATEFORMAT.equals("MMYY")){
			if (!validateisNumericAndLength(expirationDate, 4)) throw new CardException("expirationDateEx1");
			int monthValue = Integer.parseInt( expirationDate.substring(0, 2) );
			if (monthValue < 1 | monthValue > 12) throw new CardException("expirationDateEx1");
			this.expirationDate = expirationDate;
			this.expirationDateFormat = "MMYY";
		}else{
			setExpirationDate( expirationDate );
		}
	}
	/**
	 * Sets offset value for the card1 , also sets pin length ( must be Decimal between 4 to 6 digits).
	 * @param offset value
	 * @throws CardException
	 */
	public void setOffset(String offset) throws CardException {

		if (offset.length() == 0) throw new CardException("offSetEx1");
		else
		if (!validateisNumericAndLength(offset, offset.length())) throw new CardException("offSetEx1");
		else{		
			this.offset = offset;
			setPinLength(offset.length());
		}
	}
	/**
	 * Sets Pan Number for the card1
	 * @param panNumber for the card1
	 * @throws CardException
	 */
	private void setPanNumber(String panNumber) throws CardException {

		isvalidPanNumber(panNumber);
		validateCardBrand(panNumber);
		this.panNumber = panNumber;
	}
	/**
	 * Sets the pin and pin length for ibmoff card1, that must be Decimal between 4 to 6 digits
	 * @param  pin for the card1
	 * @throws CardException
	 */
	public void setPin(String pin) throws CardException {

		int pinLength = pin.length();
		if (!validateisNumericAndLength(pin, pinLength)) throw new CardException("pinLengthEx1");
		this.pin = pin;
		setPinLength(pinLength);
	}
	/**
	 * Sets pin block format type for the card1
	 * @param s
	 * @throws CardException
	 */
	public void setPinBlockFormatType(PinBlockFormatType s) throws CardException {

		if (s.equals(PinBlockFormatType.ISOFORMAT0)) this.pinblockFormatType = PinBlockFormatType.ISOFORMAT0;
		else 
		if (s.equals(PinBlockFormatType.ISOFORMAT3)) this.pinblockFormatType = PinBlockFormatType.ISOFORMAT3;
		else 
		throw new CardException("pinBlockFormatTypeEx1");
	}
	/**
	 * Sets the pin length for ibmoff card1, that must be between 4 to 6 digits
	 * @param  pinLength
	 * @throws CardException
	 */
	private void setPinLength(int pinLength) throws CardException {

		if (pinLength < 4 || pinLength > 6) throw new CardException("pinLengthEx1");
		this.pinLength = pinLength;
	}

	public void setPinValidationType(PinValidationType s) throws CardException {

		if (s.equals(PinValidationType.VISA_PVV)) {
			this.pinValidationType = PinValidationType.VISA_PVV;
		} else 
		if (s.equals(PinValidationType.IBM_3624_OFFSET)) {
			this.pinValidationType = PinValidationType.IBM_3624_OFFSET;
		} else {
			throw new CardException("pinValidationTypeEx1");
		}

	}
	/**
	 * Sets pvki to be assigned to this card1 (between 1 to 6), this is the dki index value for the PVK to be used. 
	 * @param pvki value
	 * @throws CardException
	 */
	public void setPvki(int pvki) throws CardException {

		if (pvki < 1 || pvki > 6) throw new CardException("pvkiEx1");
		this.pvki = pvki;
	}
	/**
	 * Sets pvv to be assigned to this card1 ( 4 decimal digits ), and also pin length (same value) 
	 * @param pvv value
	 * @throws CardException
	 */
	public void setPvv(String pvv) throws CardException {

		if (!validateisNumericAndLength(pvv, 4)) throw new CardException("pvvEx1");
		this.pvv = pvv;
		setPinLength(pvv.length());
	}
	/**
	 * Sets service code to be assigned to this card1 ( 3 decimal digits ). 
	 * @param serviceCode value
	 * @throws CardException
	 */
	public void setServiceCode(String serviceCode) throws CardException {

		if (!validateisNumericAndLength(serviceCode, 3)) throw new CardException("serviceCodeEx1");
		this.serviceCode = serviceCode;
	}
    /**
	 * Validate and sets card1 brand. 
	 * <p>
	 * <ul>
	 * <li>If starts with 4->VISA
	 * <li>if starts in range '51' to '55'->Mastercard
	 * <li>In other case -> Other
	 * </ul>
	 * @param 	panNumber pan number for the card1 
	 * @exception com.indarsoft.cryptocard.symmetrickey.DesKeyException
	 */
	private void validateCardBrand(String panNumber) throws CardException {

		int firtsDigit   = Integer.parseInt(panNumber.substring(0, 1));
		int firts2Digits = Integer.parseInt(panNumber.substring(0, 2));
		if (firtsDigit == 4) {
			this.cardBrand = 'V'; // VISA
		} else if (firts2Digits >= 51 && firts2Digits <= 55) {
			this.cardBrand = 'M'; // MASTERCARD
		} else {
			this.cardBrand = 'O'; // Other
		}
	}
	/**
	 * Validates that input string <b>st</b> has length <b>length</b>, and <b>st</b> has only decimal numerical values 
	 * @param st input value
	 * @param length to check 
	 * @return boolean 
	 */
	private static boolean validateisNumericAndLength(String st, int length) {
		byte aByte = 0x00;
		if (st.length() != length) {
			return false;
		}
		for (int i = 0; i < st.length(); i++) {
			aByte = (byte) Character.getNumericValue(st.charAt(i));
			if (aByte > 0x09) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Validation for ibmoff Pan Number. 
	 * <p>
	 * <ul>
	 * <li> Pan number must be between 12 and 19 decimal characters
	 * <li> check digit must be OK (Luhn Algorithm)
	 * <li> In other case an CardException is generated  
	 * </ul>
	 * @param panNumber input string value       
	 * @exception com.indarsoft.cryptocard.card.CardException
	 */	
	private boolean isvalidPanNumber(String panNumber) throws CardException{

		int panNumberLength = panNumber.length();
		if (panNumberLength < 12 || panNumberLength > 19) throw new CardException("panNumberEx1");
		if (!validateisNumericAndLength(panNumber, panNumberLength)) throw new CardException("panNumberEx1");

		boolean returnedValue = CheckDigit.validate(panNumber);
		if (!returnedValue) {
			String checkdigit = CheckDigit.calculate(panNumber.substring(0, panNumberLength - 1));
			String value =  "Wrong panNumber - check digit does not match, check digit is : " + checkdigit;
			throw new CardException( value );
		}
		return true ;
	}

}
