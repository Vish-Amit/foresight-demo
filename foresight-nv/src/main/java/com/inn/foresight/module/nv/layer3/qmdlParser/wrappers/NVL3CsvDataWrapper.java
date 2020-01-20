package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;

import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

/** The Class L3CsvDataHolderWrapper. */
public class NVL3CsvDataWrapper {

	/** The identifier. */
	private String identifier;

	/** The network type. */
	private String networkType;

	/** The ul through put. */
	private Double ulThroughPut;

	/** The dl through put. */
	private Double dlThroughPut;

	/** The time stamp. */
	private Long timeStamp;

	/** The lat. */
	private Double lat;

	/** The lon. */
	private Double lon;

	/** The latency. */
	private Double latency;

	/** The jitter. */
	private Double jitter;

	/** The host. */
	private String host;

	/** The sinr. */
	private Double sinr;

	/** The rsrq. */
	private Double rsrq;

	/** The rsrp. */
	private Double rsrp;

	/** The rssi. */
	private Double rssi;

	/** The rscp. */
	private Double rscp;
	
	private Double packetLoss;

	/** The ec no. */
	private Integer ecNo;

	/** The ec no. */
	private Integer ecIO;

	/** The rx level. */
	private Integer rxLevel;

	/** The rx quality. */
	private Integer rxQuality;

	/** The tac. */
	private Integer tac;

	/** The mnc. */
	private Integer mnc;

	/** The mcc. */
	private Integer mcc;

	/** The ci. */
	private Integer ci;

	/** The pci. */
	private Integer pci;

	/** The psc. */
	private Integer psc;

	/** The lac. */
	private Integer lac;

	/** The band. */
	private String band;

	private Double xpoint;

	private Double ypoint;

	private Double responseTime;

	private Double wifiRssi;
	private Double wifiSnr;
	private String ssid;
	private String bssid;
	private Integer channel;
	private Integer linkSpeed;
	
	private String coverage;
	private String testType;
	private Double pingPacketLoss;

	
	private String videoURL;
	private Long videoLoadTime;
	private String videoResolution;
	private Long videoDuration;
	private Double noOfStalling;
	private  Double  totalBufferTime;
	private String freezingRatio;
	private Integer iteration;
	private Integer cellId;
	private Integer eNodeBId;
	private String browseUrl;
	
	private Double httpDl;
	private Double httpUl;
	private Double ftpDl;
	private Double ftpUl;
	private String remark;
	private String testStatus;
	private String failureCause;
	private String navigationStartMessage;
	private String navigationStopMessage;
	
	
	private Double youtubeThroughPut;
	private Double youtubeBufferTime;
	private Double wptDns;
	private Double wptUrl;	
	private Integer cgi;	
	private Integer httpAttempt;
	private Integer httpSucess;
	private Integer httpFailure;	
	private Integer httpDrop;
	private Double httpDownLoadTime;
	private String isBackgroundPin;
	private  Integer smsAttempt;
	private  Integer smsSucess;
	private  Integer smsFailure;
	
	private Integer	speedTestPinNumber;
	private Double speedTestDlRate;
	private Double speedTestUlRate;	
	private Double  downloadTimeGoogle;
	private Double downloadTimeFacebook;
	private Double downloadTimeYoutube;
	private Integer pingBufferSize;	

	/* extra fields for stealth */
	private String firstbyteresponsetime;
	private String apptraffic;
	private String asnumber;
	private String autodatetimeenable;
	private String chargerconnected;
	private String chargertype;
	private String city;
	private String clientdestinationip;
	private String clientpingip;
	private String datasim;
	private String devicegroupid;
	private String devicetraffic;
	private String dnsresolvetime;
	private String firstdnsresolvetime;
	private String gpsaccuracy;
	private String gpsstatus;
	private String ipv4;
	private String ipv6;
	private String neighboursinfo;
	private String networksubtype;
	private String networkTypewhenwifi;
	private String noofredirections;
	private String operatornamewhenwifi;
	private String packetrecived;
	private String packettransmitted;
	private String pagesize;
	private String pingholderlist;
	private String redirectedurls;
	private String region;
	private String resultstatus;
	private String routeholderlist;
	private String securitytype;
	private String simslot;
	private String stallingcount;
	private String temperature;
	private String testendtime;
	private String teststarttime;
	private String testuniqueid;
	private String timeout;
	private String totalhops;
	private String totalpingcount;
	private String totaltraceroutetime;
	private String traceroutestatus;
	private String trafficipv4list;
	private String trafficipv6list;
	private String url1;
	private String url1browsetime;
	private String url1responsecode;
	private String url2;
	private String url2browsetime;
	private String url2responsecode;
	private String url3;
	private String url3browsetime;
	private String url3resonsecode;
	private String videoid;
	private String voicesim;
	private String voltage;
	private String workorderrecipemapingid;
	

