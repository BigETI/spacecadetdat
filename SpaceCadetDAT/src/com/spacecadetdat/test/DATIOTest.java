package com.spacecadetdat.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.spacecadetdat.core.DATEntry;
import com.spacecadetdat.core.DATGroup;
import com.spacecadetdat.core.DATIO;

/**
 * @author Ethem Kurt
 * @version 1.0.0
 * @since 1.0.0
 */
public class DATIOTest {

	/**
	 * IO
	 */
	private DATIO io;

	/**
	 * @throws Exception
	 *             Exception
	 */
	@Before
	public void setUp() throws Exception {
		io = new DATIO("./PINBALL.DAT");
	}

	/**
	 * Sump entry
	 * 
	 * @param path
	 *            Path to dump
	 * @param gid
	 *            Group ID
	 * @param eid
	 *            Entry ID
	 * @param entry
	 *            Entry
	 */
	public void dumpEntry(String path, int gid, int eid, DATEntry entry) {
		try (FileOutputStream fos = new FileOutputStream(
				new File("./" + path + "/" + gid + "_" + eid + "_" + entry.getType() + ".bin"))) {
			entry.serialize(fos);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test
	 */
	@Test
	public void test() {
		System.out.println("Signature: " + io.getSignature());
		System.out.println("Application name: " + io.getAppName());
		System.out.println("File description: " + io.getFileDescription());
		System.out.println("File size: " + io.getFileSize());
		if (io.getGroups() != null) {
			System.out.println("Groups (" + io.getGroups().length + "):");
			//int ii = 0;
			for (DATGroup i : io.getGroups()) {
				//int ij = 0;
				if (i.getEntries() != null) {
					System.out.println("\tEntries (" + i.getEntries().length + "):");
					for (DATEntry j : i.getEntries()) {
						// dumpEntry("PINBALL_DAT", ii, ij, j);
						System.out.println("\t\tType " + j.getType());
						//++ij;
					}
				}
				//++ii;
			}
		}
		System.out.println("Unknown 1: " + io.getUnknown1());
		System.out.println("Unknown 2: " + io.getUnknown2());
		// try (FileOutputStream fos = new FileOutputStream(new
		// File("PINBALL2.DAT"))) {
		// io.serialize(fos);
		// fos.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// //
		// }

		// io = new DATIO("./FONT.DAT");
		// if (io.getGroups() != null) {
		// int ii = 0;
		// for (DATGroup i : io.getGroups()) {
		// int ij = 0;
		// if (i.getEntries() != null) {
		// for (DATEntry j : i.getEntries()) {
		// dumpEntry("FONT_DAT", ii, ij, j);
		// ++ij;
		// }
		// }
		// ++ii;
		// }
		// }
	}

}
