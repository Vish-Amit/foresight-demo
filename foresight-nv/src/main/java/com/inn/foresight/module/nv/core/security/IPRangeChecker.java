package  com.inn.foresight.module.nv.core.security;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPRangeChecker {
	
	private IPRangeChecker() {
		
	}

 public static long ipToLong(InetAddress ip) {
	byte[] octets = ip.getAddress();
	long result = 0;
	long valueEight=8;
	for (byte octet : octets) {
	result <<= valueEight;
	result |= octet & 0xff;
	}
	return result;
	}

	public static boolean isValidRange(String ipStart, String ipEnd,
	String ipToCheck) {
		
	try {
	long ipLo = ipToLong(InetAddress.getByName(ipStart));
	long ipHi = ipToLong(InetAddress.getByName(ipEnd));
	long ipToTest = ipToLong(InetAddress.getByName(ipToCheck));
	return (ipToTest >= ipLo && ipToTest <= ipHi);
	} catch (UnknownHostException e) {
	return false;
	}
	}

	
}
