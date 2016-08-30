package com.spacecadetdat.core;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * DAT entry class
 * 
 * @author Ethem Kurt
 * @version 1.0.0
 * @since 1.0.0
 */
public class DATEntry implements IDATSerializable {

	/**
	 * Type
	 */
	byte type = 0;

	/**
	 * Data
	 */
	byte[] data = null;

	/**
	 * Constructor
	 * 
	 * @param input_stream
	 *            Input stream
	 * @throws IOException
	 *             IO exception
	 */
	public DATEntry(InputStream input_stream) throws IOException {
		byte[] type = new byte[1];
		if (input_stream.read(type) == type.length) {
			this.type = (byte) DATIO.toInt(type);
			if (this.type == 0) {
				data = new byte[2];
				input_stream.read(data);
			} else {
				byte[] data_len = new byte[4];
				if (input_stream.read(data_len) == data_len.length) {
					int d_len = DATIO.toInt(data_len);
					if (d_len > 0) {
						data = new byte[d_len];
						input_stream.read(data);
					}
				}
			}
		}
	}

	/**
	 * Get type
	 * 
	 * @return Type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * Set type
	 * 
	 * @param type
	 *            Type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * Get data
	 * 
	 * @return Data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * Set data
	 * 
	 * @param data
	 *            Data
	 */
	public void setData(byte[] data) {
		if (type != 0)
			this.data = data;
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
		if ((data != null) && (output_stream != null)) {
			output_stream.write(DATIO.byteToByteArray(type));
			ret = 1;
			if (type != 0) {
				output_stream.write(DATIO.intToByteArray(data.length));
				ret += 4;
			}
			output_stream.write(data);
			ret += data.length;
		}
		return ret;
	}
}