	private String avglatency;
	private String batterylevel;
	private String maxlatency;
	private String minlatency;
	private String operatorname;

	private String islivestream;
	private String lagtime;
	private String maxdl;
	private String maxecIO;
	private String maxecNo;
	private String maxrscp;
	private String maxrsrp;
	private String maxrsrq;
	private String maxrssi;
	private String maxrxLevel;
	private String maxrxQuality;
	private String maxsinr;
	private String maxul;
	private String mindl;
	private String minecIO;
	private String minecNo;
	private String minrscp;
	private String minrsrp;
	private String minrsrq;
	private String minrssi;
	private String minrxLevel;
	private String minrxQuality;
	private String minsinr;
	private String minul;
	private String resolution;
	private String wifimaxrssi;
	private String wifimaxwifiSnr;
	private String wifiminrssi;
	private String wifiminwifiSnr;
	private String url;
	private String dnsForUrl;
	
	public String getDnsForUrl() {
		return dnsForUrl;
	}
	public void setDnsForUrl(String dnsForUrl) {
		this.dnsForUrl = dnsForUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIslivestream() {
		return islivestream;
	}
	public void setIslivestream(String islivestream) {
		this.islivestream = islivestream;
	}
	public String getLagtime() {
		return lagtime;
	}
	public void setLagtime(String lagtime) {
		this.lagtime = lagtime;
	}
	public String getMaxdl() {
		return maxdl;
	}
	public void setMaxdl(String maxdl) {
		this.maxdl = maxdl;
	}
	public String getMaxecIO() {
		return maxecIO;
	}
	public void setMaxecIO(String maxecIO) {
		this.maxecIO = maxecIO;
	}
	public String getMaxecNo() {
		return maxecNo;
	}
	public void setMaxecNo(String maxecNo) {
		this.maxecNo = maxecNo;
	}
	public String getMaxrscp() {
		return maxrscp;
	}
	public void setMaxrscp(String maxrscp) {
		this.maxrscp = maxrscp;
	}
	public String getMaxrsrp() {
		return maxrsrp;
	}
	public void setMaxrsrp(String maxrsrp) {
		this.maxrsrp = maxrsrp;
	}
	public String getMaxrsrq() {
		return maxrsrq;
	}
	public void setMaxrsrq(String maxrsrq) {
		this.maxrsrq = maxrsrq;
	}
	public String getMaxrssi() {
		return maxrssi;
	}
	public void setMaxrssi(String maxrssi) {
		this.maxrssi = maxrssi;
	}
	public String getMaxrxLevel() {
		return maxrxLevel;
	}
	public void setMaxrxLevel(String maxrxLevel) {
		this.maxrxLevel = maxrxLevel;
	}
	public String getMaxrxQuality() {
		return maxrxQuality;
	}
	public void setMaxrxQuality(String maxrxQuality) {
		this.maxrxQuality = maxrxQuality;
	}
	public String getMaxsinr() {
		return maxsinr;
	}
	public void setMaxsinr(String maxsinr) {
		this.maxsinr = maxsinr;
	}
	public String getMaxul() {
		return maxul;
	}
	public void setMaxul(String maxul) {
		this.maxul = maxul;
	}
	public String getMindl() {
		return mindl;
	}
	public void setMindl(String mindl) {
		this.mindl = mindl;
	}
	public String getMinecIO() {
		return minecIO;
	}
	public void setMinecIO(String minecIO) {
		this.minecIO = minecIO;
	}
	public String getMinecNo() {
		return minecNo;
	}
	public void setMinecNo(String minecNo) {
		this.minecNo = minecNo;
	}
	public String getMinrscp() {
		return minrscp;
	}
	public void setMinrscp(String minrscp) {
		this.minrscp = minrscp;
	}
	public String getMinrsrp() {
		return minrsrp;
	}
	public void setMinrsrp(String minrsrp) {
		this.minrsrp = minrsrp;
	}
	public String getMinrsrq() {
		return minrsrq;
	}
	public void setMinrsrq(String minrsrq) {
		this.minrsrq = minrsrq;
	}
	public String getMinrssi() {
		return minrssi;
	}
	public void setMinrssi(String minrssi) {
		this.minrssi = minrssi;
	}
	public String getMinrxLevel() {
		return minrxLevel;
	}
	public void setMinrxLevel(String minrxLevel) {
		this.minrxLevel = minrxLevel;
	}
	public String getMinrxQuality() {
		return minrxQuality;
	}
	public void setMinrxQuality(String minrxQuality) {
		this.minrxQuality = minrxQuality;
	}
	public String getMinsinr() {
		return minsinr;
	}
	public void setMinsinr(String minsinr) {
		this.minsinr = minsinr;
	}
	public String getMinul() {
		return minul;
	}
	public void setMinul(String minul) {
		this.minul = minul;
	}
	public String getResolution() {
		return resolution;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public String getWifimaxrssi() {
		return wifimaxrssi;
	}
	public void setWifimaxrssi(String wifimaxrssi) {
		this.wifimaxrssi = wifimaxrssi;
	}
	public String getWifimaxwifiSnr() {
		return wifimaxwifiSnr;
	}
	public void setWifimaxwifiSnr(String wifimaxwifiSnr) {
		this.wifimaxwifiSnr = wifimaxwifiSnr;
	}
	public String getWifiminrssi() {
		return wifiminrssi;
	}
	public void setWifiminrssi(String wifiminrssi) {
		this.wifiminrssi = wifiminrssi;
	}
	public String getWifiminwifiSnr() {
		return wifiminwifiSnr;
	}
	public void setWifiminwifiSnr(String wifiminwifiSnr) {
		this.wifiminwifiSnr = wifiminwifiSnr;
	}
	public String getFirstbyteresponsetime() {
		return firstbyteresponsetime;
	}
	public void setFirstbyteresponsetime(String firstbyteresponsetime) {
		this.firstbyteresponsetime = firstbyteresponsetime;
	}
	public String getApptraffic() {
		return apptraffic;
	}
	public void setApptraffic(String apptraffic) {
		this.apptraffic = apptraffic;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getClientdestinationip() {
		return clientdestinationip;
	}
	public void setClientdestinationip(String clientdestinationip) {
		this.clientdestinationip = clientdestinationip;
	}
	public String getFirstdnsresolvetime() {
		return firstdnsresolvetime;
	}
	public void setFirstdnsresolvetime(String firstdnsresolvetime) {
		this.firstdnsresolvetime = firstdnsresolvetime;
	}
	public String getMinlatency() {
		return minlatency;
	}
	public void setMinlatency(String minlatency) {
		this.minlatency = minlatency;
	}
	public String getNetworkTypewhenwifi() {
		return networkTypewhenwifi;
	}
	public void setNetworkTypewhenwifi(String networkTypewhenwifi) {
		this.networkTypewhenwifi = networkTypewhenwifi;
	}
	public String getNoofredirections() {
		return noofredirections;
	}
	public void setNoofredirections(String noofredirections) {
		this.noofredirections = noofredirections;
	}
	public String getOperatornamewhenwifi() {
		return operatornamewhenwifi;
	}
	public void setOperatornamewhenwifi(String operatornamewhenwifi) {
		this.operatornamewhenwifi = operatornamewhenwifi;
	}
	public String getPackettransmitted() {
		return packettransmitted;
	}
	public void setPackettransmitted(String packettransmitted) {
		this.packettransmitted = packettransmitted;
	}
	public String getPagesize() {
		return pagesize;
	}
	public void setPagesize(String pagesize) {
		this.pagesize = pagesize;
	}
	public String getPingholderlist() {
		return pingholderlist;
	}
	public void setPingholderlist(String pingholderlist) {
		this.pingholderlist = pingholderlist;
	}
	public String getRedirectedurls() {
		return redirectedurls;
	}
	public void setRedirectedurls(String redirectedurls) {
		this.redirectedurls = redirectedurls;
	}
	public String getSecuritytype() {
		return securitytype;
	}
	public void setSecuritytype(String securitytype) {
		this.securitytype = securitytype;
	}
	public String getTeststarttime() {
		return teststarttime;
	}
	public void setTeststarttime(String teststarttime) {
		this.teststarttime = teststarttime;
	}
	public String getTotalhops() {
		return totalhops;
	}
	public void setTotalhops(String totalhops) {
		this.totalhops = totalhops;
	}
	public String getTotalpingcount() {
		return totalpingcount;
	}
	public void setTotalpingcount(String totalpingcount) {
		this.totalpingcount = totalpingcount;
	}
	public String getTraceroutestatus() {
		return traceroutestatus;
	}
	public void setTraceroutestatus(String traceroutestatus) {
		this.traceroutestatus = traceroutestatus;
	}

	public String getUrl1() {
		return url1;
	}
	public void setUrl1(String url1) {
		this.url1 = url1;
	}
	public String getUrl1responsecode() {
		return url1responsecode;
	}
	public void setUrl1responsecode(String url1responsecode) {
		this.url1responsecode = url1responsecode;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this.url2 = url2;
	}
	public String getUrl2responsecode() {
		return url2responsecode;
	}
	public void setUrl2responsecode(String url2responsecode) {
		this.url2responsecode = url2responsecode;
	}
	public String getUrl3() {
		return url3;
	}
	public void setUrl3(String url3) {
		this.url3 = url3;
	}
	public String getUrl3browsetime() {
		return url3browsetime;
	}
	public void setUrl3browsetime(String url3browsetime) {
		this.url3browsetime = url3browsetime;
	}
	public String getUrl3resonsecode() {
		return url3resonsecode;
	}
	public void setUrl3resonsecode(String url3resonsecode) {
		this.url3resonsecode = url3resonsecode;
	}
	public String getVideoid() {
		return videoid;
	}
	public void setVideoid(String videoid) {
		this.videoid = videoid;
	}
	public String getVoicesim() {
		return voicesim;
	}
	public void setVoicesim(String voicesim) {
		this.voicesim = voicesim;
	}
	public String getTestuniqueid() {
		return testuniqueid;
	}
	public void setTestuniqueid(String testuniqueid) {
		this.testuniqueid = testuniqueid;
	}
	public String getDevicegroupid() {
		return devicegroupid;
	}
	public void setDevicegroupid(String devicegroupid) {
		this.devicegroupid = devicegroupid;
	}
	public String getOperatorname() {
		return operatorname;
	}
	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}
	public String getNetworksubtype() {
		return networksubtype;
	}
	public void setNetworksubtype(String networksubtype) {
		this.networksubtype = networksubtype;
	}
	public String getGpsaccuracy() {
		return gpsaccuracy;
	}
	public void setGpsaccuracy(String gpsaccuracy) {
		this.gpsaccuracy = gpsaccuracy;
	}
	public String getGpsstatus() {
		return gpsstatus;
	}
	public void setGpsstatus(String gpsstatus) {
		this.gpsstatus = gpsstatus;
	}
	public String getChargerconnected() {
		return chargerconnected;
	}
	public void setChargerconnected(String chargerconnected) {
		this.chargerconnected = chargerconnected;
	}
	public String getChargertype() {
		return chargertype;
	}
	public void setChargertype(String chargertype) {
		this.chargertype = chargertype;
	}
	public String getBatterylevel() {
		return batterylevel;
	}
	public void setBatterylevel(String batterylevel) {
		this.batterylevel = batterylevel;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getNeighboursinfo() {
		return neighboursinfo;
	}
	public void setNeighboursinfo(String neighboursinfo) {
		this.neighboursinfo = neighboursinfo;
	}
	public String getDevicetraffic() {
		return devicetraffic;
	}
	public void setDevicetraffic(String devicetraffic) {
		this.devicetraffic = devicetraffic;
	}
	public String getSimslot() {
		return simslot;
	}
	public void setSimslot(String simslot) {
		this.simslot = simslot;
	}
	public String getStallingcount() {
		return stallingcount;
	}
	public void setStallingcount(String stallingcount) {
		this.stallingcount = stallingcount;
	}

	public String getUrl2browsetime() {
		return url2browsetime;
	}
	public void setUrl2browsetime(String url2browsetime) {
		this.url2browsetime = url2browsetime;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getDnsresolvetime() {
		return dnsresolvetime;
	}
	public void setDnsresolvetime(String dnsresolvetime) {
		this.dnsresolvetime = dnsresolvetime;
	}
	public String getWorkorderrecipemapingid() {
		return workorderrecipemapingid;
	}
	public void setWorkorderrecipemapingid(String workorderrecipemapingid) {
		this.workorderrecipemapingid = workorderrecipemapingid;
	}
	public String getPacketrecived() {
		return packetrecived;
	}
	public void setPacketrecived(String packetrecived) {
		this.packetrecived = packetrecived;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getTrafficipv4list() {
		return trafficipv4list;
	}
	public void setTrafficipv4list(String trafficipv4list) {
		this.trafficipv4list = trafficipv4list;
	}
	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public String getIpv6() {
		return ipv6;
	}
	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}
	public String getUrl1browsetime() {
		return url1browsetime;
	}
	public void setUrl1browsetime(String url1browsetime) {
		this.url1browsetime = url1browsetime;
	}
	public String getTotaltraceroutetime() {
		return totaltraceroutetime;
	}
	public void setTotaltraceroutetime(String totaltraceroutetime) {
		this.totaltraceroutetime = totaltraceroutetime;
	}
	public String getAvglatency() {
		return avglatency;
	}
	public void setAvglatency(String avglatency) {
		this.avglatency = avglatency;
	}
	public String getClientpingip() {
		return clientpingip;
	}
	public void setClientpingip(String clientpingip) {
		this.clientpingip = clientpingip;
	}
	public String getMaxlatency() {
		return maxlatency;
	}
	public void setMaxlatency(String maxlatency) {
		this.maxlatency = maxlatency;
	}
	public String getAutodatetimeenable() {
		return autodatetimeenable;
	}
	public void setAutodatetimeenable(String autodatetimeenable) {
		this.autodatetimeenable = autodatetimeenable;
	}
	public String getDatasim() {
		return datasim;
	}
	public void setDatasim(String datasim) {
		this.datasim = datasim;
	}
	public String getAsnumber() {
		return asnumber;
	}
	public void setAsnumber(String asnumber) {
		this.asnumber = asnumber;
	}
	public String getTrafficipv6list() {
		return trafficipv6list;
	}
	public void setTrafficipv6list(String trafficipv6list) {
		this.trafficipv6list = trafficipv6list;
	}
	public String getRouteholderlist() {
		return routeholderlist;
	}
	public void setRouteholderlist(String routeholderlist) {
		this.routeholderlist = routeholderlist;
	}
	public String getVoltage() {
		return voltage;
	}
	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}
	public String getTestendtime() {
		return testendtime;
	}
	public void setTestendtime(String testendtime) {
		this.testendtime = testendtime;
	}
	public String getResultstatus() {
		return resultstatus;
	}
	public void setResultstatus(String resultstatus) {
		this.resultstatus = resultstatus;
	}

	private Double pMos;	

	public Double getpMos() {
		return pMos;
	}
	public void setpMos(Double pMos) {
		this.pMos = pMos;
	}

	public Integer getPingBufferSize() {
		return pingBufferSize;
	}
	public void setPingBufferSize(Integer pingBufferSize) {
		this.pingBufferSize = pingBufferSize;
	}
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}

	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getLinkSpeed() {
		return linkSpeed;
	}
	public void setLinkSpeed(Integer linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	/** The earfcn. */
	private Integer earfcn;
	
	/** The earfcn. */
	private Long ulEarfcn;

	private NvSMSDetailWrapper nvSMsDetailWrapper;
	

	public Long getUlEarfcn() {
		return ulEarfcn;
	}
	public void setUlEarfcn(Long ulEarfcn) {
		this.ulEarfcn = ulEarfcn;
	}
	public Integer getEarfcn() {
		return earfcn;
	}
	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}
	public NvSMSDetailWrapper getNvSMsDetailWrapper() {
		return nvSMsDetailWrapper;
	}
	public void setNvSMsDetailWrapper(NvSMSDetailWrapper nvSMsDetailWrapper) {
		this.nvSMsDetailWrapper = nvSMsDetailWrapper;
	}
	public Integer getCgi() {
		return cgi;
	}
	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}


	public Integer getHttpFailure() {
		return httpFailure;
	}
	public void setHttpFailure(Integer httpFailure) {
		this.httpFailure = httpFailure;
	}
	public Double getHttpDl() {
		return httpDl;
	}

	public void setHttpDl(Double httpDl) {
		this.httpDl = httpDl;
	}

	public Double getHttpUl() {
		return httpUl;
	}

	public void setHttpUl(Double httpUl) {
		this.httpUl = httpUl;
	}

	public Double getFtpDl() {
		return ftpDl;
	}

	public void setFtpDl(Double ftpDl) {
		this.ftpDl = ftpDl;
	}

	public Double getFtpUl() {
		return ftpUl;
	}

	public void setFtpUl(Double ftpUl) {
		this.ftpUl = ftpUl;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	public String getVideoResolution() {
		return videoResolution;
	}

	public void setVideoResolution(String videoResolution) {
		this.videoResolution = videoResolution;
	}

	public Long getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}

	public Double getNoOfStalling() {
		return noOfStalling;
	}

	public void setNoOfStalling(Double noOfStalling) {
		this.noOfStalling = noOfStalling;
	}

	public Double getTotalBufferTime() {
		return totalBufferTime;
	}

	public void setTotalBufferTime(Double totalBufferTime) {
		this.totalBufferTime = totalBufferTime;
	}

	public String getFreezingRatio() {
		return freezingRatio;
	}

	public void setFreezingRatio(String freezingRatio) {
		this.freezingRatio = freezingRatio;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getCoverage() {
		return coverage;
	}

	public void setCoverage(String coverage) {
		this.coverage = coverage;
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
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier.
	 *
	 * @param identifier the new identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets the network type.
	 *
	 * @return the network type
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * Sets the network type.
	 *
	 * @param networkType the new network type
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * Gets the ul through put.
	 *
	 * @return the ul through put
	 */
	public Double getUlThroughPut() {
		return ulThroughPut;
	}

	/**
	 * Sets the ul through put.
	 *
	 * @param ulThroughPut the new ul through put
	 */
	public void setUlThroughPut(Double ulThroughPut) {
		this.ulThroughPut = ulThroughPut;
	}

	/**
	 * Gets the dl through put.
	 *
	 * @return the dl through put
	 */
	public Double getDlThroughPut() {
		return dlThroughPut;
	}

	/**
	 * Sets the dl through put.
	 *
	 * @param dlThroughPut the new dl through put
	 */
	public void setDlThroughPut(Double dlThroughPut) {
		this.dlThroughPut = dlThroughPut;
	}

	/**
	 * Gets the time stamp.
	 *
	 * @return the time stamp
	 */
	public Long getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Sets the time stamp.
	 *
	 * @param timeStamp the new time stamp
	 */
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * Gets the latency.
	 *
	 * @return the latency
	 */
	public Double getLatency() {
		return latency;
	}

	/**
	 * Sets the latency.
	 *
	 * @param latency the new latency
	 */
	public void setLatency(Double latency) {
		this.latency = latency;
	}

	/**
	 * Gets the jitter.
	 *
	 * @return the jitter
	 */
	public Double getJitter() {
		return jitter;
	}

	/**
	 * Sets the jitter.
	 *
	 * @param jitter the new jitter
	 */
	public void setJitter(Double jitter) {
		this.jitter = jitter;
	}

	/**
	 * Gets the host.
	 *
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the host.
	 *
	 * @param host the new host
	 */
	public void setHost(String host) {
		this.host = host;
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


	public Double getRsrq() {
		return rsrq;
	}

	public void setRsrq(Double rsrq) {
		this.rsrq = rsrq;
	}

	public Double getRsrp() {
		return rsrp;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public Double getRssi() {
		return rssi;
	}

	public void setRssi(Double rssi) {
		this.rssi = rssi;
	}

	public Double getRscp() {
		return rscp;
	}

	public void setRscp(Double rscp) {
		this.rscp = rscp;
	}

	/**
	 * Gets the ec no.
	 *
	 * @return the ec no
	 */
	public Integer getEcNo() {
		return ecNo;
	}

	/**
	 * Sets the ec no.
	 *
	 * @param ecNo the new ec no
	 */
	public void setEcNo(Integer ecNo) {
		this.ecNo = ecNo;
	}

	/**
	 * Gets the rx level.
	 *
	 * @return the rx level
	 */
	public Integer getRxLevel() {
		return rxLevel;
	}

	/**
	 * Sets the rx level.
	 *
	 * @param rxLevel the new rx level
	 */
	public void setRxLevel(Integer rxLevel) {
		this.rxLevel = rxLevel;
	}

	/**
	 * Gets the rx quality.
	 *
	 * @return the rx quality
	 */
	public Integer getRxQuality() {
		return rxQuality;
	}

	/**
	 * Sets the rx quality.
	 *
	 * @param rxQuality the new rx quality
	 */
	public void setRxQuality(Integer rxQuality) {
		this.rxQuality = rxQuality;
	}

	/**
	 * Gets the tac.
	 *
	 * @return the tac
	 */
	public Integer getTac() {
		return tac;
	}

	/**
	 * Sets the tac.
	 *
	 * @param tac the new tac
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
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
	 * Gets the ci.
	 *
	 * @return the ci
	 */
	public Integer getCi() {
		return ci;
	}

	/**
	 * Sets the ci.
	 *
	 * @param ci the new ci
	 */
	public void setCi(Integer ci) {
		this.ci = ci;
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
	 * Gets the psc.
	 *
	 * @return the psc
	 */
	public Integer getPsc() {
		return psc;
	}

	/**
	 * Sets the psc.
	 *
	 * @param psc the new psc
	 */
	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	/**
	 * Gets the lac.
	 *
	 * @return the lac
	 */
	public Integer getLac() {
		return lac;
	}

	/**
	 * Sets the lac.
	 *
	 * @param lac the new lac
	 */
	public void setLac(Integer lac) {
		this.lac = lac;
	}

	public Double getXpoint() {
		return xpoint;
	}

	public void setXpoint(Double xpoint) {
		this.xpoint = xpoint;
	}

	public Double getYpoint() {
		return ypoint;
	}

	public void setYpoint(Double ypoint) {
		this.ypoint = ypoint;
	}

	public Double getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Double responseTime) {
		this.responseTime = responseTime;
	}

	public Double getWifiRssi() {
		return wifiRssi;
	}

	public void setWifiRssi(Double wifiRssi) {
		this.wifiRssi = wifiRssi;
	}

	public Double getWifiSnr() {
		return wifiSnr;
	}

	public void setWifiSnr(Double wifiSnr) {
		this.wifiSnr = wifiSnr;
	}

	public String getSsid() {
		return ssid;
	}

	public void setSsid(String ssid) {
		this.ssid = ssid;
	}

	public Double getPacketLoss() {
		return packetLoss;
	}

	public void setPacketLoss(Double packetLoss) {
		this.packetLoss = packetLoss;
	}

	public Integer getIteration() {
		return iteration;
	}

	public void setIteration(Integer iteration) {
		this.iteration = iteration;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getFailureCause() {
		return failureCause;
	}

	public void setFailureCause(String failureCause) {
		this.failureCause = failureCause;
	}

	public Double getPingPacketLoss() {
		return pingPacketLoss;
	}

	public void setPingPacketLoss(Double pingPacketLoss) {
		this.pingPacketLoss = pingPacketLoss;
	}

	public String processDataCSVForLiveDrive() {
		StringBuilder builder = new StringBuilder();
		NVLayer3Utils.addValueToStringBuilder(builder, NVLayer3Utils.getDateFromTimestamp(timeStamp));
		NVLayer3Utils.addValueToStringBuilder(builder, timeStamp);
		NVLayer3Utils.addValueToStringBuilder(builder, lat);
		NVLayer3Utils.addValueToStringBuilder(builder, lon);
		NVLayer3Utils.addValueToStringBuilder(builder, networkType);
		NVLayer3Utils.addValueToStringBuilder(builder, testType);
		processSignalAndNetworkParams(builder);
		NVLayer3Utils.addValueToStringBuilder(builder, dlThroughPut);
		NVLayer3Utils.addValueToStringBuilder(builder, ulThroughPut);
		processPingBrowseWPTParams(builder);
		processYoutubeParams(builder);
		return builder.toString();
	}

	public void processYoutubeParams(StringBuilder builder) {
		NVLayer3Utils.addValueToStringBuilder(builder, videoURL);
		NVLayer3Utils.addValueToStringBuilder(builder, videoResolution);
		NVLayer3Utils.addValueToStringBuilder(builder, videoDuration);
		NVLayer3Utils.addValueToStringBuilder(builder, noOfStalling);
		NVLayer3Utils.addValueToStringBuilder(builder, totalBufferTime);
		NVLayer3Utils.addValueToStringBuilder(builder, youtubeThroughPut);
		NVLayer3Utils.addValueToStringBuilder(builder, videoLoadTime);
	}

	public void processPingBrowseWPTParams(StringBuilder builder) {
		NVLayer3Utils.addValueToStringBuilder(builder, latency);
		NVLayer3Utils.addValueToStringBuilder(builder, jitter);
		NVLayer3Utils.addValueToStringBuilder(builder, pingPacketLoss);
		NVLayer3Utils.addValueToStringBuilder(builder, browseUrl);
		NVLayer3Utils.addValueToStringBuilder(builder, responseTime);
		NVLayer3Utils.addValueToStringBuilder(builder, wptUrl);
		NVLayer3Utils.addValueToStringBuilder(builder, wptDns);
	}

	public void processSignalAndNetworkParams(StringBuilder builder) {
		NVLayer3Utils.addValueToStringBuilder(builder, mcc);
		NVLayer3Utils.addValueToStringBuilder(builder, mnc);
		NVLayer3Utils.addValueToStringBuilder(builder, ci);
		NVLayer3Utils.addValueToStringBuilder(builder, cellId);
		NVLayer3Utils.addValueToStringBuilder(builder, eNodeBId);
		NVLayer3Utils.addValueToStringBuilder(builder, band);
		NVLayer3Utils.addValueToStringBuilder(builder, rsrp);
		NVLayer3Utils.addValueToStringBuilder(builder, sinr);
		NVLayer3Utils.addValueToStringBuilder(builder, rsrq);
		NVLayer3Utils.addValueToStringBuilder(builder, rssi);
	}
	
	

	public Double getYoutubeThroughPut() {
		return youtubeThroughPut;
	}

	public void setYoutubeThroughPut(Double youtubeThroughPut) {
		this.youtubeThroughPut = youtubeThroughPut;
	}

	public Double getYoutubeBufferTime() {
		return youtubeBufferTime;
	}

	public void setYoutubeBufferTime(Double youtubeBufferTime) {
		this.youtubeBufferTime = youtubeBufferTime;
	}

	public Double getWptDns() {
		return wptDns;
	}

	public void setWptDns(Double wptDns) {
		this.wptDns = wptDns;
	}

	public Double getWptUrl() {
		return wptUrl;
	}

	public void setWptUrl(Double wptUrl) {
		this.wptUrl = wptUrl;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Integer geteNodeBId() {
		return eNodeBId;
	}

	public void seteNodeBId(Integer eNodeBId) {
		this.eNodeBId = eNodeBId;
	}

	public String getBrowseUrl() {
		return browseUrl;
	}

	public void setBrowseUrl(String browseUrl) {
		this.browseUrl = browseUrl;
	}
	public Long getVideoLoadTime() {
		return videoLoadTime;
	}
	public void setVideoLoadTime(Long videoLoadTime) {
		this.videoLoadTime = videoLoadTime;
	}
	public Integer getHttpAttempt() {
		return httpAttempt;
	}
	public void setHttpAttempt(Integer httpAttempt) {
		this.httpAttempt = httpAttempt;
	}
	public Integer getHttpSucess() {
		return httpSucess;
	}
	public void setHttpSucess(Integer httpSucess) {
		this.httpSucess = httpSucess;
	}
	public Double getHttpDownLoadTime() {
		return httpDownLoadTime;
	}
	public void setHttpDownLoadTime(Double httpDownLoadTime) {
		this.httpDownLoadTime = httpDownLoadTime;
	}
	public String getIsBackgroundPin() {
		return isBackgroundPin;
	}
	public void setIsBackgroundPin(String isBackgroundPin) {
		this.isBackgroundPin = isBackgroundPin;
	}
	public Integer getSmsAttempt() {
		return smsAttempt;
	}
	public void setSmsAttempt(Integer smsAttempt) {
		this.smsAttempt = smsAttempt;
	}
	public Integer getSmsSucess() {
		return smsSucess;
	}
	public void setSmsSucess(Integer smsSucess) {
		this.smsSucess = smsSucess;
	}
	public Integer getSmsFailure() {
		return smsFailure;
	}
	public void setSmsFailure(Integer smsFailure) {
		this.smsFailure = smsFailure;
	}
	public Integer getEcIO() {
		return ecIO;
	}
	public void setEcIO(Integer ecIO) {
		this.ecIO = ecIO;
	}
	public String getNavigationStartMessage() {
		return navigationStartMessage;
	}
	public void setNavigationStartMessage(String navigationStartMessage) {
		this.navigationStartMessage = navigationStartMessage;
	}
	public String getNavigationStopMessage() {
		return navigationStopMessage;
	}
	public void setNavigationStopMessage(String navigationStopMessage) {
		this.navigationStopMessage = navigationStopMessage;
	}
	public Integer getHttpDrop() {
		return httpDrop;
	}
	public void setHttpDrop(Integer httpDrop) {
		this.httpDrop = httpDrop;
	}
	public Double getSpeedTestDlRate() {
		return speedTestDlRate;
	}
	public void setSpeedTestDlRate(Double speedTestDlRate) {
		this.speedTestDlRate = speedTestDlRate;
	}
	public Double getSpeedTestUlRate() {
		return speedTestUlRate;
	}
	public void setSpeedTestUlRate(Double speedTestUlRate) {
		this.speedTestUlRate = speedTestUlRate;
	}
	
	public Double getDownloadTimeGoogle() {
		return downloadTimeGoogle;
	}
	public void setDownloadTimeGoogle(Double downloadTimeGoogle) {
		this.downloadTimeGoogle = downloadTimeGoogle;
	}
	public Double getDownloadTimeFacebook() {
		return downloadTimeFacebook;
	}
	public void setDownloadTimeFacebook(Double downloadTimeFacebook) {
		this.downloadTimeFacebook = downloadTimeFacebook;
	}
	public Double getDownloadTimeYoutube() {
		return downloadTimeYoutube;
	}
	public void setDownloadTimeYoutube(Double downloadTimeYoutube) {
		this.downloadTimeYoutube = downloadTimeYoutube;
	}
	
	public Integer getSpeedTestPinNumber() {
		return speedTestPinNumber;
	}
	public void setSpeedTestPinNumber(Integer speedTestPinNumber) {
		this.speedTestPinNumber = speedTestPinNumber;
	}
	
	
	


}
