package com.inn.foresight.core.infra.wrapper;

public class NetworkHierarchyDetail {

	private Integer id;
	private String siteName;
	private String vduName;
	private String vcuName;
	private long count;
	private String geographyl4Name;
	private String geographyl3Name;
	private String geographyl2Name;
	private String geographyl1Name;

	public NetworkHierarchyDetail() {
		super();
	}

	//getSiteDetail
	public NetworkHierarchyDetail(Integer id, String siteName, String vduName, String vcuName, String geographyl4Name,
			String geographyl3Name, String geographyl2Name, String geographyl1Name) {
		super();
		this.id = id;
		this.siteName = siteName;
		this.vduName = vduName;
		this.vcuName = vcuName;
		this.geographyl4Name = geographyl4Name;
		this.geographyl3Name = geographyl3Name;
		this.geographyl2Name = geographyl2Name;
		this.geographyl1Name = geographyl1Name;
	}
	
	//getSiteListingInfo
	public NetworkHierarchyDetail(long count, String siteName, Integer id) {
		super();
		this.id = id;
		this.siteName = siteName;
		this.count = count;
	}
	//getMacroSiteDetail
	public NetworkHierarchyDetail(String siteName, Integer id) {
		super();
		this.id = id;
		this.siteName = siteName;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getVduName() {
		return vduName;
	}

	public void setVduName(String vduName) {
		this.vduName = vduName;
	}

	public String getVcuName() {
		return vcuName;
	}

	public void setVcuName(String vcuName) {
		this.vcuName = vcuName;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}
	
	public String getGeographyl4Name() {
		return geographyl4Name;
	}

	public void setGeographyl4Name(String geographyl4Name) {
		this.geographyl4Name = geographyl4Name;
	}

	public String getGeographyl3Name() {
		return geographyl3Name;
	}

	public void setGeographyl3Name(String geographyl3Name) {
		this.geographyl3Name = geographyl3Name;
	}

	public String getGeographyl2Name() {
		return geographyl2Name;
	}

	public void setGeographyl2Name(String geographyl2Name) {
		this.geographyl2Name = geographyl2Name;
	}

	public String getGeographyl1Name() {
		return geographyl1Name;
	}

	public void setGeographyl1Name(String geographyl1Name) {
		this.geographyl1Name = geographyl1Name;
	}

	@Override
	public String toString() {
		return "NetworkHierarchyDetail [id=" + id + ", siteName=" + siteName + ", vduName=" + vduName + ", vcuName="
				+ vcuName + ", count=" + count + ", geographyl4Name=" + geographyl4Name + ", geographyl3Name="
				+ geographyl3Name + ", geographyl2Name=" + geographyl2Name + ", geographyl1Name=" + geographyl1Name
				+ "]";
	}

}
