package com.inn.foresight.core.infra.rowprefix;

import static org.apache.commons.lang3.StringUtils.rightPad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.inn.commons.io.FileUtils;
import com.inn.commons.lang.StringUtils;

class HBaseRowPrefixUtils {

  private static List<String> codes;

  private static List<String> generateCodes() {
    List<char[]> ranges = new ArrayList<>();
    ranges.add(new char[]{'0', '9'});
    ranges.add(new char[]{'A', 'Z'});
    ranges.add(new char[]{'a', 'z'});

    List<Character> allowedChar = new ArrayList<>();
    for (char[] range : ranges) {
      for (char c = range[0]; c <= range[1]; c++) {
        allowedChar.add(c);
      }
    }

    List<String> codes = new ArrayList<>((int) Math.pow(allowedChar.size(), 3));
    for (char c1 : allowedChar) {
      for (char c2 : allowedChar) {
        for (char c3 : allowedChar) {
        		codes.add(new String(new char[]{c1, c2, c3}));
        }
      }
    }

    return codes;
  }

  static String getAlphaNumericPrefix(Integer numericPrefix) {
    if (codes == null) {
      codes = generateCodes();
    }
    return codes.get(numericPrefix);
  }

  static List<String[]> getDomains() {
    List<String[]> list = new ArrayList<>();
    list.add(new String[]{"RAN", "ALTIOSTAR"});
    list.add(new String[]{"RAN", "DZS"});
    list.add(new String[]{"RAN", "RESERVE0"});
    list.add(new String[]{"RAN", "RESERVE1"});
    list.add(new String[]{"RAN", "RESERVE2"});
    list.add(new String[]{"TRANSPORT", "DZS"});
    list.add(new String[]{"TRANSPORT", "OKI"});
    list.add(new String[]{"TRANSPORT", "CISCO"});
    list.add(new String[]{"TRANSPORT", "RESERVE0"});
    list.add(new String[]{"TRANSPORT", "RESERVE1"});
    list.add(new String[]{"TRANSPORT", "RESERVE2"});
    list.add(new String[]{"INFRA", "QUANTA"});
    list.add(new String[]{"INFRA", "RESERVE0"});
    list.add(new String[]{"INFRA", "RESERVE1"});
    list.add(new String[]{"INFRA", "RESERVE2"});
    list.add(new String[]{"BAREMETAL", "QUANTA"});
    list.add(new String[]{"BAREMETAL", "OKI"});
    list.add(new String[]{"BAREMETAL", "ALLOT"});
    list.add(new String[]{"BAREMETAL", "RESERVE0"});
    list.add(new String[]{"BAREMETAL", "RESERVE1"});
    list.add(new String[]{"BAREMETAL", "RESERVE2"});
    
    //othergeo
  //  list.add(new String[]{"CORE", "NOKIA"});
   // list.add(new String[]{"CORE", "MAVENIR"});
   // list.add(new String[]{"CORE", "CISCO"});
    //list.add(new String[]{"CORE", "RESERVE0"});
    //list.add(new String[]{"CORE", "RESERVE1"});

    return list;
  }

  static void writeIntoFile(Integer numericPrefix, String domain, String vendor, Integer l1Id, Integer l2Id,
      Integer l3Id) throws IOException {
    String l1 = l1Id == null ? "" : String.valueOf(l1Id);
    String l2 = l2Id == null ? "" : String.valueOf(l2Id);
    String l3 = l3Id == null ? "" : String.valueOf(l3Id);

    String outputFile = "/home/ist/HbaseRowPrefix.csv";
    String alphaNumericPrefix = getAlphaNumericPrefix(numericPrefix);
    String csvRow = StringUtils.joinWith(",", "'"+domain+"'", "'"+vendor+"'", l1, l2, l3, numericPrefix, "'"+alphaNumericPrefix+"'");
    FileUtils.writeIntoFile(outputFile, csvRow + "\n");
  }

  static void writeOnConsole(Integer numericPrefix, String domain, String vendor, Integer l1Id, Integer l2Id,
      Integer l3Id) {
    String l1 = l1Id == null ? "" : String.valueOf(l1Id);
    String l2 = l2Id == null ? "" : String.valueOf(l2Id);
    String l3 = l3Id == null ? "" : String.valueOf(l3Id);

    String alphaNumericPrefix = getAlphaNumericPrefix(numericPrefix);

    String paddedDomain = rightPad(domain, 10, ' ');
    String paddedVendor = rightPad(vendor, 10, ' ');
    String paddedL1 = rightPad(l1, 10, ' ');
    String paddedL2 = rightPad(l2, 15, ' ');
    String paddedL3 = rightPad(l3, 20, ' ');
    String paddedNumericPrefix = rightPad(String.valueOf(numericPrefix), 5, ' ');
    String paddedAlphaNumericPrefix = rightPad(alphaNumericPrefix, 5, ' ');

    System.out.println(StringUtils.joinWith("|", paddedDomain, paddedVendor, paddedL1, paddedL2, paddedL3,
        paddedNumericPrefix, paddedAlphaNumericPrefix));
  }

}