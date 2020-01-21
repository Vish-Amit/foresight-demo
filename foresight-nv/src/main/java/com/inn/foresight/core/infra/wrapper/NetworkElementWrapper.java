package com.inn.foresight.core.infra.wrapper;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class NetworkElementWrapper.
 */
@RestWrapper
@JpaWrapper
public class NetworkElementWrapper {

  /** The id. */
  private Integer id;

  /** The sapid. */
  private String sapid;

  /** The status. */
  private String status;

  /** The cell id. */
  private Integer cellId;

  /** The earfcn. */
  private Integer earfcn;

  /** The enb type. */
  private String enbType;

  /** The site name. */
  private String siteName;

  /** The band. */
  private String band;

  /** The operation status. */
  private String operationStatus;

  /** The admin status. */
  private String adminStatus;

  /** The geography L 2. */
  private String geographyL2;

  /** The geography L 3. */
  private String geographyL3;

  /** The geography L 1. */
  private String geographyL1;

  /** The geography L 4. */
  private String geographyL4;

  /** The sector id. */
  private Integer sectorId;

  /** The parsed flag. */
  private String parsedFlag;

  /** The date. */
  private String date;


  /** The carrier. */
  private String carrier;


  /** The time. */
  private String time;

  /** The cell name. */
  private String cellName;

  /** The master small cell id. */
  private Integer masterSmallCellId;

  /** The vendor. */
  private String vendor;

  /** The antenna height. */
  private Double antennaHeight;

  /** The mech tilt. */
  private Integer mechTilt;

  /** The elec tilt. */
  private Integer elecTilt;

  /** The antenna type. */
  private String antennaType;

  /** The ecgi. */
  private String ecgi;

  /** The azimuth. */
  private Integer azimuth;

  /** The enb id. */
  private Integer enbId;

  /** The total count. */
  private Long totalCount;

  private Long viewPortCount;

  private String clutterCategory;


  /** The technology. */
  private String technology;

  /** The ne frequency. */
  private String neFrequency;

  /** The latitude. */
  private Double latitude;

  /** The longitude. */
  private Double longitude;

  /** The latitude. */
  private Double centroidLat;

  /** The longitude. */
  private Double centroidLong;

  /** The pci. */
  private Integer pci;

  /** The centroid. */
  private Double[] centroid;

  /** The geography name. */
  private String geographyName;

  /** The is highly utilised. */
  private Boolean isHighlyUtilised;

  /** The is highly utilised. */
  private Boolean isHighlyUtilisedOne;

  /** The is highly utilised. */
  private Boolean isHighlyUtilisedTwo;

  /** The ne type. */
  private NEType neType;

  /** The progress state. */
  private String progressState;

  /** The modified time. */
  private Date modifiedTime;

  /** The display name. */
  private String displayName;

  /** The ne id. */
  private String neId;

  /** The domain. */
  private String domain;

  /** The mcc. */
  private Integer mcc;

  /** The mnc. */
  private Integer mnc;

  /** The min limit. */
  private Integer minLimit;

  /** The geography L 2 id. */
  private Integer geographyL2Id;

  private Integer geographyL3Id;

  /** The geography L4 id. */
  private Integer geographyL4Id;

  private Integer otherGeographyId;

  /** The software version. */
  private String softwareVersion;

  /** The host name. */
  private String hostName;

  /** The color. */
  private String color;

  /** The network element. */
  private NetworkElement networkElement;

  /** The task progress. */
  private Integer taskProgress;

  /** The current stage. */
  private String currentStage;

  /** The last updated date. */
  private String lastUpdatedDate;

  /** The updated date. */
  private Date updatedDate;


  /**
   * Instantiates a new network element wrapper.
   */
  private Long count;

  /** The ne status. */
  private String neStatus;

  /** The ne name. */
  private String neName;

  /** The parent NE id. */
  private String parentNEId;

  /** The vvip. */
  private Boolean vvip;

  /** The cgi. */
  private Integer cgi;

  /** The kpi id. */
  private String kpiName;

  /** The kpi value. */
  private Object kpiValue;

  private String kpiUnit;

  private Boolean locked;

  private Integer dlBandwidth;

  private String morphology;

  private String parentName;

  private String otherGeography;

  private String backHaulMedia;

  private Date onAirDate;

  private Integer numberOfSector;

  private String siteAddress;

  private String siteCategory;

  private String ContactName;

  private String ContactNumber;

  private String neSource;

  private String sourceDate;

  private String emsHostName;

  private String emsIp;
  
  private String emsLive;
	
  private String bandwidth;
	
  private String bandwidthFourthSector;
	
  private Date onAirDateFourthSector;
  
  private String emsLiveFourthSector;
  
  private String neStatusFourthSector;
  
  private Long fddCellCount;
  
  private Long tddCellCount;
  
  private String duplex;
  
  private String ipv4;
  
  private String model;
  
  private Long vcuCount;
  
  private Long riuCount;
  
  private Long rrhCount;
  
  private Map<String,Set<String>> effectedNode;
  
  private String tagStatus;

  private String userlabel;
  
  private String fopsCluster;
  
  private Long cellCount;

  
  public NetworkElementWrapper(NEStatus neStatus,Long count) {
	  if(neStatus!=null) {
		  this.neStatus=neStatus.name();
		  this.count=count;
	  }
  }
  
  
  //Topology RAN Access Report ,Query NAME:getNEModelByNEId
  public NetworkElementWrapper(String neId,String nename,String model,Integer mcc) {
	  super();
	  this.neId=neId;
	  this.neName=nename;
	  this.model=model;
	  this.mcc=mcc;
	
  }
  //Topology RAN
  public NetworkElementWrapper(String neName,Integer enbId,String duplex) {
	  super();
	  this.neName=neName;
	  this.duplex=duplex;
	  this.enbId=enbId;
  }
 
  public NetworkElementWrapper(Long riuCount,Long rrhCount,String geographyName,Double latitude ,Double longitude,Integer id,String model) {
      super();
      this.riuCount = riuCount;
      this.rrhCount = rrhCount;
      this.geographyName = geographyName;
      this.latitude = latitude;
      this.longitude = longitude;
      this.id= id;
      this.model = model;
  }
  
      
  public NetworkElementWrapper(Long riuCount,Long rrhCount,String geographyName,Double latitude ,Double longitude,Integer id) {
      super();
      this.riuCount = riuCount;
      this.rrhCount = rrhCount;
      this.geographyName = geographyName;
      this.latitude = latitude;
      this.longitude = longitude;
      this.id= id;
  }
  
      public NetworkElementWrapper(Long vcuCount, Long riuCount, Long rrhCount) {
        super();
        this.vcuCount = vcuCount;
        this.riuCount = riuCount;
        this.rrhCount = rrhCount;
    }

public NetworkElementWrapper(Long totalCount,String geographyName,Double latitude ,Double longitude) {
      super();
      this.totalCount = totalCount;
      this.geographyName = geographyName;
      this.latitude = latitude;
      this.longitude = longitude;
  }
  
  public NetworkElementWrapper(String neFrequency,Date onAirDate, String emsLive, NEStatus neStatus,Integer sectorId,
		  String bandwidth,String bandwidthFourthSector, Date onAirDateFourthSector,String emsLiveFourthSector,String neStatusFourthSector) {
	        super();
	        this.neFrequency = neFrequency;
	        this.onAirDate=onAirDate;
	        this.emsLive=emsLive;
	        this.neStatus=neStatus.name();
	        this.sectorId=sectorId;
	        this.bandwidth=bandwidth;
	        this.bandwidthFourthSector=bandwidthFourthSector;
	        this.onAirDateFourthSector=onAirDateFourthSector;
	        this.emsLiveFourthSector=emsLiveFourthSector;
	        this.neStatusFourthSector=neStatusFourthSector;
	    }

  public NetworkElementWrapper(String neName, String neFrequency, NEStatus neStatus, Date onAirDate,
      String currentStage, String backHaulMedia, Integer numberOfSector) {
    super();
    this.neName = neName;
    this.neFrequency = neFrequency;
    this.neStatus = neStatus.name();
    this.onAirDate = onAirDate;
    this.currentStage = currentStage;
    this.backHaulMedia = backHaulMedia;
    this.numberOfSector = numberOfSector;
  }
  
