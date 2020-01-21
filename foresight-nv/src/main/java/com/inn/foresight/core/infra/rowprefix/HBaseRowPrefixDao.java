package com.inn.foresight.core.infra.rowprefix;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface HBaseRowPrefixDao extends CrudRepository<HBaseRowPrefix, Integer> {

    RowPrefixRange getPrefixByDomain(@Param("domain") String domain);

    List<RowPrefixRange> getPrefixByVendor(@Param("vendor") String vendor);

    RowPrefixRange getPrefixByDomainAndVendor(@Param("domain") String domain, @Param("vendor") String vendor);

    List<RowPrefixRange> getPrefixByGeographyL1(@Param("geographyL1") String geographyL1);

    List<RowPrefixRange> getPrefixByDomainAndGeographyL1(@Param("domain") String domain,
            @Param("geographyL1") String geographyL1);

    List<RowPrefixRange> getPrefixByVendorAndGeographyL1(@Param("vendor") String vendor,
            @Param("geographyL1") String geographyL1);

    RowPrefixRange getPrefixByDomainAndVendorAndGeographyL1(@Param("domain") String domain,
            @Param("vendor") String vendor, @Param("geographyL1") String geographyL1);

    List<RowPrefixRange> getPrefixByGeographyL1ID(@Param("geographyL1") Integer geographyL1);

    List<RowPrefixRange> getPrefixByDomainAndGeographyL1ID(@Param("domain") String domain,
            @Param("geographyL1") Integer geographyL1);

    List<RowPrefixRange> getPrefixByVendorAndGeographyL1ID(@Param("vendor") String vendor,
            @Param("geographyL1") Integer geographyL1);

    RowPrefixRange getPrefixByDomainAndVendorAndGeographyL1ID(@Param("domain") String domain,
            @Param("vendor") String vendor, @Param("geographyL1") Integer geographyL1);

    List<RowPrefixRange> getPrefixByGeographyL2(@Param("geographyL2") String geographyL2);

    List<RowPrefixRange> getPrefixByDomainAndGeographyL2(@Param("domain") String domain,
            @Param("geographyL2") String geographyL2);

    List<RowPrefixRange> getPrefixByVendorAndGeographyL2(@Param("vendor") String vendor,
            @Param("geographyL2") String geographyL2);

    RowPrefixRange getPrefixByDomainAndVendorAndGeographyL2(@Param("domain") String domain,
            @Param("vendor") String vendor, @Param("geographyL2") String geographyL2);

    List<RowPrefixRange> getPrefixByGeographyL2ID(@Param("geographyL2") Integer geographyL2);

    List<RowPrefixRange> getPrefixByDomainAndGeographyL2ID(@Param("domain") String domain,
            @Param("geographyL2") Integer geographyL2);

    List<RowPrefixRange> getPrefixByVendorAndGeographyL2ID(@Param("vendor") String vendor,
            @Param("geographyL2") Integer geographyL2);

    RowPrefixRange getPrefixByDomainAndVendorAndGeographyL2ID(@Param("domain") String domain,
            @Param("vendor") String vendor, @Param("geographyL2") Integer geographyL2);

    List<RowPrefix> getPrefixByGeographyL3(@Param("geographyL3") String geographyL3);

    List<RowPrefix> getPrefixByDomainAndGeographyL3(@Param("domain") String domain,
            @Param("geographyL3") String geographyL3);

    List<RowPrefix> getPrefixByVendorAndGeographyL3(@Param("vendor") String vendor,
            @Param("geographyL3") String geographyL3);

    RowPrefix getPrefixByDomainAndVendorAndGeographyL3(@Param("domain") String domain, @Param("vendor") String vendor,
            @Param("geographyL3") String geographyL3);

    List<RowPrefix> getPrefixByGeographyL3ID(@Param("geographyL3") Integer geographyL3);

    List<RowPrefix> getPrefixByDomainAndGeographyL3ID(@Param("domain") String domain,
            @Param("geographyL3") Integer geographyL3);

    List<RowPrefix> getPrefixByVendorAndGeographyL3ID(@Param("vendor") String vendor,
            @Param("geographyL3") Integer geographyL3);

    RowPrefix getPrefixByDomainAndVendorAndGeographyL3ID(@Param("domain") String domain, @Param("vendor") String vendor,
            @Param("geographyL3") Integer geographyL3);

    List<RowPrefix> getPrefixByCoreGeography(@Param("coreGeography") String coreGeography);

    List<RowPrefix> getPrefixByDomainAndCoreGeography(@Param("domain") String domain,
            @Param("coreGeography") String coreGeography);

    List<RowPrefix> getPrefixByVendorAndCoreGeography(@Param("vendor") String vendor,
            @Param("coreGeography") String coreGeography);

    RowPrefix getPrefixByDomainAndVendorAndCoreGeography(@Param("domain") String domain, @Param("vendor") String vendor,
            @Param("coreGeography") String coreGeography);

    List<RowPrefix> getPrefixByCoreGeographyID(@Param("coreGeography") Integer coreGeography);

    List<RowPrefix> getPrefixByDomainAndCoreGeographyID(@Param("domain") String domain,
            @Param("coreGeography") Integer coreGeography);

    List<RowPrefix> getPrefixByVendorAndCoreGeographyID(@Param("vendor") String vendor,
            @Param("coreGeography") Integer coreGeography);

    RowPrefix getPrefixByDomainAndVendorAndCoreGeographyID(@Param("domain") String domain,
            @Param("vendor") String vendor, @Param("coreGeography") Integer coreGeography);
    
    Set<HbaseRowPrefixGeoWrapper> getMaxRowPrefix();
    Set<Integer> getAllNumericCode();
    List<HbaseRowPrefixGeoWrapper> getMissingGeography();
    
    @Modifying
    @Query(value = "INSERT INTO HbaseRowPrefix(domain,vendor,numericcode, alphanumericcode,"
    		+ "geographyl1id_fk,geographyl2id_fk,geographyl3id_fk,  l1,l2,l3 )"
    		+ " VALUES(?1,?2,?3,?4,?5,?6,?7,?8,?9,?10)", nativeQuery = true)
    Integer insertRecord(String domain,String vendor,Integer numericcode, 
    		String alphanumericcode,Integer geographyl1id_fk,Integer geographyl2id_fk,
    		Integer geographyl3id_fk,  String l1,String l2,String l3);
 
    Set<HbaseRowPrefixGeoWrapper> getAvailableRowPrefix();
    
    List<RowPrefix> getL1PrefixByDomain(@Param("domain") String domain);
    List<RowPrefix> getL1PrefixByDomainAndVendor(@Param("domain") String domain,@Param("vendor") List<String> vendor);
    
    List<RowPrefixRange> getPrefixByMultiGeographyL1ID(@Param("geographyL1") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByDomainAndMultiGeographyL1ID(@Param("domain") String domain, @Param("geographyL1") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByVendorAndMultiGeographyL1ID(@Param("vendor")String vendor, @Param("geographyL1") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByDomainAndVendorAndMultiGeographyL1ID(@Param("domain")String domain, @Param("vendor")String vendor, @Param("geographyL1") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByMultiGeographyL2ID(@Param("geographyL2") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByDomainAndMultiGeographyL2ID(@Param("domain") String domain, @Param("geographyL2") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByVendorAndMultiGeographyL2ID(@Param("vendor") String vendor,@Param("geographyL2") List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByDomainAndVendorAndMultiGeographyL2ID(@Param("domain") String domain,@Param("vendor") String vendor,@Param("geographyL2") 
			List<Integer> geographyPk);

	List<RowPrefix> getPrefixByMultiGeographyL3ID(@Param("geographyL3") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByDomainAndMultiGeographyL3ID(@Param("domain") String domain,@Param("geographyL3") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByVendorAndMultiGeographyL3ID(@Param("vendor") String vendor,@Param("geographyL3") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByDomainAndVendorAndMultiGeographyL3ID(@Param("domain") String domain,@Param("vendor") String vendor,@Param("geographyL3") 
			List<Integer> geographyPk);

	List<RowPrefix> getPrefixByCoreMultiGeographyID(@Param("coreGeography") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByDomainAndCoreMultiGeographyID(@Param("domain") String domain,@Param("coreGeography") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByVendorAndCoreMultiGeographyID(@Param("vendor") String vendor,@Param("coreGeography") List<Integer> geographyPk);

	List<RowPrefix> getPrefixByDomainAndVendorAndCoreMultiGeographyID(@Param("domain") String domain,@Param("vendor") String vendor,@Param("coreGeography")
			List<Integer> geographyPk);
	
    RowPrefix getPanLevelPrefixByVendorAndDomain(@Param("domain") String domain, @Param("vendor") String vendor);
    
    List<RowPrefixRange> getPrefixRangeByDomainAndVendorList(@Param("domain") String domain,
            @Param("vendors") List<String> vendors);
 
    List<RowPrefixRange> getPrefixByMultiDomainAndMultiGeographyL1ID(@Param("domain") List<String> domain, @Param("geographyL1") List<Integer> geographyPk);
 
    List<RowPrefixRange> getPrefixByMultiVendorAndMultiGeographyL1ID(@Param("vendor") List<String> vendor, @Param("geographyL1") List<Integer> geographyPk);

    List<RowPrefixRange> getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL1ID(@Param("domain") List<String> domain, @Param("vendor") List<String> vendor,
			@Param("geographyL1") List<Integer> geographyPk);
    
    List<RowPrefixRange> getPrefixByMultiDomainAndMultiGeographyL2ID(@Param("domain") List<String> domain, @Param("geographyL2") List<Integer> geographyPk);
    
    List<RowPrefixRange> getPrefixByMultiVendorAndMultiGeographyL2ID(@Param("vendor") List<String> vendor,@Param("geographyL2") List<Integer> geographyPk);
    
    List<RowPrefixRange> getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL2ID(@Param("domain") List<String> domain,@Param("vendor") List<String> vendor,@Param("geographyL2") 
	List<Integer> geographyPk);
    
    List<RowPrefix> getPrefixByMultiDomainAndMultiGeographyL3ID(@Param("domain") List<String> domain,@Param("geographyL3") List<Integer> geographyPk);
    
    List<RowPrefix> getPrefixByMultiVendorAndMultiGeographyL3ID(@Param("vendor") List<String> vendor,@Param("geographyL3") List<Integer> geographyPk);
    
    List<RowPrefix> getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL3ID(@Param("domain") List<String> domain, @Param("vendor") List<String> vendor,@Param("geographyL3") 
	List<Integer> geographyPk);
    
    List<RowPrefix> getPrefixByMultiDomainAndMultiCoreGeographyID(@Param("domain") List<String> domain,@Param("coreGeography") List<Integer> geographyPk);
    
    List<RowPrefix> getPrefixByMultiVendorAndMultiCoreGeographyID(@Param("vendor") List<String> vendor,@Param("coreGeography") List<Integer> geographyPk);

    List<RowPrefix> getPrefixByMultiDomainAndMultiVendorAndMultiCoreGeographyID(@Param("domain") List<String> domain,@Param("vendor") List<String> vendor,@Param("coreGeography")
	List<Integer> geographyPk);
}