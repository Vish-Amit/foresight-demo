package com.inn.foresight.module.nv.inbuilding.wrapper;

import java.io.Serializable;
import java.text.ParseException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.foresight.core.infra.constants.InBuildingConstants.BuildingType;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

/**
 * The Class NVIBResultWrapper.
 *
 * @author innoeye
 * date - 17-Mar-2018 4:06:02 PM
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JpaWrapper
@RestWrapper
public class NVIBResultWrapper implements Serializable{
	private static Logger logger = LogManager.getLogger(NVIBResultWrapper.class);

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Integer id;
	/** The count. */
	private Long count;
	
	/** The geography name. */
	private String name;

    private String displayName;

    /** The centroid lat. */
	private Double latitude;
	
	/** The centroid lng. */
	private Double longitude;
	
	/** The rsrp. */
	private Double rsrp;
	
	/** The rssi. */
	private Double rssi;
	
	/** The sinr. */
	private Double sinr;
	
	/** The dl. */
	private Double dl;
	
	/** The ul. */
	private Double ul;
	
	/** The sinr. */
	private Double snr;
	
	/** The operator. */
	private String operator;
	
	/** The work order id. */
	private Integer workOrderId;
	private Long wingCount;
	private Long floorCount;
	private Long unitCount;
    private BuildingType buildingType;
    private  String address;
    private Integer band;
    private String date;
	
