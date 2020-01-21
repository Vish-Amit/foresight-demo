package com.inn.foresight.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.service.IAdvanceSearchService;
import com.inn.foresight.test.SpringJUnitRunner;

public class TestAdvanceSearchServiceImpl extends SpringJUnitRunner {

  @Autowired
  public IAdvanceSearchService iadvancesearchservice;


  @Test
  public void validateVendor() {
    List<AdvanceSearch> list = new ArrayList<>();
    Map<String, List<String>> map = new HashMap<>();
    System.out.println("--- Test Case 1 ---");
    map.put("vendor", Arrays.asList("NOKIA", "SAMSUNG"));
    map.put("type",
        Arrays.asList("MACRO_ONAIR", "MACRO_DECOMMISSIONED", "MACRO_NONRADIATING", "PICO_ONAIR",
            "IBS_ONAIR", "SHOOTER_ONAIR", "MACRO_PLANNE", "ODSC_ONAIR", "OUTLET", "HOST", "ipv4",
            "GeographyL0", "NENAME", "GeographyL1", "GeographyL2", "GeographyL3", "GeographyL4",
            "COREGEOGRAPHY", "GALLERY", "SMILE", "Building", "SalesL1", "SalesL2", "SalesL3",
            "SalesL4"));


    list = iadvancesearchservice.getAdvanceSearchByTypeListAndVendor("NJKT", map);
    System.out.println("Advancesearh List :- " + list);


  } 

  @Test
  public void searchOutlet() {
    List<AdvanceSearch> list = new ArrayList<>();
    Map<String, List<String>> map = new HashMap<>();
    System.out.println("--- Test Case 2 ---");
    /*
     * If username :- salesnokiauser1 email :- salesnokiauser1@innoeye.com { levelType :- sales_NHQ
     * } Advancesearh List :- [] :- JABO 1, JABO 2 , JABO 3
     */
    map.put("vendor", Arrays.asList("NOKIA", "SAMSUNG"));
    map.put("type",
        Arrays.asList("MACRO_ONAIR", "MACRO_DECOMMISSIONED", "MACRO_NONRADIATING", "PICO_ONAIR",
            "IBS_ONAIR", "SHOOTER_ONAIR", "MACRO_PLANNE", "ODSC_ONAIR", "OUTLET", "HOST", "ipv4",
            "GeographyL0", "NENAME", "GeographyL1", "GeographyL2", "GeographyL3", "GeographyL4",
            "COREGEOGRAPHY", "GALLERY", "SMILE", "Building", "SalesL1", "SalesL2", "SalesL3",
            "SalesL4"));



    list = iadvancesearchservice.getAdvanceSearchByTypeListAndVendor("CJA0", map);
    System.out.println("Advancesearh List :- " + list);



  }
  /*
   * When username:- pralay.s01 email :- pralay.s01@innoeye.com Output :- Intersection b/w vendor
   * and userOrganizationType is null (AdvanceSearchServiceImpl.java:722) Advancesearh List :- []
   * 
   */


  @Test
  public void vendorNull() {
    List<AdvanceSearch> list = new ArrayList<>();
    Map<String, List<String>> map = new HashMap<>();
    System.out.println("--- Test Case 3 ---");
    // map.put("vendor", Arrays.asList("NOKIA","SAMSUNG"));
    /*
     * When username :- salesnokiauser1 email :- salesnokiauser1@innoeye.com Output :- JABO 1, JABO
     * 2 , JABO 3
     */
    map.put("type",
        Arrays.asList("MACRO_ONAIR", "MACRO_DECOMMISSIONED", "MACRO_NONRADIATING", "PICO_ONAIR",
            "IBS_ONAIR", "SHOOTER_ONAIR", "MACRO_PLANNE", "ODSC_ONAIR", "OUTLET", "HOST", "ipv4",
            "GeographyL0", "NENAME", "GeographyL1", "GeographyL2", "GeographyL3", "GeographyL4",
            "COREGEOGRAPHY", "GALLERY", "SMILE", "Building", "SalesL1", "SalesL2", "SalesL3",
            "SalesL4"));



    list = iadvancesearchservice.getAdvanceSearchByTypeListAndVendor("JABO", map);
    System.out.println("Advancesearh List :- " + list);


  }

  @Test
  public void salesUserValidator() {
    List<AdvanceSearch> list = new ArrayList<>();
    Map<String, List<String>> map = new HashMap<>();
    System.out.println("--- Test Case 4 ---");
    /*
     * If username :- salesnokiauser1 email :- salesnokiauser1@innoeye.com {levelType :- sales_NHQ }
     * Advancesearh List :- [] :- JABO 1, JABO 2 , JABO 3
     */
    map.put("vendor", Arrays.asList("NOKIA", "SAMSUNG"));
    map.put("type",
        Arrays.asList("MACRO_ONAIR", "MACRO_DECOMMISSIONED", "MACRO_NONRADIATING", "PICO_ONAIR",
            "IBS_ONAIR", "SHOOTER_ONAIR", "MACRO_PLANNE", "ODSC_ONAIR", "OUTLET", "HOST", "ipv4",
            "GeographyL0", "NENAME", "GeographyL1", "GeographyL2", "GeographyL3", "GeographyL4",
            "COREGEOGRAPHY", "GALLERY", "SMILE", "Building", "SalesL1", "SalesL2", "SalesL3",
            "SalesL4"));



    list = iadvancesearchservice.getAdvanceSearchByTypeListAndVendor("JABO", map);
    System.out.println("Advancesearh List :- " + list);



  }
}
