package com.spacecadetdat.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * DAT group class
 * 
 * @author Ethem Kurt
 * @version 1.0.0
 * @since 1.0.0
 */
public class DATGroup implements IDATSerializable {

	/**
	 * Entries
	 */
	private DATEntry[] entries = null;

	/**
	 * Constructor
	 * 
	 * @param input_stream
	 *            Input stream
	 * @throws IOException
	 *             IO exception
	 */
	public DATGroup(InputStream input_stream) throws IOException {
		byte[] entries_count = new byte[1];
		if (input_stream.read(entries_count) == entries_count.length) {
			int e_len = DATIO.toInt(entries_count);
			if (e_len > 0) {
				entries = new DATEntry[e_len];
				for (int i = 0; i < e_len; i++) {
					entries[i] = new DATEntry(input_stream);
				}
			}
		}
	}

	/**
	 * Get entries
	 * 
	 * @return DAT entry array
	 */
	public DATEntry[] getEntries() {
		return entries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.spacecadetdat.core.IDATSerializable#serialize(java.io.OutputStream)
	 */
	@Override
	public int serialize(OutputStream output_stream) throws IOException {
		int ret = 0;
		if ((entries != null) && (output_stream != null)) {
			output_stream.write(DATIO.byteToByteArray((byte) entries.length));
			ret = 1;
			for (DATEntry i : entries)
				ret += i.serialize(output_stream);
		}
		return ret;
	}
}
