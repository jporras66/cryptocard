package es.indarsoft.cryptocard.utl;

import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;

public class CheckDigit {

	public static boolean validate ( String panNUmber){
		
		LuhnCheckDigit   luhn = new LuhnCheckDigit();
		return luhn.isValid ( panNUmber ) ;	
		
	}
	
	public static String calculate ( String panNUmber){
		
		LuhnCheckDigit   luhn = new LuhnCheckDigit();
		String checkdigit;
		try {
			checkdigit = luhn.calculate(  panNUmber );
		} catch (CheckDigitException e) {
			checkdigit = "CheckDigit.calculate : "+ e.getMessage() ;
			System.out.println(checkdigit);
		}
		return checkdigit ;	
		
	}	
	
}
