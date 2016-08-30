package com.spacecadetdat.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * DAT Serializable interface
 * 
 * @author Ethem Kurt
 * @version 1.0.0
 * @since 1.0.0
 */
public interface IDATSerializable {

	/**
	 * Serialize content
	 * 
	 * @param output_stream
	 *            Output stream
	 * @return Number of bytes serialized
	 * @throws IOException
	 *             IO exception
	 */
	public int serialize(OutputStream output_stream) throws IOException;
}
