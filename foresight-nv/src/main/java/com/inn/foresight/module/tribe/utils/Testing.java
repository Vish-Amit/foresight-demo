package com.inn.foresight.module.tribe.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Testing {

	public static void main(String[] args) {

		String url="https://192.168.4.235/tribe/rest/appauth/uploadTribeDocs?filePath=/TRIBE_FILES/banners/banner_image/48&fileName=2j6c0p4tin4120.png";
		InputStream inputStream;
		try {
			inputStream = new FileInputStream("/home/ist/Pictures/Ideas1.jpg");
			String sendPostRequestWithInputStream = Utils.sendPostRequestWithInputStream(url, inputStream);
		System.out.println("sendPostRequestWithInputStream"+sendPostRequestWithInputStream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
