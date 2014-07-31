package es.indarsoft.cryptocard.symmetrickey;

public class DesKeyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8029564933566258445L;
	private String msg ;
	
	public DesKeyException (String msg ) {
		super(msg);
		this.msg = msg ;
	}

	public DesKeyException() {

		super("Unhandled KeyException !!") ;
	}
	
	public String getMessage(){
		return msg ;
	}
}