  public NetworkElementWrapper(String neId, NEType neType, NEStatus neStatus,Domain domain,Vendor vendor,String ipv4,Long cellCount) {
      super();
      this.neId =neId ;
      this.neType = neType;
      this.neStatus = neStatus!=null ? neStatus.name() :null;
      this.domain = domain!=null ? domain.name(): null;
      this.vendor = vendor!=null ? vendor.name() : null;
      this.ipv4 = ipv4;
      this.cellCount = cellCount;
  }

  
  /**
 * @param neName
 * @param neType
 * @param neStatus
 * @param technology
 * @param duplex
 * @param geographyL4
 * @param geographyL3
 * @param geographyL2
 * @param geographyL1
 * @param fddCellCount
 * @param tddCellCount
 */
public NetworkElementWrapper(String neName,NEType neType,NEStatus neStatus,Technology technology,
		  String duplex, String geographyL4, String geographyL3, String geographyL2,
			String geographyL1, Long fddCellCount, Long tddCellCount)    
	   {
		this.neName = neName;
		
		if (neType != null) {
		this.neType = neType;
		}
		
		if (neStatus != null) {
		this.neStatus = neStatus.displayName();
		}
		
		if (technology != null) {
		this.technology = technology.displayName();
		}
		
		this.duplex = duplex;
		this.geographyL2 = geographyL4;
		this.geographyL3 = geographyL3;
		this.geographyL1 = geographyL2;
		this.geographyL4 = geographyL1;
		this.fddCellCount = fddCellCount;
		this.tddCellCount = tddCellCount;

	}
 
  
  /**
 * @param neName
 * @param ipv4
 * @param neType
 * @param vendor
 * @param domain
 * @param model
 * @param softwareVersion
 */
public NetworkElementWrapper(String neName, String ipv4, NEType neType,Vendor vendor, Domain domain,
			 String model, String softwareVersion) {
		super();
		this.neName = neName;
		this.ipv4 = ipv4;
		this.neType = neType;
		this.vendor = vendor.displayName();
		this.domain = domain.displayName();
		this.model = model;
		this.softwareVersion = softwareVersion;
	
	}
 
  
  public NetworkElementWrapper(String tagStatus, String userlabel, String clusterFOPS) {
	super();
	this.tagStatus = tagStatus;
	this.userlabel = userlabel;
	this.fopsCluster = clusterFOPS;
}

public String getParentName() {
	    return parentName;
	  }


public void setParentName(String parentName) {
    this.parentName = parentName;
  }



  public String getOtherGeography() {
    return otherGeography;
  }


  public void setOtherGeography(String otherGeography) {
    this.otherGeography = otherGeography;
  }



  /**
 
 * @param status
 * @param enbType
 * @param geographyL2
 * @param geographyL3
 * @param geographyL1
 * @param geographyL4
 * @param technology
 * @param neFrequency
 * @param neType
 * @param geographyL2Id
 * @param geographyL3Id
 * @param geographyL4Id
 * @param neStatus
 
 */
  public NetworkElementWrapper(String neName,NEType neType,NEStatus neStatus,Technology technology,Long count,String duplex, String geographyL4, String geographyL3,
			String geographyL2, String geographyL1)    
	   {
		super();
		
		this.neName = neName;
		this.neType = neType;
		this.neStatus = neStatus.displayName();
		this.technology = technology.displayName();
		this.count = count;
		this.duplex = duplex;
		this.geographyL2 = geographyL4;
		this.geographyL3 = geographyL3;
		this.geographyL1 = geographyL2;
		this.geographyL4 = geographyL1;

	}


/**
   * Gets the count.
   *
   * @return the count
   */
  public Long getCount() {
    return count;
  }

