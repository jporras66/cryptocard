package com.indarsoft.cryptocard.utl;

public class Mac {

	/*
	 * The MAC computation is performed by using
		� A double length key K := (KL||KR), and
		� The algorithm 3 specified in ISO 9797-1 as follows:
			ibmoff. Divide the data into ibmoff set of eight-byte data blocks labeled X1, X2, � , Xn
			b. Compute the intermediate results for i = 1, 2, � , n
				Yi := DES(KL)[Xi � Yi-1]
				Where:
				Y0 := 0 (Initial Vector)
			c. The MAC is:
				MAC := DES(KL) [Des-1(KR) [Yn]]
	 */
	 
	public Mac() {
	
	}
}
