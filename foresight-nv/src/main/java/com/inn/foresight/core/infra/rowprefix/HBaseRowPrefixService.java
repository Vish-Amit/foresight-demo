package com.inn.foresight.core.infra.rowprefix;

import java.io.IOException;
import java.util.List;

public interface HBaseRowPrefixService {

    void generate() throws IOException;

    List<RowPrefixRange> getPrefixByDomainVendor(String domain, String vendor);

    List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, String geographyName);

    List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, Integer geographyPk);

    List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, String geographyName);

    List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, Integer geographyPk);

    List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, String geographyName);

    List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, Integer geographyId);

    List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, String geographyName);

    List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, Integer geographyId);

	void generateCodeForMissingGeo() throws Exception;
	
	List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, List<Integer> geographyPk);


	List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, List<Integer> geographyPk);

	List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, List<Integer> geographyPk);

	List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, List<Integer> geographyPk);


	RowPrefix getPanLevelPrefixByVendorAndDomain(String domain, String vendor);

	List<RowPrefix> getPrefixByMultiDomainVendorCoreGeography(List<String> domain, List<String> vendor,
			List<Integer> geographyPk);

	List<RowPrefix> getPrefixByMultiDomainVendorGeographyL3(List<String> domain, List<String> vendor,
			List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByMultiDomainVendorGeographyL2(List<String> domain, List<String> vendor,
			List<Integer> geographyPk);

	List<RowPrefixRange> getPrefixByMultiDomainVendorGeographyL1(List<String> domain, List<String> vendor,
			List<Integer> geographyPk);

}