//    Unable to locate appropriate constructor on class [com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper]. Expected arguments are: int, java.lang.String, com.inn.foresight.core.infra.constants.InBuildingConstants$BuildingType, java.lang.String, double, double, long, long, long, double, long

    
    
    /** / used in getBuildingInFoByWoId. */
    
    public NVIBResultWrapper(Integer id, String name, BuildingType buildingType,  String address,Double lat,Double lon, Long wingCount, Long floorCount,
			Long unitCount,Double totalRsrp,Long countRsrp) {
		super();
		this.id = id;
		this.name = name;
		this.buildingType = buildingType;
		this.address = address;
		this.latitude=lat;
		this.longitude=lon;
		this.wingCount = wingCount;
		this.floorCount = floorCount;
		this.unitCount = unitCount;
		if(totalRsrp!=null&&countRsrp!=null)
			try {
				this.rsrp=NVLayer3Utils.roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND,(double) (totalRsrp/countRsrp));
			} catch (ParseException e) {
				logger.error("Error in round off value {}"+e.getMessage());
			}
	}

	private Integer hoFailure;
	
	private Integer hoSuccess;
	
	private Integer hoInitiate;
	/**
	 * Instantiates a new NVIB result wrapper.
	 *
	 * @param count the count
	 * @param name the name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public NVIBResultWrapper(Long count, String name, Double latitude,
			Double longitude) {
		super();
		this.count = count;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public NVIBResultWrapper(Long count, String name, String displayName, Double latitude,
	      Double longitude) {
	    super();
	    this.count = count;
	    this.name = name;
	    this.displayName = displayName;
	    this.latitude = latitude;
	    this.longitude = longitude;
	}
	

	/**
	 * Instantiates a new NVIB result wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 * @param rsrp the rsrp
	 * @param sinr the sinr
	 * @param ul the ul
	 * @param dl the dl
	 * @param rssi the rssi
	 * @param snr the snr
	 */
	public NVIBResultWrapper(Integer id, String name, Double rsrp, Double sinr, Double ul,
			Double dl,Double rssi,Double snr) {
		super();
		this.id = id;
		this.name = name;
		this.rsrp = rsrp;
		this.rssi = rssi;
		this.sinr = sinr;
		this.dl = dl;
		this.ul = ul;
		this.snr = snr;
	}
	//building id construtor 
	public NVIBResultWrapper(Double rsrp, Double sinr, Double ul,
			Double dl,Double rssi,Double snr,Integer band) {
		super();
		
		this.rsrp = rsrp;
		this.rssi = rssi;
		this.sinr = sinr;
		this.dl = dl;
		this.ul = ul;
		this.snr = snr;
		this.band=band;
	}
	//building id construtor 
		public NVIBResultWrapper(Double rsrp, Double sinr, Double ul,
				Double dl,Double rssi,Double snr) {
			super();
			
			this.rsrp = rsrp;
			this.rssi = rssi;
			this.sinr = sinr;
			this.dl = dl;
			this.ul = ul;
			this.snr = snr;
		}
		public NVIBResultWrapper(Double rsrp, Double sinr, Double ul,
				Double dl,Double rssi,Double snr,Integer band,String date) {
			super();
			
			this.rsrp = rsrp;
			this.rssi = rssi;
			this.sinr = sinr;
			this.dl = dl;
			this.ul = ul;
			this.snr = snr;
			this.band=band;
			this.date=date;
		}
	/**
	 * Instantiates a new NVIB result wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param rsrp the rsrp
	 * @param rssi the rssi
	 */
	public NVIBResultWrapper(Integer id, String name, Double latitude,
			Double longitude, Double rsrp, Double rssi,String address,BuildingType type) {
		super();
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.rsrp = rsrp;
		this.rssi = rssi;
		this.address=address;
		this.buildingType=type;
	}



	/**
	 * Instantiates a new NVIB result wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 * @param operator the operator
	 * @param workOrderId the work order id
	 */
	public NVIBResultWrapper(Integer id, String name, String operator,
			Integer workOrderId) {
		super();
		this.id = id;
		this.name = name;
		this.operator = operator;
		this.workOrderId = workOrderId;
	}


	/**
	 * Instantiates a new NVIB result wrapper.
	 *
	 * @param id the id
	 * @param name the name
	 */
	public NVIBResultWrapper(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}



	public NVIBResultWrapper() {
		super();
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
	
	

	public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the rsrp.
	 *
	 * @return the rsrp
	 */
	public Double getRsrp() {
		return rsrp;
	}

	/**
	 * Sets the rsrp.
	 *
	 * @param rsrp the new rsrp
	 */
	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * Gets the rssi.
	 *
	 * @return the rssi
	 */
	public Double getRssi() {
		return rssi;
	}

	/**
	 * Sets the rssi.
	 *
	 * @param rssi the new rssi
	 */
	public void setRssi(Double rssi) {
		this.rssi = rssi;
	}

	/**
	 * Gets the sinr.
	 *
	 * @return the sinr
	 */
	public Double getSinr() {
		return sinr;
	}

	/**
	 * Sets the sinr.
	 *
	 * @param sinr the new sinr
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	/**
	 * Gets the dl.
	 *
	 * @return the dl
	 */
	public Double getDl() {
		return dl;
	}

	/**
	 * Sets the dl.
	 *
	 * @param dl the new dl
	 */
	public void setDl(Double dl) {
		this.dl = dl;
	}

	/**
	 * Gets the ul.
	 *
	 * @return the ul
	 */
	public Double getUl() {
		return ul;
	}

	/**
	 * Sets the ul.
	 *
	 * @param ul the new ul
	 */
	public void setUl(Double ul) {
		this.ul = ul;
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
	 * Gets the snr.
	 *
	 * @return the snr
	 */
	public Double getSnr() {
		return snr;
	}



	/**
	 * Sets the snr.
	 *
	 * @param snr the new snr
	 */
	public void setSnr(Double snr) {
		this.snr = snr;
	}



	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}



	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}



	/**
	 * Gets the work order id.
	 *
	 * @return the work order id
	 */
	public Integer getWorkOrderId() {
		return workOrderId;
	}



	/**
	 * Sets the work order id.
	 *
	 * @param workOrderId the new work order id
	 */
	public void setWorkOrderId(Integer workOrderId) {
		this.workOrderId = workOrderId;
	}



	public Long getWingCount() {
		return wingCount;
	}



	public void setWingCount(Long wingCount) {
		this.wingCount = wingCount;
	}



	public Long getFloorCount() {
		return floorCount;
	}



	public void setFloorCount(Long floorCount) {
		this.floorCount = floorCount;
	}



	public Long getUnitCount() {
		return unitCount;
	}



	public void setUnitCount(Long unitCount) {
		this.unitCount = unitCount;
	}



	public BuildingType getBuildingType() {
		return buildingType;
	}



	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	@Override
	public String toString() {
		return "NVIBResultWrapper [id=" + id + ", count=" + count + ", name=" + name + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", rsrp=" + rsrp + ", rssi=" + rssi + ", sinr=" + sinr + ", dl=" + dl
				+ ", ul=" + ul + ", snr=" + snr + ", operator=" + operator + ", workOrderId=" + workOrderId
				+ ", wingCount=" + wingCount + ", floorCount=" + floorCount + ", unitCount=" + unitCount
				+ ", buildingType=" + buildingType + ", address=" + address + "]";
	}
	




	public Integer getHoFailure() {
		return hoFailure;
	}



	public void setHoFailure(Integer hoFailure) {
		this.hoFailure = hoFailure;
	}



	public Integer getHoSuccess() {
		return hoSuccess;
	}



	public void setHoSuccess(Integer hoSuccess) {
		this.hoSuccess = hoSuccess;
	}



	public Integer getHoInitiate() {
		return hoInitiate;
	}



	public void setHoInitiate(Integer hoInitiate) {
		this.hoInitiate = hoInitiate;
	}

	public Integer getBand() {
		return band;
	}

	public void setBand(Integer band) {
		this.band = band;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	

}
