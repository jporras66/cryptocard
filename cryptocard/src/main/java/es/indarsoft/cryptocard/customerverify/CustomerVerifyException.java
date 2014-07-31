package es.indarsoft.cryptocard.customerverify;

public class CustomerVerifyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7559475613700140437L;

	public CustomerVerifyException (String msg ) {
		super(msg);
	}

	public CustomerVerifyException() {
		super("Unhandled Exception !!") ;
	}
}
