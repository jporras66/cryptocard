package com.indarsoft.cryptocard.types;
/**
 * For ibmoff detailed description of DES and Triple DES, please refer to
 * ANSI X9.52
 * <ul>
 * <li>SIMPLE 8  bytes key length
 * <li>DOUBLE 16 bytes key length
 * <li>TRIPLE 24 bytes key length
 * </ul> 
 */
public enum DesKeyType {

	SIMPLE(0), DOUBLE(1), TRIPLE(2) ;
    @SuppressWarnings("unused")
	private int value;

    private DesKeyType (int value) {
            this.value = value;
    }	
}
