package com.indarsoft.cryptocard.types;
/**
 * VISA-Payment Technology Standards Manual
 * <p>
 * PIN Verification Method : 
 * <ul>
 * <li>VISA_PVV
 * <li>IBM_3624_OFFSET
 * </ul>
 */
public enum PinValidationType {
	VISA_PVV(0), IBM_3624_OFFSET(1) ;
    @SuppressWarnings("unused")
	private int value;

    private PinValidationType (int value) {
            this.value = value;
    }	
}
