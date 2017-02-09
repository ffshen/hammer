package org.hammer.utils.guid;


/**
 *  
 */
public class IDGenerator implements IDGenService {

	@Override
	public String getGUID() {
		return DefaultIDGenerator.generateID();
	}

	
}