  /**
   * Sets the count.
   *
   * @param count the new count
   */
  public void setCount(Long count) {
    this.count = count;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param neType the ne type
   * @param count the count
   */
  public NetworkElementWrapper(NEType neType, Long count) {
    super();
    this.neType = neType;
    this.count = count;
  }

  public NetworkElementWrapper(String emsHostName, String emsIp, String neName, String swVersion,
      String geographyL4Name) {
    this.emsHostName = emsHostName;
    this.emsIp = emsIp;
    this.neName = neName;
    this.softwareVersion = swVersion;
    this.geographyL4 = geographyL4Name;

  }

  

  public String getEmsHostName() {
    return emsHostName;
  }

  public void setEmsHostName(String emsHostName) {
    this.emsHostName = emsHostName;
  }

  public String getEmsIp() {
    return emsIp;
  }

  public void setEmsIp(String emsIp) {
    this.emsIp = emsIp;
  }

  /**
   * Instantiates a new network element wrapper.
   */
  public NetworkElementWrapper() {

  }

  public NetworkElementWrapper(NEType neType, String parentNEId, String neId, Vendor vendor,
      Domain domain, Double latitude, Double longitude, String geographyL4, String geographyL3,
      String geographyL2, String geographyL1, String neName, Integer geographyL3Pk) {
    super();
    this.neType = neType;
    this.parentNEId = parentNEId;
    this.neId = neId;
    this.vendor = vendor.name();
    this.domain = domain.name();
    this.latitude = latitude;
    this.longitude = longitude;
    this.geographyL4 = geographyL4;
    this.geographyL3 = geographyL3;
    this.geographyL2 = geographyL2;
    this.geographyL1 = geographyL1;
    this.neName = neName;
    this.id = geographyL3Pk;
  }


  /**
   * Instantiates a new network element wrapper.
   *
   * @param domain the domain
   * @param vendor the vendor
   * @param technology the technology
   */
  // getDistinctInfoFromNetworkElement
  public NetworkElementWrapper(Domain domain, Vendor vendor, Technology technology) {
    this.domain = domain.name();
    this.vendor = vendor.name();
    this.technology = technology.displayName();
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param band the band
   * @param totalCount the total count
   */
  public NetworkElementWrapper(String band, Long totalCount) {
    this.band = band;
    this.totalCount = totalCount;
  }



  public NetworkElementWrapper(Integer cellId, Integer enodeBId, String neName, String band,
      Integer id) {
    this.cellId = cellId;
    this.enbId = enodeBId;
    this.sapid = neName;
    this.band = band;
    this.id = id;
  }

  /**
   * get neId sapid and geographyL2 for pm.
   *
   * @param cellId the cell id
   * @param sapid the sapid
   * @param geographyL2 the geography L 2
   */
  public NetworkElementWrapper(Integer cellId, String sapid, String geographyL2) {
    super();
    this.cellId = cellId;
    this.sapid = sapid;
    this.geographyL2 = geographyL2;
  }

  /**
   * get neId sapid and geographyL2 and geographyL3 for pm.
   *
   * @param cellId the cell id
   * @param sapid the sapid
   * @param geographyL2 the geography L 2
   * @param geographyL3 the geography L 3
   */
  public NetworkElementWrapper(Integer cellId, String sapid, String geographyL2,
      String geographyL3) {
    super();
    this.cellId = cellId;
    this.sapid = sapid;
    this.geographyL2 = geographyL2;
    this.geographyL3 = geographyL3;
  }

  /**
   * get geographyL2 and geographyName for pm.
   *
   * @param geographyL2 the geography L 2
   * @param geographyName the geography name
   */
  public NetworkElementWrapper(String geographyL2, String geographyName) {
    super();
    this.geographyL2 = geographyL2;
    this.geographyName = geographyName;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param technology the technology
   * @param id the id
   * @param vendor the vendor
   * @param adminState the admin state
   * @param emsStatus the ems status
   * @param neName the ne name
   * @param neType the ne type
   * @param cellId the cell id
   * @param band the band
   * @param geographyL2 the geography L 2
   * @param geographyL3 the geography L 3
   * @param geographyL1 the geography L 1
   * @param sector the sector
   * @param geographyL4 the geography L 4
   * @param cellName the cell name
   */
  public NetworkElementWrapper(String technology, Integer id, String vendor, String adminState,
      String emsStatus, String neName, NEType neType, Integer cellId, String band,
      String geographyL2, String geographyL3, String geographyL1, Integer sector,
      String geographyL4, String cellName) {
    this.technology = technology;
    this.id = id;
    this.vendor = vendor.toString();
    this.adminStatus = adminState;
    this.operationStatus = emsStatus;
    this.sapid = neName;
    if (neType != null)
      this.enbType = neType.displayName();
    this.cellId = cellId;
    this.geographyL2 = geographyL2;
    this.geographyL1 = geographyL1;
    this.geographyL4 = geographyL4;
    this.geographyL3 = geographyL3;
    this.band = band;
    this.sectorId = sector;
    this.cellName = cellName;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param id the id
   * @param sector the sector
   * @param neId the ne id
   * @param neName the ne name
   * @param cellId the cell id
   * @param band the band
   * @param azimuth the azimuth
   * @param height the height
   * @param mechTilt the mech tilt
   * @param elecTilt the elec tilt
   * @param antenantype the antenantype
   */
  public NetworkElementWrapper(Integer id, Integer sector, String neId, String neName,
      Integer cellId, String band, Integer azimuth, Double height, Integer mechTilt,
      Integer elecTilt, String antenantype) {
    this.id = id;
    this.sectorId = sector;
    this.neId = neId;
    this.sapid = neName;
    this.cellId = cellId;
    this.band = band;
    this.azimuth = azimuth;
    this.antennaType = antenantype;
    this.antennaHeight = height;
    this.elecTilt = elecTilt;
    this.mechTilt = mechTilt;
    this.antennaType = antenantype;
  }


  /* NetworkElement/getGeographyL4ByneName */
  public NetworkElementWrapper(String neId, String neName, String geographyL4Name,
      Integer geographyL4Id, Double latitude, Double longitude) {
    this.neId = neId;
    this.sapid = neName;
    this.geographyL4Id = geographyL4Id;
    this.geographyL4 = geographyL4Name;
    this.centroid = new Double[] {latitude, longitude};
  }

  /* MacroSiteDetail/SmallCellSiteDetail */

  /**
   * Instantiates a new network element wrapper.
   *
   * @param technology the technology
   * @param id the id
   * @param vendor the vendor
   * @param adminState the admin state
   * @param emsStatus the ems status
   * @param neName the ne name
   * @param neType the ne type
   * @param cellId the cell id
   * @param band the band
   * @param geographyL2 the geography L 2
   * @param geographyL3 the geography L 3
   * @param geographyL1 the geography L 1
   * @param geographyL4 the geography L 4
   * @param neId the ne id
   * @param sector the sector
   * @param cellName the cell name
   */
  public NetworkElementWrapper(Technology technology, Integer id, Vendor vendor, String adminState,
      String emsStatus, String neName, NEType neType, Integer cellId, String band,
      String geographyL2, String geographyL3, String geographyL1, String geographyL4, String neId,
      Integer sector, String cellName) {
    if (technology != null)
      this.technology = technology.displayName();
    this.id = id;
    if (vendor != null)
      this.vendor = vendor.name();
    this.adminStatus = adminState;
    this.operationStatus = emsStatus;
    if (neType != null)
      this.enbType = neType.displayName();
    this.cellId = cellId;
    this.geographyL2 = geographyL2;
    this.geographyL1 = geographyL1;
    this.geographyL4 = geographyL4;
    this.geographyL3 = geographyL3;
    this.band = band;
    this.sectorId = sector;
    this.neId = neId;
    this.sapid = neName;
    this.cellName = cellName;
  }

  // Tracport Workorder site wrapper

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param enbId the enb id
   * @param mnc the mnc
   * @param mcc the mcc
   * @param geographyL4 the geography L 4
   */
  public NetworkElementWrapper(String sapid, Integer enbId, Integer mnc, Integer mcc,
      String geographyL4) {
    super();
    this.sapid = sapid;
    this.geographyL4 = geographyL4;
    this.enbId = enbId;
    this.mcc = mcc;
    this.mnc = mnc;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param neName the ne name
   * @param vendor the vendor
   * @param neId the ne id
   * @param geographyL4 the geography L 4
   * @param neType the ne type
   * @param latitude the latitude
   * @param longitude the longitude
   * @param neStatus the ne status
   * @param domain the domain
   * @param technology the technology
   */
  // wifi Actual site Visualisation
  public NetworkElementWrapper(String neName, Vendor vendor, String neId, String geographyL4,
      NEType neType, Double latitude, Double longitude, NEStatus neStatus, Domain domain,
      Technology technology) {
    super();
    this.neName = neName;
    this.displayName = neName;
    if (vendor != null) {
      this.vendor = vendor.toString();
    }
    this.neId = neId;
    this.geographyL4 = geographyL4;
    if (neType != null) {
      this.enbType = neType.name();
      this.neType = neType;
    }
    this.latitude = latitude;
    this.longitude = longitude;
    if (neStatus != null) {
      this.neStatus = neStatus.name();
      this.status = neStatus.name();
      this.progressState = neStatus.name();
    }
    if (domain != null) {
      this.domain = domain.name();
    }
    if (technology != null) {
      this.technology = technology.name();
    }
    this.totalCount = 1L;

  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param id the id
   * @param neId the ne id
   * @param domain the domain
   * @param vendor the vendor
   * @param softwareVersion the software version
   * @param neType the ne type
   * @param geographyL2Id the geography L 2 id
   * @param mnc the mnc
   * @param mcc the mcc
   * @param hostName the host name
   */
  // getNEInfoByNeId
  public NetworkElementWrapper(Integer id, String neId, Domain domain, Vendor vendor,
      String softwareVersion, NEType neType, Integer geographyL2Id, Integer mnc, Integer mcc,
      String hostName) {
    this.id = id;
    this.neId = neId;
    if (domain != null) {
      this.domain = domain.toString();
    }
    if (vendor != null) {
      this.vendor = vendor.toString();
    }
    this.softwareVersion = softwareVersion;
    if (neType != null) {
      this.enbType = neType.toString();
    }
    this.geographyL2Id = geographyL2Id;
    this.mnc = mnc;
    this.mcc = mcc;
    this.hostName = hostName;
  }

  // wifi
  public NetworkElementWrapper(String neName, Vendor vendor, String neFrequency, String neId,
      String geographyL4, NEType neType, Double latitude, Double longitude, NEStatus neStatus,
      Domain domain, Technology technology) {
    super();
    this.neName = neName;
    this.displayName = neName;
    if (vendor != null) {
      this.vendor = vendor.toString();
    }
    this.neFrequency = neFrequency;
    this.neId = neId;
    this.geographyL4 = geographyL4;
    if (neType != null) {
      this.enbType = neType.name();
      this.neType = neType;
    }
    this.latitude = latitude;
    this.longitude = longitude;
    if (neStatus != null) {
      this.neStatus = neStatus.name();
      this.status = neStatus.name();
      this.progressState = neStatus.name();
    }
    if (domain != null) {
      this.domain = domain.name();
    }
    if (technology != null) {
      this.technology = technology.name();
    }
    this.totalCount = 1L;
  }


  public NetworkElementWrapper(String domain, String vendor, String neId, String geographyL3) {
    super();
    this.geographyL3 = geographyL3;
    this.vendor = vendor;
    this.neId = neId;
    this.domain = domain;
  }

  public NetworkElementWrapper(Domain domain, Vendor vendor, Integer geographyL3Id, String neId) {
    super();
    this.geographyL3Id = geographyL3Id;
    this.vendor = vendor.toString();
    this.neId = neId;
    this.domain = domain.toString();
  }

  public NetworkElementWrapper(String neId, String neName, Double latitude, Double longitude) {
    this.neId = neId;
    this.parentNEId = neName;
    this.latitude = latitude;
    this.longitude = longitude;

  }



  /**
   * Gets the host name.
   *
   * @return the host name
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Sets the host name.
   *
   * @param hostName the new host name
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Gets the geography L 2 id.
   *
   * @return the geography L 2 id
   */
  public Integer getGeographyL2Id() {
    return geographyL2Id;
  }

  public Integer getGeographyL3Id() {
    return geographyL3Id;
  }

  public void setGeographyL3Id(Integer geographyL3Id) {
    this.geographyL3Id = geographyL3Id;
  }

  /**
   * @return the geographyL4Id
   */
  public Integer getGeographyL4Id() {
    return geographyL4Id;
  }

  /**
   * @param geographyL4Id the geographyL4Id to set
   */
  public void setGeographyL4Id(Integer geographyL4Id) {
    this.geographyL4Id = geographyL4Id;
  }

  public Integer getOtherGeographyId() {
    return otherGeographyId;
  }

  public void setOtherGeographyId(Integer otherGeographyId) {
    this.otherGeographyId = otherGeographyId;
  }

  /**
   * Sets the geography L 2 id.
   *
   * @param geographyL2Id the new geography L 2 id
   */
  public void setGeographyL2Id(Integer geographyL2Id) {
    this.geographyL2Id = geographyL2Id;
  }

  /**
   * Gets the software version.
   *
   * @return the software version
   */
  public String getSoftwareVersion() {
    return softwareVersion;
  }

  /**
   * Sets the software version.
   *
   * @param softwareVersion the new software version
   */
  public void setSoftwareVersion(String softwareVersion) {
    this.softwareVersion = softwareVersion;
  }

  /**
   * Gets the domain.
   *
   * @return the domain
   */
  public String getDomain() {
    return domain;
  }

  /**
   * Sets the domain.
   *
   * @param domain the new domain
   */
  public void setDomain(String domain) {
    this.domain = domain;
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public Integer getId() {
    return id;
  }

  /**
   * Sets the id.
   *
   * @param id the new id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Gets the sapid.
   *
   * @return the sapid
   */
  public String getSapid() {
    return sapid;
  }

  /**
   * Sets the sapid.
   *
   * @param sapid the new sapid
   */
  public void setSapid(String sapid) {
    this.sapid = sapid;
  }

  /**
   * Gets the status.
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Sets the status.
   *
   * @param status the new status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Gets the cell id.
   *
   * @return the cell id
   */
  public Integer getCellId() {
    return cellId;
  }

  /**
   * Sets the cell id.
   *
   * @param cellId the new cell id
   */
  public void setCellId(Integer cellId) {
    this.cellId = cellId;
  }

  /**
   * Gets the enb type.
   *
   * @return the enb type
   */
  public String getEnbType() {
    return enbType;
  }

  /**
   * Sets the enb type.
   *
   * @param enbType the new enb type
   */
  public void setEnbType(String enbType) {
    this.enbType = enbType;
  }

  /**
   * Gets the band.
   *
   * @return the band
   */
  public String getBand() {
    return band;
  }

  /**
   * Sets the band.
   *
   * @param band the new band
   */
  public void setBand(String band) {
    this.band = band;
  }

  /**
   * Gets the operation status.
   *
   * @return the operation status
   */
  public String getOperationStatus() {
    return operationStatus;
  }

  /**
   * Sets the operation status.
   *
   * @param operationStatus the new operation status
   */
  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }

  /**
   * Gets the admin status.
   *
   * @return the admin status
   */
  public String getAdminStatus() {
    return adminStatus;
  }

  /**
   * Sets the admin status.
   *
   * @param adminStatus the new admin status
   */
  public void setAdminStatus(String adminStatus) {
    this.adminStatus = adminStatus;
  }

  /**
   * Gets the parsed flag.
   *
   * @return the parsed flag
   */
  public String getParsedFlag() {
    return parsedFlag;
  }

  /**
   * Sets the parsed flag.
   *
   * @param parsedFlag the new parsed flag
   */
  public void setParsedFlag(String parsedFlag) {
    this.parsedFlag = parsedFlag;
  }

  /**
   * Gets the date.
   *
   * @return the date
   */
  public String getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date the new date
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * Gets the time.
   *
   * @return the time
   */
  public String getTime() {
    return time;
  }

  /**
   * Sets the time.
   *
   * @param time the new time
   */
  public void setTime(String time) {
    this.time = time;
  }

  /**
   * Gets the cell name.
   *
   * @return the cell name
   */
  public String getCellName() {
    return cellName;
  }

  /**
   * Sets the cell name.
   *
   * @param cellName the new cell name
   */
  public void setCellName(String cellName) {
    this.cellName = cellName;
  }

  /**
   * Gets the master small cell id.
   *
   * @return the master small cell id
   */
  public Integer getMasterSmallCellId() {
    return masterSmallCellId;
  }

  /**
   * Sets the master small cell id.
   *
   * @param masterSmallCellId the new master small cell id
   */
  public void setMasterSmallCellId(Integer masterSmallCellId) {
    this.masterSmallCellId = masterSmallCellId;
  }

  /**
   * Gets the vendor.
   *
   * @return the vendor
   */
  public String getVendor() {
    return vendor;
  }

  /**
   * Sets the vendor.
   *
   * @param vendor the new vendor
   */
  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  /**
   * Gets the antenna height.
   *
   * @return the antenna height
   */
  public Double getAntennaHeight() {
    return antennaHeight;
  }

  /**
   * Sets the antenna height.
   *
   * @param antennaHeight the new antenna height
   */
  public void setAntennaHeight(Double antennaHeight) {
    this.antennaHeight = antennaHeight;
  }

  /**
   * Gets the mech tilt.
   *
   * @return the mech tilt
   */
  public Integer getMechTilt() {
    return mechTilt;
  }

  /**
   * Sets the mech tilt.
   *
   * @param mechTilt the new mech tilt
   */
  public void setMechTilt(Integer mechTilt) {
    this.mechTilt = mechTilt;
  }

  /**
   * Gets the elec tilt.
   *
   * @return the elec tilt
   */
  public Integer getElecTilt() {
    return elecTilt;
  }

  /**
   * Checks if is vvip.
   *
   * @return true, if is vvip
   */
  public Boolean getVvip() {
    return vvip;
  }

  /**
   * Sets the vvip.
   *
   * @param vvip the new vvip
   */
  public void setVvip(Boolean vvip) {
    this.vvip = vvip;
  }

  /**
   * Sets the elec tilt.
   *
   * @param elecTilt the new elec tilt
   */
  public void setElecTilt(Integer elecTilt) {
    this.elecTilt = elecTilt;
  }

  /**
   * Gets the antenna type.
   *
   * @return the antenna type
   */
  public String getAntennaType() {
    return antennaType;
  }

  /**
   * Sets the antenna type.
   *
   * @param antennaType the new antenna type
   */
  public void setAntennaType(String antennaType) {
    this.antennaType = antennaType;
  }

  /**
   * Gets the azimuth.
   *
   * @return the azimuth
   */
  public Integer getAzimuth() {
    return azimuth;
  }

  /**
   * Sets the azimuth.
   *
   * @param azimuth the new azimuth
   */
  public void setAzimuth(Integer azimuth) {
    this.azimuth = azimuth;
  }

  /**
   * Gets the enb id.
   *
   * @return the enb id
   */
  public Integer getEnbId() {
    return enbId;
  }

  /**
   * Sets the enb id.
   *
   * @param enbId the new enb id
   */
  public void setEnbId(Integer enbId) {
    this.enbId = enbId;
  }

  /**
   * Gets the total count.
   *
   * @return the total count
   */
  public Long getTotalCount() {
    return totalCount;
  }

  /**
   * Sets the total count.
   *
   * @param totalCount the new total count
   */
  public void setTotalCount(Long totalCount) {
    this.totalCount = totalCount;
  }

  /**
   * Gets the technology.
   *
   * @return the technology
   */
  public String getTechnology() {
    return technology;
  }

  /**
   * Sets the technology.
   *
   * @param technology the new technology
   */
  public void setTechnology(String technology) {
    this.technology = technology;
  }

  /**
   * Gets the sector id.
   *
   * @return the sector id
   */
  public Integer getSectorId() {
    return sectorId;
  }

  /**
   * Sets the sector id.
   *
   * @param sectorId the new sector id
   */
  public void setSectorId(Integer sectorId) {
    this.sectorId = sectorId;
  }

  /**
   * Gets the ne frequency.
   *
   * @return the ne frequency
   */
  public String getNeFrequency() {
    return neFrequency;
  }

  /**
   * Sets the ne frequency.
   *
   * @param neFrequency the new ne frequency
   */
  public void setNeFrequency(String neFrequency) {
    this.neFrequency = neFrequency;
  }

  /**
   * Gets the latitude.
   *
   * @return the latitude
   */
  public Double getLatitude() {
    return latitude;
  }

  /**
   * Sets the latitude.
   *
   * @param latitude the new latitude
   */
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  /**
   * Gets the longitude.
   *
   * @return the longitude
   */
  public Double getLongitude() {
    return longitude;
  }

  /**
   * Sets the longitude.
   *
   * @param longitude the new longitude
   */
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  /**
   * Gets the pci.
   *
   * @return the pci
   */
  public Integer getPci() {
    return pci;
  }

  /**
   * Sets the pci.
   *
   * @param pci the new pci
   */
  public void setPci(Integer pci) {
    this.pci = pci;
  }

  /**
   * Gets the geography name.
   *
   * @return the geography name
   */
  public String getGeographyName() {
    return geographyName;
  }

  /**
   * Sets the geography name.
   *
   * @param geographyName the new geography name
   */
  public void setGeographyName(String geographyName) {
    this.geographyName = geographyName;
  }

  /**
   * Gets the checks if is highly utilised.
   *
   * @return the checks if is highly utilised
   */
  public Boolean getIsHighlyUtilised() {
    return isHighlyUtilised;
  }

  /**
   * Gets the mcc.
   *
   * @return the mcc
   */
  public Integer getMcc() {
    return mcc;
  }

  /**
   * Sets the mcc.
   *
   * @param mcc the new mcc
   */
  public void setMcc(Integer mcc) {
    this.mcc = mcc;
  }

  /**
   * Gets the mnc.
   *
   * @return the mnc
   */
  public Integer getMnc() {
    return mnc;
  }

  /**
   * Sets the mnc.
   *
   * @param mnc the new mnc
   */
  public void setMnc(Integer mnc) {
    this.mnc = mnc;
  }

  /**
   * Sets the checks if is highly utilised.
   *
   * @param isHighlyUtilised the new checks if is highly utilised
   */
  public void setIsHighlyUtilised(Boolean isHighlyUtilised) {
    this.isHighlyUtilised = isHighlyUtilised;
  }

  public Boolean getIsHighlyUtilisedOne() {
    return isHighlyUtilisedOne;
  }

  public void setIsHighlyUtilisedOne(Boolean isHighlyUtilisedOne) {
    this.isHighlyUtilisedOne = isHighlyUtilisedOne;
  }

  public Boolean getIsHighlyUtilisedTwo() {
    return isHighlyUtilisedTwo;
  }

  public void setIsHighlyUtilisedTwo(Boolean isHighlyUtilisedTwo) {
    this.isHighlyUtilisedTwo = isHighlyUtilisedTwo;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param cellName the cell name
   * @param cellId the cell id
   * @param geographyName the geography name
   * @param neType the ne type
   * @param lattitude the lattitude
   * @param longitude the longitude
   * @param macroPci the macro pci
   * @param macroAzimuth the macro azimuth
   * @param progressState the progress state
   * @param sectorId the sector id
   * @param displayName the display name
   */
  /*
   * Used for showing count for zoom level greater than 10 for MACRO/SMALLCELL ne type
   */
  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, String geographyName, NEType neType, Double lattitude, Double longitude,
      Integer macroPci, Integer macroAzimuth, NEStatus progressState, Integer sectorId,
      String displayName) {
    this.sapid = sapid;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    this.cellName = cellName;
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.cellId = cellId;
    this.geographyName = geographyName;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.pci = macroPci;
    this.azimuth = macroAzimuth;
    if (progressState != null) {
      this.progressState = progressState.name();
    }
    this.sectorId = sectorId;
    this.displayName = displayName;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param cellName the cell name
   * @param cellId the cell id
   * @param geographyName the geography name
   * @param neType the ne type
   * @param lattitude the lattitude
   * @param longitude the longitude
   * @param macroPci the macro pci
   * @param macroAzimuth the macro azimuth
   * @param progressState the progress state
   * @param sectorId the sector id
   * @param status the status
   * @param ecgi the ecgi
   * @param neId the ne id
   * @param siteName the site name
   * @param lastUpdatedDate the last updated date
   * @param earfcn the earfcn
   * @param carrier the carrier
   */
  // Actual site visualisation
  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, String geographyName, NEType neType, Double lattitude, Double longitude,
      Integer macroPci, Integer macroAzimuth, NEStatus progressState, Integer sectorId,
      NEStatus status, String ecgi, String neId, String siteName, Date lastUpdatedDate,
      Integer earfcn, String carrier, String morphology,
      Integer enbId, String neName, String parentName, Domain domain, Technology technology,Double antennaHeight , Integer mechTilt , Integer elecTilt) {
    this.sapid = sapid;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    this.cellName = cellName;
    this.neFrequency = neFrequency;
    this.cellId = cellId;
    this.geographyName = geographyName;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.pci = macroPci;
    this.azimuth = macroAzimuth;
    if (progressState != null) {
      this.progressState = progressState.name();
    }
    this.sectorId = sectorId;
    if (status != null) {
      this.status = status.name();
    }
    this.ecgi = ecgi;
    this.neId = neId;
    this.siteName = siteName;
    if (lastUpdatedDate != null) {
      Timestamp ts = new Timestamp(lastUpdatedDate.getTime());
      this.lastUpdatedDate = new SimpleDateFormat("dd-MM-yyyy").format(ts);
    }
    this.earfcn = earfcn;
    this.carrier = carrier;
    /*this.clutterCategory = clutterCategory;
    this.cgi = cgi;*/
    this.totalCount = 1L;
    this.displayName = sapid;
    this.morphology = morphology;
    this.enbId = enbId;
    this.neName = neName;
    this.parentName = parentName;
    if (domain != null) {
      this.domain = domain.name();
    }
    if (technology != null) {
      this.technology = technology.name();
    }
    this.antennaHeight=antennaHeight;
    this.mechTilt=mechTilt;
    this.elecTilt=elecTilt;
  }

  // getConstructorStatementForSmallCell
  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, String geographyName, NEType neType, Double lattitude, Double longitude,
      Integer macroPci, Integer macroAzimuth, NEStatus progressState, Integer sectorId,
      NEStatus status, String ecgi, String neId, String siteName, Date lastUpdatedDate,
      Integer earfcn, Integer enbId, String neName, String parentName,
      Domain domain, Technology technology, Double antennaHeight , Integer mechTilt , Integer elecTilt) {
    this.sapid = sapid;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    this.neFrequency = neFrequency;
    this.cellName = cellName;
    this.cellId = cellId;
    this.geographyName = geographyName;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.pci = macroPci;
    this.azimuth = macroAzimuth;
    if (progressState != null) {
      this.progressState = progressState.name();
    }
    this.sectorId = sectorId;
    if (status != null) {
      this.status = status.name();
    }
    this.ecgi = ecgi;
    this.neId = neId;
    this.siteName = siteName;
    if (lastUpdatedDate != null) {
      Timestamp ts = new Timestamp(lastUpdatedDate.getTime());
      this.lastUpdatedDate = new SimpleDateFormat("dd-MM-yyyy").format(ts);
    }
    this.earfcn = earfcn;
    //this.clutterCategory = clutterCategory;
    this.enbId = enbId;
    this.neName = neName;
    this.parentName = parentName;
    if (domain != null) {
      this.domain = domain.name();
    }
    if (technology != null) {
      this.technology = technology.name();
    }
    this.antennaHeight=antennaHeight;
    this.mechTilt=mechTilt;
    this.elecTilt=elecTilt;
  }

  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, String geographyName, NEType neType, Double lattitude, Double longitude,
      Integer macroPci, Integer macroAzimuth, NEStatus progressState, Integer sectorId,
      NEStatus status, String ecgi, String neId, String siteName, Date lastUpdatedDate,
      Integer earfcn, String clutterCategory) {
    this.sapid = sapid;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    this.cellName = cellName;
    this.neFrequency = neFrequency;
    this.cellId = cellId;
    this.geographyName = geographyName;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.pci = macroPci;
    this.azimuth = macroAzimuth;
    if (progressState != null) {
      this.progressState = progressState.name();
    }
    this.sectorId = sectorId;
    if (status != null) {
      this.status = status.name();
    }
    this.ecgi = ecgi;
    this.neId = neId;
    this.siteName = siteName;
    if (lastUpdatedDate != null) {
      Timestamp ts = new Timestamp(lastUpdatedDate.getTime());
      this.lastUpdatedDate = new SimpleDateFormat("dd-MM-yyyy").format(ts);
    }
    this.earfcn = earfcn;
    this.clutterCategory = clutterCategory;
    this.totalCount = 1L;
    this.displayName = sapid;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param neFrequency the ne frequency
   * @param status the status
   * @param latitude the latitude
   * @param longitude the longitude
   * @param geographyName the geography name
   * @param neType the ne type
   * @param currentStage the current stage
   * @param taskProgress the task progress
   * @param siteName the site name
   * @param lastUpdatedDate the last updated date
   */
  // Planned site visualisation
  public NetworkElementWrapper(String sapid, String neFrequency, NEStatus status, Double latitude,
      Double longitude, String geographyName, NEType neType, String currentStage,
      Integer taskProgress, String siteName, Date lastUpdatedDate) {
    this.sapid = sapid;
    this.neFrequency = neFrequency;
    if (status != null) {
      this.status = status.name();
      this.progressState = status.name();
    }
    this.latitude = latitude;
    this.longitude = longitude;
    this.geographyName = geographyName;
    this.neType = neType;
    this.currentStage = currentStage;
    this.taskProgress = taskProgress;
    this.siteName = siteName;
    Timestamp ts = new Timestamp(lastUpdatedDate.getTime());
    this.lastUpdatedDate = new SimpleDateFormat("dd-MM-yyyy").format(ts);
    this.totalCount = 1L;
    this.displayName = sapid;

  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param cellName the cell name
   * @param cellId the cell id
   * @param neType the ne type
   * @param latitude the latitude
   * @param longitude the longitude
   * @param isHighlyUtilised the is highly utilised
   * @param progressState the progress state
   * @param azimuth the azimuth
   * @param sectorId the sector id
   * @param domain the domain
   * @param technology the technology
   */
  /* Used for Highly Utilised sites */
  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, NEType neType, Double latitude, Double longitude, Boolean isHighlyUtilised,
      Boolean isHighlyUtilisedOne, Boolean isHighlyUtilisedTwo, NEStatus progressState,
      Integer azimuth, Integer sectorId, Domain domain, Technology technology, String carrier,
      Integer pci, Integer dlBandwidth, Date updatedDate) {
    this.sapid = sapid;
    this.longitude = longitude;
    this.isHighlyUtilised = isHighlyUtilised;
    this.isHighlyUtilisedOne = isHighlyUtilisedOne;
    this.isHighlyUtilisedTwo = isHighlyUtilisedTwo;
    this.latitude = latitude;
    this.neFrequency = neFrequency;
    this.azimuth = azimuth;
    this.cellId = cellId;
    if (progressState != null)
      this.progressState = progressState.name();
    this.neType = neType;
    this.sectorId = sectorId;
    if (domain != null)
      this.domain = domain.name();
    this.cellName = cellName;
    if (technology != null)
      this.technology = technology.displayName();
    if (vendor != null)
      this.vendor = vendor.name();
    this.carrier = carrier;
    this.pci = pci;
    this.dlBandwidth = dlBandwidth;
    this.updatedDate = updatedDate;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param geographyName the geography name
   * @param totalCount the total count
   * @param modifiedTime the modified time
   */
  /*
   * Used for getSiteTableDataForGeographyL2,getSiteTableDataForGeographyL3)
   */
  public NetworkElementWrapper(String geographyName, Long totalCount, Date modifiedTime) {
    this.geographyName = geographyName;
    this.totalCount = totalCount;
    this.setModifiedTime(modifiedTime);
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param cellId the cell id
   * @param neType the ne type
   * @param neFrequency the ne frequency
   * @param modifiedTime the modified time
   */
  /* Used for getSiteTableDataForGeographyL4 */
  public NetworkElementWrapper(String sapid, Integer cellId, NEType neType, String neFrequency,
      Date modifiedTime) {
    this.sapid = sapid;
    this.neFrequency = neFrequency;
    this.cellId = cellId;
    this.neType = neType;
    this.modifiedTime = modifiedTime;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param cellName the cell name
   * @param cellId the cell id
   * @param neType the ne type
   * @param latitude the latitude
   * @param longitude the longitude
   * @param progressState the progress state
   * @param azimuth the azimuth
   * @param sectorId the sector id
   * @param domain the domain
   * @param pci the pci
   */
  /* Used for pm Dashboard */
  public NetworkElementWrapper(String sapid, Vendor vendor, String neFrequency, String cellName,
      Integer cellId, NEType neType, Double latitude, Double longitude, NEStatus progressState,
      Integer azimuth, Integer sectorId, Domain domain, Integer pci, String carrier) {
    this.sapid = sapid;
    if (vendor != null)
      this.vendor = vendor.name();
    this.neFrequency = neFrequency;
    this.cellName = cellName;
    this.cellId = cellId;
    this.neType = neType;
    this.latitude = latitude;
    this.longitude = longitude;
    if (progressState != null)
      this.progressState = progressState.name();
    this.azimuth = azimuth;
    this.sectorId = sectorId;
    if (domain != null)
      this.domain = domain.name();
    this.pci = pci;
    this.carrier = carrier;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param neId the ne id
   * @param neName the ne name
   * @param geographyL4Name the geography L 4 name
   * @param geographyL3Name the geography L 3 name
   * @param geographyL2Name the geography L 2 name
   * @param neType the ne type
   */
  public NetworkElementWrapper(String neId, String neName, String geographyL4Name,
      String geographyL3Name, String geographyL2Name, NEType neType) {
    this.geographyL2 = geographyL2Name;
    this.geographyL3 = geographyL3Name;
    this.geographyL4 = geographyL4Name;
    this.sapid = neId;
    this.displayName = neName;
    this.neType = neType;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param id the id
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param neId the ne id
   * @param cellId the cell id
   * @param neType the ne type
   * @param lattitude the lattitude
   * @param longitude the longitude
   * @param neStatus the ne status
   */
  // getNEByGeographyDomainVendorTechAndNeTypeForCell
  public NetworkElementWrapper(Integer id, String sapid, Vendor vendor, String neFrequency,
      String neId, Integer cellId, NEType neType, Double lattitude, Double longitude,
      NEStatus neStatus) {
    this.sapid = sapid;
    this.id = id;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    if (neStatus != null) {
      this.neStatus = neStatus.name();
    }
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.cellId = cellId;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.neId = neId;
  }

  // getNEByNeNamesForCellWithLockStatus
  public NetworkElementWrapper(Integer id, String sapid, Vendor vendor, String neFrequency,
      String neId, Integer cellId, NEType neType, Double lattitude, Double longitude,
      NEStatus neStatus,  Boolean locked, String geographyL2,
      String geographyL3, String geographyL4) {
    this.sapid = sapid;
    this.id = id;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    if (neStatus != null) {
      this.neStatus = neStatus.name();
    }
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.cellId = cellId;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.neId = neId;
   
    if (locked != null) {
      this.locked = locked;
    } else {
      this.locked = false;
    }
    this.geographyL2 = geographyL2;
    this.geographyL3 = geographyL3;
    this.geographyL4 = geographyL4;
  }

  /*
   * getNeDetailsAndLockStatusForCells,getNEByNeNamesForODSCWithLockStatus
   */
  public NetworkElementWrapper(Integer id, String sapid, Vendor vendor, String neFrequency,
      String neId, Integer cellId, NEType neType, Double lattitude, Double longitude,
      NEStatus neStatus, Boolean locked, String geographyL2,
      String geographyL3, String geographyL4, String geographyL1) {
    this.sapid = sapid;
    this.id = id;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    if (neStatus != null) {
      this.neStatus = neStatus.name();
    }
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.cellId = cellId;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.neId = neId;
    if (locked != null) {
      this.locked = locked;
    } else {
      this.locked = false;
    }
    this.geographyL2 = geographyL2;
    this.geographyL3 = geographyL3;
    this.geographyL4 = geographyL4;
    this.geographyL1 = geographyL1;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param id the id
   * @param sapid the sapid
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param neId the ne id
   * @param neType the ne type
   * @param lattitude the lattitude
   * @param longitude the longitude
   * @param neStatus the ne status
   * @param vipSiteType the vip site type
   */
  // getNEByGeographyDomainVendorTechAndNeType
  public NetworkElementWrapper(Integer id, String sapid, Vendor vendor, String neFrequency,
      String neId, NEType neType, Double lattitude, Double longitude, NEStatus neStatus) {
    this.sapid = sapid;
    this.id = id;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    if (neStatus != null) {
      this.neStatus = neStatus.name();
    }
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.neId = neId;
  }

  // getNEByNeNamesForMacroWithLockStatus
  public NetworkElementWrapper(Integer id, String sapid, Vendor vendor, String neFrequency,
      String neId, NEType neType, Double lattitude, Double longitude, NEStatus neStatus, Boolean locked, String geographyL2, String geographyL3,
      String geographyL4, String geographyL1) {
    this.sapid = sapid;
    this.id = id;
    if (vendor != null) {
      this.vendor = vendor.name();
    }
    if (neStatus != null) {
      this.neStatus = neStatus.name();
    }
    this.neFrequency = neFrequency;
    this.band = neFrequency;
    this.neType = neType;
    this.latitude = lattitude;
    this.longitude = longitude;
    this.neId = neId;
    
    if (locked != null) {
      this.locked = locked;
    } else {
      this.locked = false;
    }
    this.geographyL2 = geographyL2;
    this.geographyL3 = geographyL3;
    this.geographyL4 = geographyL4;
    this.geographyL1 = geographyL1;
  }

  public NetworkElementWrapper(Domain domain, Vendor vendor, String geographyL3, String neId,
      String otherGeography) {
    this.domain = domain.name();
    this.vendor = vendor.name();
    this.geographyL3 = geographyL3;
    this.neId = neId;
    this.otherGeography = otherGeography;
  }

  public NetworkElementWrapper(Domain domain, Vendor vendor, Integer geographyL3Id, String neId,
      Integer otherGeographyId, String parentNEId) {
    this.domain = domain.name();
    this.vendor = vendor.name();
    this.geographyL3Id = geographyL3Id;
    this.neId = neId;
    this.otherGeographyId = otherGeographyId;
    this.parentNEId = parentNEId;
  }
  
  public NetworkElementWrapper(Domain domain, Vendor vendor, Integer geographyL3Id, String neId,
			Integer otherGeographyId, String parentNEId, NEType neType) {
	this.domain = domain.name();
	this.vendor = vendor.name();
	this.geographyL3Id = geographyL3Id;
	this.neId = neId;
	this.otherGeographyId = otherGeographyId;
	this.parentNEId = parentNEId;
	this.neType = neType;
  }

  public NetworkElementWrapper(Domain domain, Vendor vendor, String geographyL3) {
    this.domain = domain.name();
    this.vendor = vendor.name();
    this.geographyL3 = geographyL3;
  }

  // getOtherGeographyNameByNeNameDomainVendor, getGeographyL3NameNeIdByNEName
  public NetworkElementWrapper(String geographyName, String neId, Integer id) {
    this.geographyName = geographyName;
    this.neId = neId;
    this.id = id;
  }

  /**
   * Gets the display name.
   *
   * @return the display name
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Sets the display name.
   *
   * @param displayName the new display name
   */
  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Gets the ne type.
   *
   * @return the ne type
   */
  public NEType getNeType() {
    return neType;
  }

  /**
   * Sets the ne type.
   *
   * @param neType the new ne type
   */
  public void setNeType(NEType neType) {
    this.neType = neType;
  }

  /**
   * Gets the progress state.
   *
   * @return the progress state
   */
  public String getProgressState() {
    return progressState;
  }

  /**
   * Sets the progress state.
   *
   * @param progressState the new progress state
   */
  public void setProgressState(String progressState) {
    this.progressState = progressState;
  }

  /**
   * Gets the modified time.
   *
   * @return the modified time
   */
  public Date getModifiedTime() {
    return modifiedTime;
  }

  /**
   * Gets the centroid.
   *
   * @return the centroid
   */
  public Double[] getCentroid() {
    return centroid;
  }

  /**
   * Sets the centroid.
   *
   * @param centroid the new centroid
   */
  public void setCentroid(Double[] centroid) {
    this.centroid = centroid;
  }

  /**
   * Sets the modified time.
   *
   * @param modifiedTime the new modified time
   */
  public void setModifiedTime(Date modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  /**
   * Gets the geography L 1.
   *
   * @return the geography L 1
   */
  public String getGeographyL1() {
    return geographyL1;
  }

  /**
   * Sets the geography L 1.
   *
   * @param geographyL1 the new geography L 1
   */
  public void setGeographyL1(String geographyL1) {
    this.geographyL1 = geographyL1;
  }

  /**
   * Gets the geography L 4.
   *
   * @return the geography L 4
   */
  public String getGeographyL4() {
    return geographyL4;
  }

  /**
   * Sets the geography L 4.
   *
   * @param geographyL4 the new geography L 4
   */
  public void setGeographyL4(String geographyL4) {
    this.geographyL4 = geographyL4;
  }

  /**
   * Gets the geography L 2.
   *
   * @return the geography L 2
   */
  public String getGeographyL2() {
    return geographyL2;
  }

  /**
   * Sets the geography L 2.
   *
   * @param geographyL2 the new geography L 2
   */
  public void setGeographyL2(String geographyL2) {
    this.geographyL2 = geographyL2;
  }

  /**
   * Gets the geography L 3.
   *
   * @return the geography L 3
   */
  public String getGeographyL3() {
    return geographyL3;
  }

  /**
   * Sets the geography L 3.
   *
   * @param geographyL3 the new geography L 3
   */
  public void setGeographyL3(String geographyL3) {
    this.geographyL3 = geographyL3;
  }

  /**
   * Gets the ne id.
   *
   * @return the ne id
   */
  public String getNeId() {
    return neId;
  }

  /**
   * Sets the ne id.
   *
   * @param neId the new ne id
   */
  public void setNeId(String neId) {
    this.neId = neId;
  }

  /**
   * Gets the network element.
   *
   * @return the network element
   */
  public NetworkElement getNetworkElement() {
    return networkElement;
  }

  /**
   * Sets the network element.
   *
   * @param networkElement the new network element
   */
  public void setNetworkElement(NetworkElement networkElement) {
    this.networkElement = networkElement;
  }

  /**
   * Gets the min limit.
   *
   * @return the min limit
   */
  public Integer getMinLimit() {
    return minLimit;
  }

  /**
   * Sets the min limit.
   *
   * @param minLimit the new min limit
   */
  public void setMinLimit(Integer minLimit) {
    this.minLimit = minLimit;
  }

  /**
   * Gets the color.
   *
   * @return the color
   */
  public String getColor() {
    return color;
  }

  /**
   * Sets the color.
   *
   * @param color the new color
   */
  public void setColor(String color) {
    this.color = color;
  }

  /**
   * Gets the ecgi.
   *
   * @return the ecgi
   */
  public String getEcgi() {
    return ecgi;
  }

  /**
   * Sets the ecgi.
   *
   * @param ecgi the new ecgi
   */
  public void setEcgi(String ecgi) {
    this.ecgi = ecgi;
  }

  /**
   * Gets the task progress.
   *
   * @return the task progress
   */
  public Integer getTaskProgress() {
    return taskProgress;
  }

  /**
   * Sets the task progress.
   *
   * @param taskProgress the new task progress
   */
  public void setTaskProgress(Integer taskProgress) {
    this.taskProgress = taskProgress;
  }

  /**
   * Gets the current stage.
   *
   * @return the current stage
   */
  public String getCurrentStage() {
    return currentStage;
  }

  /**
   * Sets the current stage.
   *
   * @param currentStage the new current stage
   */
  public void setCurrentStage(String currentStage) {
    this.currentStage = currentStage;
  }

  /**
   * Gets the ne status.
   *
   * @return the ne status
   */
  public String getNeStatus() {
    return neStatus;
  }

  /**
   * Sets the ne status.
   *
   * @param neStatus the new ne status
   */
  public void setNeStatus(String neStatus) {
    this.neStatus = neStatus;
  }

  /**
   * Gets the ne name.
   *
   * @return the ne name
   */
  public String getNeName() {
    return neName;
  }

  /**
   * Sets the ne name.
   *
   * @param neName the new ne name
   */
  public void setNeName(String neName) {
    this.neName = neName;
  }

  /**
   * Gets the parent NE id.
   *
   * @return the parent NE id
   */
  public String getParentNEId() {
    return parentNEId;
  }

  /**
   * Sets the parent NE id.
   *
   * @param parentNEId the new parent NE id
   */
  public void setParentNEId(String parentNEId) {
    this.parentNEId = parentNEId;
  }



  /**
   * Gets the last updated date.
   *
   * @return the last updated date
   */
  public String getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  /**
   * Sets the last updated date.
   *
   * @param lastUpdatedDate the new last updated date
   */
  public void setLastUpdatedDate(String lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }

  /**
   * Gets the updated date.
   *
   * @return the updated date
   */
  public Date getUpdatedDate() {
    return updatedDate;
  }

  /**
   * Sets the updated date.
   *
   * @param updatedDate the new updated date
   */
  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  /**
   * Gets the site name.
   *
   * @return the site name
   */
  public String getSiteName() {
    return siteName;
  }

  /**
   * Sets the site name.
   *
   * @param siteName the new site name
   */
  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param neName the ne name
   * @param vendor the vendor
   * @param neFrequency the ne frequency
   * @param cellName the cell name
   * @param cellId the cell id
   * @param neType the ne type
   * @param latitude the latitude
   * @param longitude the longitude
   * @param neStatus the ne status
   * @param azimuth the azimuth
   * @param sector the sector
   * @param domain the domain
   * @param pci the pci
   */
  public NetworkElementWrapper(String neName, String vendor, String neFrequency, String cellName,
      Integer cellId, NEType neType, Double latitude, Double longitude, String neStatus,
      Integer azimuth, Integer sector, String domain, Integer pci) {
    this.neName = neName;
    this.vendor = vendor;
    this.neFrequency = neFrequency;
    this.cellName = cellName;
    this.cellId = cellId;
    if (neType != null)
      this.neType = neType;
    this.latitude = latitude;
    this.longitude = longitude;
    this.neStatus = neStatus;
    this.azimuth = azimuth;
    this.sectorId = sector;
    this.domain = domain;
    this.pci = pci;

  }

  /**
   * Instantiates a new network element wrapper.
   *
   * @param state the state
   * @param totalCount the total count
   * @param latitude the latitude
   * @param longitude the longitude
   * @param status the status
   * @param neType the ne type
   * @param lastUpdatedDate the last updated date
   */
  // For Sites Table View
  public NetworkElementWrapper(String state, Long totalCount, Double latitude, Double longitude,
      NEStatus status, NEType neType, Date lastUpdatedDate) {
    this.geographyName = state;
    this.totalCount = totalCount;
    this.latitude = latitude;
    this.longitude = longitude;
    this.centroid = new Double[] {latitude, longitude};
    this.status = status.name();
    this.neType = neType;
    Timestamp ts = new Timestamp(lastUpdatedDate.getTime());
    this.lastUpdatedDate = new SimpleDateFormat("dd-MM-yyyy").format(ts);
  }

  /**
   * Named query constructor getCellDetailByVendorAndStatus
   */
  public NetworkElementWrapper(Integer id, Integer cellId, String neFrequency, NEStatus neStatus,
      NEType neType, String neName, Technology technology, Vendor vendor, Domain domain,
      String neId, Double latitude, Double longitude, String geographyL4) {
    this.id = id;
    this.cellId = cellId;
    this.neFrequency = neFrequency;
    if (neStatus != null)
      this.neStatus = neStatus.name();
    this.neType = neType;
    this.neName = neName;
    if (technology != null)
      this.technology = technology.name();
    if (vendor != null)
      this.vendor = vendor.name();
    if (domain != null)
      this.domain = domain.name();
    this.neId = neId;
    this.latitude = latitude;
    this.longitude = longitude;
    this.geographyL4 = geographyL4;
  }
  
  
  //getConstructorStatementForLayerCount for count in layer
  public NetworkElementWrapper(String state, Long totalCount, Double latitude,
		  Double longitude, NEType neType,
		  NEStatus status, String neFrequency,Double centroidLat,Double centroidLong) {
	  this.geographyName = state;
	  this.totalCount = totalCount;
	  this.latitude=latitude;
	  this.longitude=longitude;
	  this.neType = neType;
	  this.status = status.name();
	  this.neFrequency = neFrequency;
	  this.centroid = new Double[] { latitude, longitude };
  }
//RAN Count
  public NetworkElementWrapper(Vendor vendor,NEStatus neStatus,String emsName, Long count) {
		super();
		this.vendor = String.valueOf(vendor);
		this.emsHostName = emsName;
		this.neStatus = String.valueOf(neStatus);
		this.count = count;
	}
//IPBB And IPRAN Count
public NetworkElementWrapper(NEType neType, Long count, Technology technology,Vendor vendor,String emsHostName,String ip) {
	super();
	this.vendor =vendor!=null ? vendor.toString():null;
	this.technology = technology!=null ? technology.toString():null;
	this.neType = neType;
	this.count = count;
	this.emsHostName=emsHostName;
	this.ipv4=ip;
}

  /**
   * Gets the earfcn.
   *
   * @return the earfcn
   */
  public Integer getEarfcn() {
    return earfcn;
  }

  /**
   * Sets the earfcn.
   *
   * @param earfcn the new earfcn
   */
  public void setEarfcn(Integer earfcn) {
    this.earfcn = earfcn;
  }


  /**
   * Gets the carrier.
   *
   * @return the carrier
   */
  public String getCarrier() {
    return carrier;
  }

  /**
   * Sets the carrier.
   *
   * @param carrier the new carrier
   */
  public void setCarrier(String carrier) {
    this.carrier = carrier;
  }

  public Integer getCgi() {
    return cgi;
  }

  public void setCgi(Integer cgi) {
    this.cgi = cgi;
  }

  public Double getCentroidLat() {
    return centroidLat;
  }

  public void setCentroidLat(Double centroidLat) {
    this.centroidLat = centroidLat;
  }

  public Double getCentroidLong() {
    return centroidLong;
  }

  public void setCentroidLong(Double centroidLong) {
    this.centroidLong = centroidLong;
  }

  public String getKpiUnit() {
    return kpiUnit;
  }

  public void setKpiUnit(String kpiUnit) {
    this.kpiUnit = kpiUnit;
  }

  public Object getKpiValue() {
    return kpiValue;
  }

  public void setKpiValue(Object kpiValue) {
    this.kpiValue = kpiValue;
  }

  public String getKpiName() {
    return kpiName;
  }

  public void setKpiName(String kpiName) {
    this.kpiName = kpiName;
  }

  public Long getViewPortCount() {
    return viewPortCount;
  }

  public void setViewPortCount(Long viewPortCount) {
    this.viewPortCount = viewPortCount;
  }

  public Boolean getLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public String getClutterCategory() {
    return clutterCategory;
  }

  public void setClutterCategory(String clutterCategory) {
    this.clutterCategory = clutterCategory;
  }


  public Integer getDlBandwidth() {
    return dlBandwidth;
  }

  public void setDlBandwidth(Integer dlBandwidth) {
    this.dlBandwidth = dlBandwidth;
  }

  public String getMorphology() {
    return morphology;
  }

  public void setMorphology(String morphology) {
    this.morphology = morphology;
  }

  public String getBackHaulMedia() {
    return backHaulMedia;
  }

  public void setBackHaulMedia(String backHaulMedia) {
    this.backHaulMedia = backHaulMedia;
  }

  public Date getOnAirDate() {
    return onAirDate;
  }

  public void setOnAirDate(Date onAirDate) {
    this.onAirDate = onAirDate;
  }

  public Integer getNumberOfSector() {
    return numberOfSector;
  }

  public void setNumberOfSector(Integer numberOfSector) {
    this.numberOfSector = numberOfSector;
  }

  public String getSiteAddress() {
    return siteAddress;
  }

  public void setSiteAddress(String siteAddress) {
    this.siteAddress = siteAddress;
  }

  public String getSiteCategory() {
    return siteCategory;
  }

  public void setSiteCategory(String siteCategory) {
    this.siteCategory = siteCategory;
  }

  public String getContactName() {
    return ContactName;
  }

  public void setContactName(String contactName) {
    ContactName = contactName;
  }

  public String getContactNumber() {
    return ContactNumber;
  }

  public void setContactNumber(String contactNumber) {
    ContactNumber = contactNumber;
  }

  public String getNeSource() {
    return neSource;
  }

  public void setNeSource(String neSource) {
    this.neSource = neSource;
  }

  public String getSourceDate() {
    return sourceDate;
  }

  public void setSourceDate(String sourceDate) {
    this.sourceDate = sourceDate;
  }

public String getEmsLive() {
	return emsLive;
}

public void setEmsLive(String emsLive) {
	this.emsLive = emsLive;
}

public String getBandwidth() {
	return bandwidth;
}

public void setBandwidth(String bandwidth) {
	this.bandwidth = bandwidth;
}

public String getBandwidthFourthSector() {
	return bandwidthFourthSector;
}

public void setBandwidthFourthSector(String bandwidthFourthSector) {
	this.bandwidthFourthSector = bandwidthFourthSector;
}

public Date getOnAirDateFourthSector() {
	return onAirDateFourthSector;
}

public void setOnAirDateFourthSector(Date onAirDateFourthSector) {
	this.onAirDateFourthSector = onAirDateFourthSector;
}

public String getEmsLiveFourthSector() {
	return emsLiveFourthSector;
}

public void setEmsLiveFourthSector(String emsLiveFourthSector) {
	this.emsLiveFourthSector = emsLiveFourthSector;
}

public String getNeStatusFourthSector() {
	return neStatusFourthSector;
}

public void setNeStatusFourthSector(String neStatusFourthSector) {
	this.neStatusFourthSector = neStatusFourthSector;
}

public Long getFddCellCount() {
	return fddCellCount;
}

public void setFddCellCount(Long fddCellCount) {
	this.fddCellCount = fddCellCount;
}

public Long getTddCellCount() {
	return tddCellCount;
}

public void setTddCellCount(Long tddCellCount) {
	this.tddCellCount = tddCellCount;
}

public String getDuplex() {
	return duplex;
}

public void setDuplex(String duplex) {
	this.duplex = duplex;
}

public String getIpv4() {
	return ipv4;
}

public void setIpv4(String ipv4) {
	this.ipv4 = ipv4;
}

public String getModel() {
	return model;
}

public void setModel(String model) {
	this.model = model;
}


public Long getVcuCount() {
    return vcuCount;
}

public void setVcuCount(Long vcuCount) {
    this.vcuCount = vcuCount;
}


public Long getRiuCount() {
    return riuCount;
}


public void setRiuCount(Long riuCount) {
    this.riuCount = riuCount;
}


public Long getRrhCount() {
    return rrhCount;
}


public void setRrhCount(Long rrhCount) {
    this.rrhCount = rrhCount;
}



public Map<String,Set<String>> getEffectedNode() {
	return effectedNode;
}


public void setEffectedNode(Map<String,Set<String>> effectedNode) {
	this.effectedNode = effectedNode;
}

public String getTagStatus() {
    return tagStatus;
}

public void setTagStatus(String tagStatus) {
    this.tagStatus = tagStatus;
}


public String getUserlabel() {
	return userlabel;
}

public void setUserlabel(String userlabel) {
	this.userlabel = userlabel;
}

public String getFopsCluster() {
	return fopsCluster;
}

public void setFopsCluster(String clusterFOPS) {
	this.fopsCluster = clusterFOPS;
}
public Long getCellCount() {
	return cellCount;
}


public void setCellCount(Long cellCount) {
	this.cellCount = cellCount;
}


@Override
public String toString() {
	return "NetworkElementWrapper [id=" + id + ", sapid=" + sapid + ", status=" + status + ", cellId=" + cellId
			+ ", earfcn=" + earfcn + ", enbType=" + enbType + ", siteName=" + siteName + ", band=" + band
			+ ", operationStatus=" + operationStatus + ", adminStatus=" + adminStatus + ", geographyL2=" + geographyL2
			+ ", geographyL3=" + geographyL3 + ", geographyL1=" + geographyL1 + ", geographyL4=" + geographyL4
			+ ", sectorId=" + sectorId + ", parsedFlag=" + parsedFlag + ", date=" + date + ", carrier=" + carrier
			+ ", time=" + time + ", cellName=" + cellName + ", masterSmallCellId=" + masterSmallCellId + ", vendor="
			+ vendor + ", antennaHeight=" + antennaHeight + ", mechTilt=" + mechTilt + ", elecTilt=" + elecTilt
			+ ", antennaType=" + antennaType + ", ecgi=" + ecgi + ", azimuth=" + azimuth + ", enbId=" + enbId
			+ ", totalCount=" + totalCount + ", viewPortCount=" + viewPortCount + ", clutterCategory=" + clutterCategory
			+ ", technology=" + technology + ", neFrequency=" + neFrequency + ", latitude=" + latitude + ", longitude="
			+ longitude + ", centroidLat=" + centroidLat + ", centroidLong=" + centroidLong + ", pci=" + pci
			+ ", centroid=" + Arrays.toString(centroid) + ", geographyName=" + geographyName + ", isHighlyUtilised="
			+ isHighlyUtilised + ", isHighlyUtilisedOne=" + isHighlyUtilisedOne + ", isHighlyUtilisedTwo="
			+ isHighlyUtilisedTwo + ", neType=" + neType + ", progressState=" + progressState + ", modifiedTime="
			+ modifiedTime + ", displayName=" + displayName + ", neId=" + neId + ", domain=" + domain + ", mcc=" + mcc
			+ ", mnc=" + mnc + ", minLimit=" + minLimit + ", geographyL2Id=" + geographyL2Id + ", geographyL3Id="
			+ geographyL3Id + ", geographyL4Id=" + geographyL4Id + ", otherGeographyId=" + otherGeographyId
			+ ", softwareVersion=" + softwareVersion + ", hostName=" + hostName + ", color=" + color
			+ ", networkElement=" + networkElement + ", taskProgress=" + taskProgress + ", currentStage=" + currentStage
			+ ", lastUpdatedDate=" + lastUpdatedDate + ", updatedDate=" + updatedDate + ", count=" + count
			+ ", neStatus=" + neStatus + ", neName=" + neName + ", parentNEId=" + parentNEId + ", vvip=" + vvip
			+ ", cgi=" + cgi + ", kpiName=" + kpiName + ", kpiValue=" + kpiValue + ", kpiUnit=" + kpiUnit + ", locked="
			+ locked + ", dlBandwidth=" + dlBandwidth + ", morphology=" + morphology + ", parentName=" + parentName
			+ ", otherGeography=" + otherGeography + ", backHaulMedia=" + backHaulMedia + ", onAirDate=" + onAirDate
			+ ", numberOfSector=" + numberOfSector + ", siteAddress=" + siteAddress + ", siteCategory=" + siteCategory
			+ ", ContactName=" + ContactName + ", ContactNumber=" + ContactNumber + ", neSource=" + neSource
			+ ", sourceDate=" + sourceDate + ", emsHostName=" + emsHostName + ", emsIp=" + emsIp + ", emsLive="
			+ emsLive + ", bandwidth=" + bandwidth + ", bandwidthFourthSector=" + bandwidthFourthSector
			+ ", onAirDateFourthSector=" + onAirDateFourthSector + ", emsLiveFourthSector=" + emsLiveFourthSector
			+ ", neStatusFourthSector=" + neStatusFourthSector + ", fddCellCount=" + fddCellCount + ", tddCellCount="
			+ tddCellCount + ", duplex=" + duplex + ", ipv4=" + ipv4 + ", model=" + model + ", vcuCount=" + vcuCount
			+ ", riuCount=" + riuCount + ", rrhCount=" + rrhCount + ", effectedNode=" + effectedNode + ", tagStatus="
			+ tagStatus + ", userlabel=" + userlabel + ", fopsCluster=" + fopsCluster + ", cellCount=" + cellCount
			+ "]";
}

}
