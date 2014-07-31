package es.indarsoft.cryptocard.card;

public class CardException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6331897274979269949L;
	private String msg ;
	private String panNumberEx1 			= "Wrong panNumber - must be Decimal between 12 and 19 !!!!";
	private String panNumberEx2 			= "Wrong panNumber - check digit does not match !!";
	//private String panNumberEx3 			= "Wrong panNumber - must start 4 for VISA or 51-55 for MASTERCARD !!";
	private String expirationDateEx1 		= "Wrong expirationDate - must be 4 decimal digits (yymm or mmyy) !!";
	private String serviceCodeEx1 			= "Wrong serviceCode - must be 3 Decimal digits!!";
	private String pinBlockFormatTypeEx1 	= "Wrong pinBlockFormatType - must be ISOFORMAT0/ISOFORMAT3 !!";
	private String pvkiEx1 					= "Wrong pvki - must be Decimal between 1 and 6 !!";
	private String pvvEx1 					= "Wrong pvv - must be 4 Decimal digits!!";
	private String pinLengthEx1				= "Wrong pinLength - must be Decimal between 4 and 6 !!";
	private String pinValidationTypeEx1		= "Wrong pinValidationType - must be VISA_PVV/IBM_3624_OFFSET !!";
	private String offSetEx1 				= "Wrong Offset - must be Decimal same length than PIN Number !!";	

	public CardException (String msg ) {
		super(msg);
		if ( msg.equals("panNumberEx1") ){
			this.msg = panNumberEx1 ;	
		}else
		if ( msg.equals("panNumberEx2") ){
			this.msg = panNumberEx2 ;	
		}else
		/*if ( msg.equals("panNumberEx3") ){
			this.msg = panNumberEx3 ;	
		}else*/
		if (msg.equals("expirationDateEx1") ){
			this.msg = expirationDateEx1 ;			
		}else
		if (msg.equals("serviceCodeEx1") ){
			this.msg = serviceCodeEx1 ;			
		}else	
		if (msg.equals("pinBlockFormatTypeEx1") ){
			this.msg = pinBlockFormatTypeEx1 ;			
		}else			
		if (msg.equals("pvkiEx1") ){
			this.msg = pvkiEx1 ;			
		}else
		if (msg.equals("pvvEx1") ){
			this.msg = pvvEx1 ;			
		}else
		if (msg.equals("pinLengthEx1") ){
			this.msg = pinLengthEx1 ;			
		}else
		if (msg.equals("pinValidationTypeEx1") ){
			this.msg = pinValidationTypeEx1 ;			
		}else
		if (msg.equals("offSetEx1") ){
			this.msg = offSetEx1 ;			
		}else
		{
			this.msg = msg ;
		}
	}

	public CardException() {
		super("Unhandled Exception !!") ;
	}
	
	public String getMessage(){
		return msg ;
	}
}
