package com.indarsoft.cryptocard.symmetrickey;

/**
 * ZPK Zone Pin  Key 
 * <p>
 * @author fjavier.porras@gmail.com
 *
 */
public class Zpk extends DesKey {

	private int version ;
	
	public Zpk (String aKey ) throws DesKeyException  {
		
		 super(aKey );
		 this.version = 1 ;
	}

	public Zpk (String aKey, int aVersion ) throws DesKeyException  {
		
		 super(aKey );
		 this.version = aVersion ;
	}
	
	public int getVersion () {
		return this.version ;				
	}	
	
}
