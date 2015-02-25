package com.indarsoft.cryptocard.types;
/**
 * VISA-Payment Technology Standards Manual
 * <p> 
 * Pinblock format types:
 * <ul>
 * <li>ISOFORMAT0
 * <li>ISOFORMAT3
 * </ul>
 */
public enum PinBlockFormatType {

	//ISOFORMAT0(0), ISOFORMAT3(3), IBM3624(4) ;
	ISOFORMAT0(0), ISOFORMAT3(3);
    @SuppressWarnings("unused")
	private int value;

    private PinBlockFormatType (int value) {
            this.value = value;
    }	
}
