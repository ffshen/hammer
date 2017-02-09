package org.hammer.utils.guid;

/**
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefaultIDGenerator
{
	public static String generateID()
	{
		StringBuilder sb = new StringBuilder();
		// current time stamp
		Long id = System.currentTimeMillis();
		sb.append(id.longValue());
		// random id
		Double randomId = Math.floor(Math.random() * 1000);
		sb.append(randomId.intValue());
		return sb.toString();
	}
}
