package com.inn.foresight.module.nv.report.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtils {

	private ZipUtils() {
		super();
	}

	/**
	 * Zip file or directory.
	 * 
	 * @param file to zip
	 * @param zipAbsolutePath absolute path of zip to create
	 */
	public static void zip(File file, String zipAbsolutePath) throws IOException {

		// create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(zipAbsolutePath);

		// create object of ZipOutputStream from FileOutputStream
		ZipOutputStream zout = new ZipOutputStream(fout);
		zip(zout, file);

		// close the ZipOutputStream
		zout.close();
		fout.close();
	}

	/**
	 * Zip multiple file and directory.
	 * 
	 * @param files to zip
	 * @param zipAbsolutePath absolute path of zip to create
	 */
	public static void zip(File[] files, String zipAbsolutePath) throws IOException {
		// create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(zipAbsolutePath);

		// create object of ZipOutputStream from FileOutputStream
		ZipOutputStream zout = new ZipOutputStream(fout);

		for (File file : files) {
			zip(zout, file);
		}

		// close the ZipOutputStream
		zout.close();
		fout.close();
	}
	
	public static void zip(List<File> files, String zipAbsolutePath) throws IOException {
		// create object of FileOutputStream
		FileOutputStream fout = new FileOutputStream(zipAbsolutePath);

		// create object of ZipOutputStream from FileOutputStream
		ZipOutputStream zout = new ZipOutputStream(fout);

		for (File file : files) {
			zip(zout, file);
		}

		// close the ZipOutputStream
		zout.close();
		fout.close();
	}

	private static void zip(ZipOutputStream zout, File file) throws IOException {
		if (file.isDirectory()) {
			addDir(zout, file, file.getParentFile());
		}
		else {
			addFile(zout, file, file.getParentFile());
		}
	}

	private static void addDir(ZipOutputStream zipOutputStream, File dir, File parentFile) throws IOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory())
				addDir(zipOutputStream, file, parentFile);
			else
				addFile(zipOutputStream, file, parentFile);
		}
	}

	private static void addFile(ZipOutputStream zipOutputStream, File file, File parentFile)
			throws IOException {
		// create object of FileInputStream for source file
		try(FileInputStream fin = new FileInputStream(file)){
		// add files to ZIP
		String fileRelativePath = getRelativePath(parentFile, file);
		zipOutputStream.putNextEntry(new ZipEntry(fileRelativePath));
		// write file content
		int length;
		byte[] buffer = new byte[1024];
		while ((length = fin.read(buffer)) > 0) {
			zipOutputStream.write(buffer, 0, length);
		}
		zipOutputStream.closeEntry();
		}
	}

	private static String getRelativePath(File baseFile, File file) {
		return baseFile.toURI().relativize(file.toURI()).getPath();
	}

}