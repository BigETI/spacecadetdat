package com.spacecadetdat.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * DAT IO class
 * 
 * @author Ethem Kurt
 * @version 1.0.0
 * @since 1.0.0
 */
public class DATIO implements IDATSerializable {

	/**
	 * Signature
	 */
	private String signature = "";

	/**
	 * Application name
	 */
	private String app_name = "";

	/**
	 * File description
	 */
	private String file_description = "";

	/**
	 * Unknown 1
	 */
	private short unknown1 = 0;

	/**
	 * Unknown 2
	 */
	private int unknown2 = 0;

	/**
	 * Groups
	 */
	private DATGroup[] groups = null;

	/**
	 * Constructor
	 * 
	 * @param file_name
	 *            File name
	 */
	public DATIO(String file_name) {
		try {
			File file = new File(file_name);
			try (FileInputStream fis = new FileInputStream(file)) {
				init(fis);
			} finally {
				//
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Construtor
	 * 
	 * @param file
	 *            File
	 */
	public DATIO(File file) {
		try (FileInputStream fis = new FileInputStream(file)) {
			init(fis);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//
		}
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 *            URL
	 */
	public DATIO(URL url) {
		try {
			init(url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor
	 * 
	 * @param input_stream
	 *            Input stream
	 */
	public DATIO(InputStream input_stream) {
		init(input_stream);
	}

	/**
	 * Byte array to integer
	 * 
	 * @param b
	 *            Byte array
	 * @return Integer
	 */
	public static int toInt(byte[] b) {
		int ret = 0;
		int len = Math.min(4, b.length);
		for (int i = len - 1; i >= 0; i--) {
			ret |= ((((int) (b[i])) & 0xFF) << (i * 8));
		}
		return ret;
	}

	/**
	 * Integer to byte array
	 * 
	 * @param value
	 *            Integer
	 * @return Byte array
	 */
	public static byte[] intToByteArray(int value) {
		byte[] ret = new byte[4];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) ((value >> (i * 8)) & 0xFF);
		}
		return ret;
	}

	/**
	 * Short to byte array
	 * 
	 * @param value
	 *            Short
	 * @return Byte array
	 */
	public static byte[] shortToByteArray(short value) {
		byte[] ret = new byte[2];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (byte) ((value >> (i * 8)) & 0xFF);
		}
		return ret;
	}

	/**
	 * Byte to byte array
	 * 
	 * @param value
	 *            Byte
	 * @return Byte array
	 */
	public static byte[] byteToByteArray(byte value) {
		byte[] ret = new byte[1];
		ret[0] = value;
		return ret;
	}

	/**
	 * Null terminated string to string
	 * 
	 * @param b
	 *            Byte array
	 * @return String
	 */
	public static String nullTerminatedStringToString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (byte i : b) {
			if (i == 0)
				break;
			sb.append((char) i);
		}
		return sb.toString();
	}

	/**
	 * String to null terminated string
	 * 
	 * @param str
	 *            String
	 * @param max_len
	 *            Max byte array length
	 * @return Byte array
	 */
	public static byte[] stringToNullTerminatedString(String str, int max_len) {
		byte[] ret = null;
		if ((str != null) && (max_len > 0)) {
			ret = new byte[max_len];
			for (int i = 0; i < Math.min(max_len, str.length()); i++)
				ret[i] = (byte) str.charAt(i);
			ret[ret.length - 1] = '\0';
		}
		return ret;
	}

	/**
	 * Initialize object
	 * 
	 * @param input_stream
	 *            Input stream
	 */
	private void init(InputStream input_stream) {
		byte[] signature = new byte[21];
		byte[] app_name = new byte[50];
		byte[] file_description = new byte[100];
		byte[] file_size = new byte[4];
		byte[] groups_count = new byte[2];
		byte[] unknown1 = new byte[2];
		byte[] unknown2 = new byte[4];
		try {
			if (input_stream.read(signature) == signature.length) {
				this.signature = nullTerminatedStringToString(signature);
				if (input_stream.read(app_name) == app_name.length) {
					this.app_name = nullTerminatedStringToString(app_name);
					if (input_stream.read(file_description) == file_description.length) {
						this.file_description = nullTerminatedStringToString(file_description);
						if (input_stream.read(file_size) == file_size.length) {
							// this.file_size = toInt(file_size);
							if (input_stream.read(groups_count) == groups_count.length) {
								int g_len = toInt(groups_count);
								if (g_len > 0) {
									groups = new DATGroup[g_len];
									if (input_stream.read(unknown1) == unknown1.length) {
										this.unknown1 = (short) toInt(unknown1);
										if (input_stream.read(unknown2) == unknown2.length) {
											this.unknown2 = toInt(unknown2);
											try {
												for (int i = 0; i < g_len; i++) {
													groups[i] = new DATGroup(input_stream);
												}
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get signature
	 * 
	 * @return Signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Set signature
	 * 
	 * @param signature
	 *            Signature
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * Get application name
	 * 
	 * @return Application name
	 */
	public String getAppName() {
		return app_name;
	}

	/**
	 * Set application name
	 * 
	 * @param app_name
	 *            Application name
	 */
	public void setAppName(String app_name) {
		this.app_name = app_name;
	}

	/**
	 * Get file description
	 * 
	 * @return File description
	 */
	public String getFileDescription() {
		return file_description;
	}

	/**
	 * Set file description
	 * 
	 * @param file_description
	 *            File description
	 */
	public void setFileDescription(String file_description) {
		this.file_description = file_description;
	}

	/**
	 * Get file size
	 * 
	 * @return File size
	 */
	public int getFileSize() {
		int ret = 0;
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ret = serialize(baos);
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			//
		}
		return ret;
	}

	/**
	 * Get unknown 1
	 * 
	 * @return Unknown 1
	 */
	public short getUnknown1() {
		return unknown1;
	}

	/**
	 * Set unknown 1
	 * 
	 * @param unknown1
	 *            Unknown 1
	 */
	public void setUnknown1(short unknown1) {
		this.unknown1 = unknown1;
	}

	/**
	 * Get unknown 2
	 * 
	 * @return Unknown 2
	 */
	public int getUnknown2() {
		return unknown2;
	}

	/**
	 * Set unknown
	 * 
	 * @param unknown2
	 *            Unknown 2
	 */
	public void setUnknown2(int unknown2) {
		this.unknown2 = unknown2;
	}

	/**
	 * Get groups
	 * 
	 * @return Groups
	 */
	public DATGroup[] getGroups() {
		return groups;
	}

	/**
	 * Set groups
	 * 
	 * @param groups
	 *            Groups
	 */
	public void setGroups(DATGroup[] groups) {
		this.groups = groups;
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
		if (output_stream != null) {
			output_stream.write(stringToNullTerminatedString(signature, 21));
			output_stream.write(stringToNullTerminatedString(app_name, 50));
			output_stream.write(stringToNullTerminatedString(file_description, 100));
			byte[] x_data = null;
			try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
				baos.write(shortToByteArray((short) ((groups != null) ? groups.length : 0)));
				baos.write(shortToByteArray(unknown1));
				baos.write(intToByteArray(unknown2));
				if (groups != null) {
					for (DATGroup i : groups) {
						i.serialize(baos);
					}
				}
				x_data = baos.toByteArray();
			} finally {
				//
			}
			int file_size = 175 + x_data.length;
			output_stream.write(intToByteArray(file_size));
			output_stream.write(x_data);
			ret = file_size;
		}
		return ret;
	}
}
