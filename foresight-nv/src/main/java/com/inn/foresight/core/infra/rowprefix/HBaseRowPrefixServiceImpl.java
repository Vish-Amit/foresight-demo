package com.inn.foresight.core.infra.rowprefix;

import static com.inn.foresight.core.infra.rowprefix.HBaseRowPrefixConstants.RESERVED_L3_IN_DOMAIN;
import static com.inn.foresight.core.infra.rowprefix.HBaseRowPrefixConstants.RESERVED_L3_IN_L1;
import static com.inn.foresight.core.infra.rowprefix.HBaseRowPrefixConstants.RESERVED_L3_IN_L2;
import static j2html.TagCreator.body;
import static j2html.TagCreator.br;
import static j2html.TagCreator.head;
import static j2html.TagCreator.html;
import static j2html.TagCreator.text;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.AESUtils;
import com.inn.commons.jdbc.JdbcClient;
import com.inn.commons.jdbc.SqlResult;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.utils.wrapper.GeographyMappingWrapper;

import j2html.tags.ContainerTag;

@Service("HBaseRowPrefixServiceImpl")
public class HBaseRowPrefixServiceImpl implements HBaseRowPrefixService {

  private static Logger logger = LogManager.getLogger(HBaseRowPrefixServiceImpl.class);

  @Autowired
  private GeographyL3Dao geographyL3Dao;

  @Autowired
  private HBaseRowPrefixDao hBaseRowPrefixDao;

  @Override
  public void generate() throws IOException {
    try {
      List<GeographyMappingWrapper> geographyMappings = getGeographyMapping();
      if (CollectionUtils.isEmpty(geographyMappings)) {
        logger.error("Could not create HBaseRowPrefix, as geography mapping not found in DB");
        return;
      }

      Integer numericPrefix = 0;
      String lastDomain = null;
      Integer lastL1;
      Integer lastL2;

      for (String[] domainVendor : HBaseRowPrefixUtils.getDomains()) {
        if (!StringUtils.equalsIgnoreCase(lastDomain, domainVendor[0])) {
          lastDomain = domainVendor[0];
          write(new String[]{domainVendor[0], ""}, null, null, null, numericPrefix);
          numericPrefix++;
        }

        write(domainVendor, null, null, null, numericPrefix);
        lastL1 = lastL2 = null;
        numericPrefix++;

        for (GeographyMappingWrapper geographyMapping : geographyMappings) {
          Integer l1 = geographyMapping.getGeographyL1Id();
          Integer l2 = geographyMapping.getGeographyL2Id();
          Integer l3 = geographyMapping.getGeographyL3Id();

          if (lastL1 == null) {
            write(domainVendor, l1, null, null, numericPrefix);
            numericPrefix++;

            write(domainVendor, l1, l2, null, numericPrefix);
            numericPrefix++;
          } else if (l1 != lastL1) {
            numericPrefix += RESERVED_L3_IN_L1 - 1;

            write(domainVendor, l1, null, null, numericPrefix);
            numericPrefix++;

            write(domainVendor, l1, l2, null, numericPrefix);
            numericPrefix++;
          } else if (l2 != lastL2) {
            numericPrefix += RESERVED_L3_IN_L2 - 1;

            write(domainVendor, l1, l2, null, numericPrefix);
            numericPrefix++;
          }

          write(domainVendor, l1, l2, l3, numericPrefix);

          lastL1 = l1;
          lastL2 = l2;
          numericPrefix++;
        }

        numericPrefix += RESERVED_L3_IN_DOMAIN - 1;
        lastL1 = null;
      }
    } catch (RuntimeException e) {
      logger.error("error in generating HBase Row Prefix", e);
    }
  }

  private List<GeographyMappingWrapper> getGeographyMapping() {
    try {
      ConfigUtils.setPropertiesFilePath("application.properties");
      JdbcClient jdbc = new JdbcClient(ConfigUtils.getString("commons.datasource.driverClassName"),
          ConfigUtils.getString("commons.datasource.url"),
          AESUtils.decrypt(ConfigUtils.getString("commons.datasource.username")),
          AESUtils.decrypt(ConfigUtils.getString("commons.datasource.checksum")));

      SqlResult rs = jdbc.execute("SELECT DISTINCT geographyl1id_pk, geographyl2id_pk, geographyl3id_pk "
          + "FROM GeographyL1 JOIN GeographyL2 ON geographyl1id_fk=geographyl1id_pk "
          + "JOIN GeographyL3 ON geographyl2id_fk=geographyl2id_pk");

      List<GeographyMappingWrapper> list = new ArrayList<>();
      while (rs.next()) {
        list.add(new GeographyMappingWrapper(NumberUtils.toInteger(rs.getString(1)),
            NumberUtils.toInteger(rs.getString(2)), NumberUtils.toInteger(rs.getString(3))));
      }
      return list;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    // return geographyL3Dao.getGeographyMapping();
  }

  private void write(String[] domainVendor, Integer l1, Integer l2, Integer l3, Integer numericPrefix)
      throws IOException {
    String domain = domainVendor[0];
    String vendor = domainVendor[1];

    // HBaseRowPrefixUtils.writeOnConsole(numericPrefix, domain, vendor, l1, l2, l3);
    HBaseRowPrefixUtils.writeIntoFile(numericPrefix, domain, vendor, l1, l2, l3);
  }

  private static <T> List<T> toList(T obj) {
    return obj == null ? Collections.emptyList() : Arrays.asList(obj);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByDomainVendor(String domain, String vendor) {
    Validate.checkArgument(!StringUtils.isAllEmpty(domain, vendor), "Domain and vendor is null or empty");

    List<RowPrefixRange> prefixs = null;
    if (StringUtils.isEmpty(vendor)) {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomain(domain));
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendor(vendor);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendor(domain, vendor));
    }
    return Validate.checkNotEmpty(prefixs, "No prefix found for domain=" + domain + ", vendor=" + vendor);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, String geographyName) {
    Validate.checkNotEmpty(geographyName, "GeographyL3 name is null or empty");

    List<RowPrefixRange> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL1(geographyName);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL1(domain, geographyName);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL1(vendor, geographyName);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL1(domain, vendor, geographyName));
    }

    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyName=" + geographyName);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, Integer geographyPk) {
    Validate.checkNotNull(geographyPk, "GeographyL1Pk is null");

    List<RowPrefixRange> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL1ID(geographyPk);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL1ID(domain, geographyPk);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL1ID(vendor, geographyPk);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL1ID(domain, vendor, geographyPk));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
  }
  
	// TODO
	@Override
	@Cacheable(cacheNames = "HBaseRowPrefixCache")
	public List<RowPrefixRange> getPrefixByGeographyL1(String domain, String vendor, List<Integer> geographyPk) {
		Validate.checkNotNull(geographyPk, "GeographyL1Pk is null");
		List<String> domains = StringUtils.isNotEmpty(domain) ? Arrays.asList(domain) : null;
		List<String> vendors = StringUtils.isNotEmpty(vendor) ? Arrays.asList(vendor) : null;
		return getPrefixByMultiDomainVendorGeographyL1(domains, vendors, geographyPk);
	}

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, String geographyName) {
    Validate.checkNotEmpty(geographyName, "GeographyL2Name is null or empty");

    List<RowPrefixRange> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL2(geographyName);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL2(domain, geographyName);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL2(vendor, geographyName);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL2(domain, vendor, geographyName));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyName=" + geographyName);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, Integer geographyPk) {
    Validate.checkNotNull(geographyPk, "GeographyL2Pk is null");

    List<RowPrefixRange> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL2ID(geographyPk);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL2ID(domain, geographyPk);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL2ID(vendor, geographyPk);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL2ID(domain, vendor, geographyPk));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefixRange> getPrefixByGeographyL2(String domain, String vendor, List<Integer> geographyPk) {
    Validate.checkNotNull(geographyPk, "GeographyL2Pk is null");
    List<String> domains = StringUtils.isNotEmpty(domain) ? Arrays.asList(domain) : null;
	List<String> vendors = StringUtils.isNotEmpty(vendor) ? Arrays.asList(vendor) : null;
    return getPrefixByMultiDomainVendorGeographyL2(domains, vendors, geographyPk);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, String geographyName) {
    Validate.checkNotEmpty(geographyName, "GeographyL3Name is null or empty");

    List<RowPrefix> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL3(geographyName);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL3(vendor, geographyName);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL3(domain, geographyName);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL3(domain, vendor, geographyName));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyName=" + geographyName);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, Integer geographyPk) {
    Validate.checkNotNull(geographyPk, "Domain, vendor and geographyPk is null or empty");

    List<RowPrefix> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByGeographyL3ID(geographyPk);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndGeographyL3ID(domain, geographyPk);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndGeographyL3ID(vendor, geographyPk);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndGeographyL3ID(domain, vendor, geographyPk));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
  }
  //TODO
  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByGeographyL3(String domain, String vendor, List<Integer> geographyPk) {
    Validate.checkNotNull(geographyPk, "Domain, vendor and geographyPk is null or empty");
    List<String> domains = StringUtils.isNotEmpty(domain) ? Arrays.asList(domain) : null;
	List<String> vendors = StringUtils.isNotEmpty(vendor) ? Arrays.asList(vendor) : null;
    return getPrefixByMultiDomainVendorGeographyL3(domains, vendors, geographyPk);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, String geographyName) {
    Validate.checkNotEmpty(geographyName, "Core geographyName is null or empty");

    List<RowPrefix> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByCoreGeography(geographyName);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndCoreGeography(domain, geographyName);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndCoreGeography(vendor, geographyName);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndCoreGeography(domain, vendor, geographyName));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyName=" + geographyName);
  }

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, Integer geographyPk) {
    Validate.checkNotNull(geographyPk, "Core geographyPk is null");

    List<RowPrefix> prefixs = null;
    if (StringUtils.isAllEmpty(domain, vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByCoreGeographyID(geographyPk);
    } else if (StringUtils.isEmpty(vendor)) {
      prefixs = hBaseRowPrefixDao.getPrefixByDomainAndCoreGeographyID(domain, geographyPk);
    } else if (StringUtils.isEmpty(domain)) {
      prefixs = hBaseRowPrefixDao.getPrefixByVendorAndCoreGeographyID(vendor, geographyPk);
    } else {
      prefixs = toList(hBaseRowPrefixDao.getPrefixByDomainAndVendorAndCoreGeographyID(domain, vendor, geographyPk));
    }
    return Validate.checkNotEmpty(prefixs,
        "No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
  }
  //TODO
  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
  public List<RowPrefix> getPrefixByCoreGeography(String domain, String vendor, List<Integer> geographyPk) {
    Validate.checkNotNull(geographyPk, "Core geographyPk is null");
    List<String> domains = StringUtils.isNotEmpty(domain) ? Arrays.asList(domain) : null;
	List<String> vendors = StringUtils.isNotEmpty(vendor) ? Arrays.asList(vendor) : null;
	return getPrefixByMultiDomainVendorCoreGeography(domains,vendors,geographyPk);
  }

 

  @Transactional
  @Override
  public void generateCodeForMissingGeo() throws Exception{
	  try{
		  logger.info("Going to generate missing sequence number for Missing geography");
		  Set<Integer> allHbaseRowPrefix=hBaseRowPrefixDao.getAllNumericCode();
		  logger.debug("Total HbaseRowprefix geo {}",allHbaseRowPrefix);
		  Set<HbaseRowPrefixGeoWrapper> availableRowPrefix= hBaseRowPrefixDao.getAvailableRowPrefix();
		  logger.info("Total available Hbase Row Prefix {}",(availableRowPrefix!=null?availableRowPrefix.size():0));
		  List<HbaseRowPrefixGeoWrapper>missingGeo=hBaseRowPrefixDao.getMissingGeography();
		  logger.info("Total missing geo {}",missingGeo!=null?missingGeo.size():0);
		  
		  addGeoInHbaseRowPrefix(missingGeo, allHbaseRowPrefix,availableRowPrefix);
		  logger.info("Activity Successfully Done.");
	  }catch (Exception e) {
		  logger.error("error in generating HBase Row Prefix for Missing geo {}", e.getMessage());
		  sendMailToAuthority(Utils.getStackTrace(e));
		  throw new RestException(ForesightConstants.FAILURE_JSON);
	}
  }
  
  
  private void sendMailToAuthority(String exceptionDetails) {
	try{
		String recieverEmailIds=ConfigUtils.getString("HBASEROWPREFIX_EMAIL_NOTIFICATION");
		String ccEmailIds="nimit.agrawal@innoeye.com,rahul.shukla@innoeye.com,abhishek.mishra@innoeye.com";
		String mailSubject="HBaseRowPrefix code not generated";
		Utils.createEmailNotification(recieverEmailIds, ccEmailIds, null, mailSubject, 
				createMessageBody(exceptionDetails), null, null);
	}catch (Exception e) {
		logger.error("Exception in sending mail notification {}",e);
	}
}
  
  private String createMessageBody(String exceptionDetails){
		ContainerTag body = body(text("Dear Team, "), br(), br());
		body.with(text("Please look into the below Exception."),br(),br());
		body.with(text(exceptionDetails));
		return html(head(), body).render();
  }
  
public void addGeoInHbaseRowPrefix(List<HbaseRowPrefixGeoWrapper> missingGeo,Set<Integer> allHbaseRowPrefix, Set<HbaseRowPrefixGeoWrapper> availableRowPrefix) throws Exception{	
		Set<HbaseRowPrefixGeoWrapper> maxRowPrefix=hBaseRowPrefixDao.getMaxRowPrefix();
		if(maxRowPrefix==null){
			throw new BusinessException("Unable to get domain-vendor wise max count");
		}
		
		for(HbaseRowPrefixGeoWrapper missedGeo:missingGeo){
			
			HbaseRowPrefixGeoWrapper missedGeoL1 = new HbaseRowPrefixGeoWrapper(missedGeo.getL1PK(), null, null,
					missedGeo.getL1Name(), null, null);
			addRowPrefixForGeo(allHbaseRowPrefix, maxRowPrefix, missedGeoL1,availableRowPrefix);

			HbaseRowPrefixGeoWrapper missedGeoL2 = new HbaseRowPrefixGeoWrapper(missedGeo.getL1PK(),
					missedGeo.getL2PK(), null, missedGeo.getL1Name(), missedGeo.getL2Name(), null);
			addRowPrefixForGeo(allHbaseRowPrefix, maxRowPrefix, missedGeoL2,availableRowPrefix);

			HbaseRowPrefixGeoWrapper missedGeoL3 = new HbaseRowPrefixGeoWrapper(missedGeo.getL1PK(),
					missedGeo.getL2PK(), missedGeo.getL3PK(), missedGeo.getL1Name(), missedGeo.getL2Name(),
					missedGeo.getL3Name());
			addRowPrefixForGeo(allHbaseRowPrefix, maxRowPrefix, missedGeoL3,availableRowPrefix);

		}
  }
  

private void addRowPrefixForGeo(Set<Integer> allHbaseRowPrefix,
		Set<HbaseRowPrefixGeoWrapper> maxRowPrefix,HbaseRowPrefixGeoWrapper missedGeo,
		Set<HbaseRowPrefixGeoWrapper> availableRowPrefix) throws Exception {
	
	Set<HbaseRowPrefixGeoWrapper> maxRowPrefixClone=new HashSet<HbaseRowPrefixGeoWrapper>(maxRowPrefix);
	
	for (HbaseRowPrefixGeoWrapper hbaseRowPrefixGeoWrapper : maxRowPrefix) {
		HbaseRowPrefixGeoWrapper missedGeoClone= new  HbaseRowPrefixGeoWrapper(missedGeo);
		missedGeoClone.setVendor(hbaseRowPrefixGeoWrapper.getVendor());
		missedGeoClone.setDomain(hbaseRowPrefixGeoWrapper.getDomain());
		
		if(availableRowPrefix.contains(missedGeoClone)) {
			continue;
		}
    
		if (!StringUtils.equalsIgnoreCaseAny(hbaseRowPrefixGeoWrapper.getDomain(), "POWER")) {
	        if (allHbaseRowPrefix.contains((hbaseRowPrefixGeoWrapper.getNumericPrefix() + 1))) {
	          throw new BusinessException("Numeric Prefix is already reserved. Code:"
	              + (hbaseRowPrefixGeoWrapper.getNumericPrefix() + 1) + " Domain:" + hbaseRowPrefixGeoWrapper.getDomain()
	              + " Vendor:" + hbaseRowPrefixGeoWrapper.getVendor());
	        }
      }
	
		insertHbaseRowPrefix(missedGeoClone,hbaseRowPrefixGeoWrapper);
		allHbaseRowPrefix.add(hbaseRowPrefixGeoWrapper.getNumericPrefix()+1);
		hbaseRowPrefixGeoWrapper.setNumericPrefix((hbaseRowPrefixGeoWrapper.getNumericPrefix()+1));
		maxRowPrefixClone.add(hbaseRowPrefixGeoWrapper);
		availableRowPrefix.add(missedGeoClone);
	}
	
	maxRowPrefix.clear();
	
	maxRowPrefix.addAll(maxRowPrefixClone);
	
}
  
  private void insertHbaseRowPrefix(HbaseRowPrefixGeoWrapper missedGeo,HbaseRowPrefixGeoWrapper hbaseRowPrefixGeoWrapper) throws Exception {
	  
	  String alphanumericCode=HBaseRowPrefixUtils.getAlphaNumericPrefix(hbaseRowPrefixGeoWrapper.getNumericPrefix()+1);
	  
	  Integer newRecordStatus=hBaseRowPrefixDao.insertRecord(hbaseRowPrefixGeoWrapper.getDomain(), 
			  hbaseRowPrefixGeoWrapper.getVendor(), hbaseRowPrefixGeoWrapper.getNumericPrefix()+1,
			  alphanumericCode,missedGeo.getL1PK(),missedGeo.getL2PK(),missedGeo.getL3PK(),
			  missedGeo.getL1Name(),missedGeo.getL2Name(),missedGeo.getL3Name());
	  
	  logger.info("Insertion success {}, {} ,{} , {},{} ,{}",newRecordStatus,hbaseRowPrefixGeoWrapper.getDomain(),
			  hbaseRowPrefixGeoWrapper.getVendor(),(hbaseRowPrefixGeoWrapper.getNumericPrefix()+1),alphanumericCode,
			  (missedGeo.getL3PK()!=null?"L3: "+missedGeo.getL3PK()
					  :missedGeo.getL2PK()!=null?"L2: "+missedGeo.getL2PK()
							  :missedGeo.getL1PK()!=null?"L1: "+missedGeo.getL1PK():null));
}

  @Override
  @Cacheable(cacheNames = "HBaseRowPrefixCache")
	public RowPrefix getPanLevelPrefixByVendorAndDomain(String domain, String vendor) {
		logger.info("Inside getPanLevelPrefixByVendorAndDomain domain {}, vendor {}", domain, vendor);
		RowPrefix prefix = null;
		if (StringUtils.isEmpty(vendor) && StringUtils.isEmpty(domain)) {
			logger.info("Can not find prefix for domain {} and vendor {}", domain, vendor);
		} else {
			prefix = hBaseRowPrefixDao.getPanLevelPrefixByVendorAndDomain(domain, vendor);
		}
		return prefix;
	}
  
	@Override
	@Cacheable(cacheNames = "HBaseRowPrefixCache")
	public List<RowPrefixRange> getPrefixByMultiDomainVendorGeographyL1(List<String> domain, List<String> vendor,
			List<Integer> geographyPk) {
		Validate.checkNotNull(geographyPk, "GeographyL1Pk is null");
		List<RowPrefixRange> prefixs = null;
		if (CollectionUtils.isEmpty(domain) && CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiGeographyL1ID(geographyPk);
		} else if (CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiGeographyL1ID(domain, geographyPk);
		} else if (CollectionUtils.isEmpty(domain)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiVendorAndMultiGeographyL1ID(vendor, geographyPk);
		} else {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL1ID(domain, vendor,
					geographyPk);
		}
		return Validate.checkNotEmpty(prefixs,
				"No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
	}

	@Override
	@Cacheable(cacheNames = "HBaseRowPrefixCache")
	public List<RowPrefixRange> getPrefixByMultiDomainVendorGeographyL2(List<String> domain, List<String> vendor,
			List<Integer> geographyPk) {
		Validate.checkNotNull(geographyPk, "GeographyL2Pk is null");
		List<RowPrefixRange> prefixs = null;
		if (CollectionUtils.isEmpty(domain) && CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiGeographyL2ID(geographyPk);
		} else if (CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiGeographyL2ID(domain, geographyPk);
		} else if (CollectionUtils.isEmpty(domain)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiVendorAndMultiGeographyL2ID(vendor, geographyPk);
		} else {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL2ID(domain, vendor,
					geographyPk);
		}
		return Validate.checkNotEmpty(prefixs,
				"No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
	}

	@Override
	@Cacheable(cacheNames = "HBaseRowPrefixCache")
	public List<RowPrefix> getPrefixByMultiDomainVendorGeographyL3(List<String> domain, List<String> vendor,
			List<Integer> geographyPk) {
		Validate.checkNotNull(geographyPk, "GeographyL3Pk is null");
		List<RowPrefix> prefixs = null;
		if (CollectionUtils.isEmpty(domain) && CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiGeographyL3ID(geographyPk);
		} else if (CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiGeographyL3ID(domain, geographyPk);
		} else if (CollectionUtils.isEmpty(domain)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiVendorAndMultiGeographyL3ID(vendor, geographyPk);
		} else {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiVendorAndMultiGeographyL3ID(domain, vendor,
					geographyPk);
		}
		return Validate.checkNotEmpty(prefixs,
				"No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
	}

	@Override
	@Cacheable(cacheNames = "HBaseRowPrefixCache")
	public List<RowPrefix> getPrefixByMultiDomainVendorCoreGeography(List<String> domain, List<String> vendor,
			List<Integer> geographyPk) {
		Validate.checkNotNull(geographyPk, "Core geographyPk is null");
		List<RowPrefix> prefixs = null;
		if (CollectionUtils.isEmpty(domain) && CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByCoreMultiGeographyID(geographyPk);
		} else if (CollectionUtils.isEmpty(vendor)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiCoreGeographyID(domain, geographyPk);
		} else if (CollectionUtils.isEmpty(domain)) {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiVendorAndMultiCoreGeographyID(vendor, geographyPk);
		} else {
			prefixs = hBaseRowPrefixDao.getPrefixByMultiDomainAndMultiVendorAndMultiCoreGeographyID(domain, vendor,
					geographyPk);
		}
		return Validate.checkNotEmpty(prefixs,
				"No prefix found for domain=" + domain + ", vendor=" + vendor + " and geographyPk=" + geographyPk);
	}
}
