package com.inn.foresight.module.nv.report.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.livedrive.constants.KPI;
import com.inn.foresight.module.nv.livedrive.wrapper.DeviceDataWapper;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackPosition;
import com.inn.foresight.module.nv.report.LiveDriveWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.ChartBeanWrapper;
import com.inn.foresight.module.nv.report.wrapper.RangeScoreWrapper;

public class LiveDriveReportUtil implements LiveDriveReportConstants {

	private static Integer totalCount = ReportConstants.INDEX_ZER0;
	private static double pdf = ReportConstants.INDEX_ZER0;
	private static double cdf = ReportConstants.INDEX_ZER0;
	private static DecimalFormat decimalFormat = new DecimalFormat("####.##");

	/** The logger. */
	private static Logger logger = LogManager.getLogger(LiveDriveReportUtil.class);

	public static String getDhiWithRanges(Double nhiData) {
		if (nhiData != null && !Double.isNaN(nhiData)) {
            return getHealthIndexScore(DATA_HEATH_INDEX, nhiData) + "(" + nhiData + ")";
        }
		return null;
	}

	public static String getChiWithRanges(Double nhiVoice) {
		if (nhiVoice != null && !Double.isNaN(nhiVoice)) {
            return getHealthIndexScore(COVERAGE_HEATH_INDEX, nhiVoice) + "(" + nhiVoice + ")";
        }
		return null;
	}

	public static String getHealthIndexScore(String nhi, Double nhiValue) {
		if (nhiValue != null) {

			String score = "-";
			if (COVERAGE_HEATH_INDEX.equalsIgnoreCase(nhi) || DATA_HEATH_INDEX.equalsIgnoreCase(nhi)) {
				score = getNhiDhiScore(nhiValue);
			}
			return score;

		}
		return null;
	}

	private static String getNhiDhiScore(Double nhiValue) {
		String score;
		if (nhiValue >= ReportConstants.INDEX_EIGHTY_FIVE) {
			score = SCORE_EXCELLENT;
		} else if (nhiValue < ReportConstants.INDEX_EIGHTY_FIVE && nhiValue >= ReportConstants.INDEX_SIXTY_FIVE) {
			score = SCORE_GOOD;
		} else if (nhiValue < ReportConstants.INDEX_SIXTY_FIVE && nhiValue >= ReportConstants.INDEX_FOURTY_FIVE) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	public static Double getMed(Double min, Double max) {
		double mid = (min + max) / ReportConstants.INDEX_TWO;
		return Double.parseDouble(decimalFormat.format(mid));
	}

	public  Map<String, LiveDriveWrapper> setDataOnMap(List<KPIWrapper> kpiList,Map<String, List<Double>> liveDriveKpiMap) {
		Map<String, LiveDriveWrapper> liveDriveMap = new HashMap<>();
		try {
			for(int index=ReportConstants.INDEX_ZER0;index<kpiList.size();index++){
				LiveDriveWrapper driveWrapper = new LiveDriveWrapper();
				KPIWrapper wrapper = kpiList.get(index);
				Statistics statistics = new Statistics(liveDriveKpiMap.get(wrapper.getKpiName()));
				driveWrapper.setKPI(wrapper.getKpiName() + " ("+ReportUtil.getUnitByKPiName(wrapper.getKpiName())+")");
				
				setStatsData(driveWrapper, statistics);
				
				if (driveWrapper.getMedKPI() != null) {
					driveWrapper.setScore(getScore(driveWrapper.getMedKPI(), wrapper.getKpiName()));
				}
				List<RangeScoreWrapper> listRangeScore = getRangeScoreList(wrapper.getKpiName());
				driveWrapper.setRangeScoreWrapperList(listRangeScore);
				driveWrapper.setChartTitle(ReportUtil.getChartTitle(wrapper.getKpiName()));
				driveWrapper.setChartType(ReportUtil.getChartType(wrapper.getKpiName()));
				driveWrapper.setGraphHeading(ReportUtil.getGraphHeading(wrapper.getKpiName()));
				List<KPIWrapper> rangeSlabs = kpiList.stream()
						.filter(k -> k.getKpiName() != null && k.getKpiName().equalsIgnoreCase(wrapper.getKpiName()))
						.collect(Collectors.toList());
				if (rangeSlabs != null && !rangeSlabs.isEmpty()) {
					driveWrapper.setRangeSlabs(rangeSlabs.get(ReportConstants.INDEX_ZER0).getRangeSlabs());
				}
				calculatePDFAndCDF(kpiList, wrapper.getKpiName(), driveWrapper);
				liveDriveMap.put(wrapper.getKpiName(), driveWrapper);
			}
			logger.info("Finally Going to return the liveDriveMap for master Report {} ",liveDriveMap);
			return liveDriveMap;
		} catch (Exception e) {
			logger.error("Exception inside method setDataOnMap ======{} ",Utils.getStackTrace(e));
		}
		logger.info("Finally returning liveDriveMap for master Report {} ",liveDriveMap);
		return liveDriveMap;
	}

	public String getScore(Double med, String kpi) {

		String score = "-";
		try {
			if (med != null && med != Double.NaN) {

				switch (kpi) {
				case DriveHeaderConstants.RSRP:
					score = getRsrpScore(med, score);
					break;

				case DriveHeaderConstants.SINR:
					score = getSinrScore(med, score);
					break;
				case ReportConstants.WEB_DOWNLOAD_DELAY:
					score = getWebDLDelayScore(med);

					break;

				case ReportConstants.DL_THROUGHPUT:
				case ReportConstants.FTP_DL_THROUGHPUT:
				case ReportConstants.HTTP_DL_THROUGHPUT:
					score = getDLScore(med);

					break;

				case ReportConstants.UL_THROUGHPUT:
				case ReportConstants.FTP_UL_THROUGHPUT:
				case ReportConstants.HTTP_UL_THROUGHPUT:
					score = getULScore(med);
					break;
					
				case LATENCY:
					score = getLatencyScore(med);
					break;
					
				case JITTER:
				case ReportConstants.PCI_PLOT:
					score = getJitterScore(med);
					break;

				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception in calculating the Score for kpi {} , med {} , {} ",kpi,med,e.getMessage());
		}
		return score;
	}

	private static String getJitterScore(Double med) {
		String score;
		if (med < ReportConstants.INDEX_THIRTY) {
			score = SCORE_EXCELLENT;
		} else if (med >= ReportConstants.INDEX_THIRTY && med < ReportConstants.INDEX_SIXTY) {
			score = SCORE_GOOD;
		} else if (med >= ReportConstants.INDEX_SIXTY && med < ReportConstants.INDEX_HUNDRED) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	private static String getLatencyScore(Double med) {
		String score;
		if (med < ReportConstants.INDEX_THIRTY) {
			score = SCORE_EXCELLENT;
		} else if (med >= ReportConstants.INDEX_THIRTY && med < ReportConstants.INDEX_FIFTY) {
			score = SCORE_GOOD;
		} else if (med >= ReportConstants.INDEX_FIFTY && med < ReportConstants.EIGHTY) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	private static String getULScore(Double med) {
		String score;
		if (med > ReportConstants.INDEX_THREE) {
			score = SCORE_EXCELLENT;
		} else if (med <= ReportConstants.INDEX_THREE && med > ReportConstants.ZERO_DOT_SEVEN_SIX_EIGHT) {
			score = SCORE_GOOD;
		} else if (med <= ReportConstants.ZERO_DOT_SEVEN_SIX_EIGHT && med >= ReportConstants.ZERO_DOT_TWO_FIVE_SIX) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	private static String getDLScore(Double med) {
		String score;
		if (med > 8) {
			score = SCORE_EXCELLENT;
		} else if (med <= 8 && med > ReportConstants.INDEX_TWO) {
			score = SCORE_GOOD;
		} else if (med <= ReportConstants.INDEX_TWO && med >= ReportConstants.ZERO_DOT_FIVE_ONE_TWO) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	private static String getWebDLDelayScore(Double med) {
		String score;
		if (med < ReportConstants.INDEX_THREE) {
			score = SCORE_EXCELLENT;
		} else if (med >= ReportConstants.INDEX_THREE && med < ReportConstants.INDEX_SIX) {
			score = SCORE_GOOD;
		} else if (med >= ReportConstants.INDEX_SIX && med < ReportConstants.TWELVE) {
			score = SCORE_AVG;
		} else {
		    score = SCORE_POOR;
		}
		return score;
	}

	private static String getSinrScore(Double med, String score) {
		if (med > ReportConstants.INDEX_FIVE) {
			score = SCORE_GOOD;
		} else if (med <= ReportConstants.INDEX_FIVE && med >= -ReportConstants.INDEX_TWO) {
			score = SCORE_AVG;
		} else if (med < -ReportConstants.INDEX_TWO) {
			score = SCORE_POOR;
		}
		return score;
	}

	private static String getRsrpScore(Double med, String score) {
		if (med > -ReportConstants.INDEX_NINTY_FIVE) {
			score = SCORE_GOOD;
		} else if (med <= -ReportConstants.INDEX_NINTY_FIVE && med >= -ReportConstants.ONE_HUNDRED_TEN) {
			score = SCORE_AVG;
		} else if (med < -ReportConstants.ONE_HUNDRED_TEN) {
			score = SCORE_POOR;
		}
		return score;
	}

	public static List<KPIWrapper> getKpiCount(List<KPIWrapper> kpiList, Map<String, List<Double>> liveDriveKpiMap) {
		try {
		kpiList.forEach(k -> {
		
			if (k.getKpiName().equalsIgnoreCase(KPI.RSRP.toString())) {
				k.getRangeSlabs().forEach(r -> {
					int count = (int) liveDriveKpiMap.get(KPI.RSRP.toString()).stream()
							.filter(v -> (v != null && v < r.getUpperLimit() && v >= r.getLowerLimit())).count();
					r.setCount(count);
					totalCount = totalCount + count;
				});
				k.setTotalCount(totalCount);

			} else if (k.getKpiName().equalsIgnoreCase(KPI.SINR.toString())) {
				totalCount = 0;
				k.getRangeSlabs().forEach(r -> {
					int count = (int) liveDriveKpiMap.get(KPI.SINR.toString()).stream()
							.filter(v -> (v != null && v < r.getUpperLimit() && v >= r.getLowerLimit())).count();
					r.setCount(count);
					totalCount = totalCount + count;
				});
				k.setTotalCount(totalCount);
			}

			else if (k.getKpiName().equalsIgnoreCase(DriveHeaderConstants.DL_THROUGHPUT)) {
				totalCount = 0;
				k.getRangeSlabs().forEach(r -> {
					int count = (int) liveDriveKpiMap.get(KPI.DL.toString()).stream()
							.filter(v -> (v != null && v < r.getUpperLimit() && v >= r.getLowerLimit())).count();
					r.setCount(count);
					totalCount = totalCount + count;
				});
				k.setTotalCount(totalCount);
			} else if (k.getKpiName().equalsIgnoreCase(DriveHeaderConstants.UL_THROUGHPUT)) {
				totalCount = 0;
				k.getRangeSlabs().forEach(r -> {
					int count = (int) liveDriveKpiMap.get(KPI.UL.toString()).stream()
							.filter(v -> (v != null && v < r.getUpperLimit() && v >= r.getLowerLimit())).count();
					r.setCount(count);
					totalCount = totalCount + count;
				});
				k.setTotalCount(totalCount);
			}
		});
		}
		catch(Exception e) {
			logger.error("error inside the method getKpiCount{}",Utils.getStackTrace(e));
		}
		return kpiList;
	}

	private static void setStatsData(LiveDriveWrapper driveWrapper, Statistics statistics) {
		if (statistics.getMean() != null) {
			driveWrapper.setMean(statistics.getMean());
		}
		if (statistics.getPercentileInclusive(NINETY) != null) {
			driveWrapper.setPer90(statistics.getPercentileInclusive(NINETY));
		}
		if (statistics.getPercentileInclusive(TEN) != null) {
			driveWrapper.setPer10(statistics.getPercentileInclusive(TEN));
		}
		if (statistics.getMax() != null) {
			driveWrapper.setMax((double) statistics.getMax());
		}
		if (statistics.getMin() != null) {
			driveWrapper.setMin((double) statistics.getMin());
		}
		if (statistics.getStdDev() != null) {
			driveWrapper.setStdev(statistics.getStdDev());
		}
		if (driveWrapper.getMin() != null && driveWrapper.getMax() != null) {
			driveWrapper.setMedKPI(LiveDriveReportUtil.getMed(driveWrapper.getMin(), driveWrapper.getMax()));
		}
	}


	private static void calculatePDFAndCDF(List<KPIWrapper> kpiList, String kpiName, LiveDriveWrapper driveWrapper) {
		logger.info("Inside method calculatePDFAndCDF for kpiName {} ",kpiName);
		List<ChartBeanWrapper> chartList = new ArrayList<>();
		if (kpiList != null && !kpiList.isEmpty()) {
			kpiList.forEach(x -> {
				if (x.getKpiName() != null && x.getKpiName().equalsIgnoreCase(kpiName)) {
					try {
						if(x.getTotalCount()!=null){
							setWrapperList(chartList, x);
						}
					} catch (Exception e) {
						logger.error("Inside calculatePDFAndCDF method : {}" + ExceptionUtils.getStackTrace(e));
					}
				}
				driveWrapper.setChartList(chartList);
			});
		}
	}

	private static void setWrapperList(List<ChartBeanWrapper> chartList, KPIWrapper x) {
		totalCount = x.getTotalCount();
		cdf = ReportConstants.INDEX_ZER0;
		x.getRangeSlabs().forEach(r -> {
			if (r != null) {
				ChartBeanWrapper beanWrapper = new ChartBeanWrapper();
				pdf = ReportConstants.INDEX_ZER0;
				if (totalCount!=null && totalCount != ReportConstants.INDEX_ZER0 &&  r.getCount()!=null && r.getCount() != ReportConstants.INDEX_ZER0) {
					pdf = (r.getCount() * ReportConstants.HUNDRED) / totalCount;
				}
				cdf += pdf;
				beanWrapper.setPerKPI(pdf);
				beanWrapper.setCdfKPI(cdf);
				beanWrapper.setRateKPI(r.getUpperLimit() + " - " + r.getLowerLimit());
				chartList.add(beanWrapper);
			}
		});
	}

	public static Double calculateCHI(Map<String, LiveDriveWrapper> livedrivewrapper, String netWorkType) {
		Double chi = null;
		try {
			if (livedrivewrapper != null) {
				if (NETWORK_CHECK_NOT_REQUIRED.equals(netWorkType)) {
					Double signalQualityMean = 0.0;
					Double signalStrengthMean = 0.0;
					if (livedrivewrapper.get(KPI.SINR.toString()).getMean() != null) {
						signalQualityMean = getAvg(livedrivewrapper.get(KPI.SINR.toString()).getMean(),
								livedrivewrapper.get(KPI.ECNO.toString()).getMean(),
								livedrivewrapper.get(KPI.RXQUALITY.toString()).getMean());
					}
					if (livedrivewrapper.get(KPI.RSRP.toString()).getMean() != null) {
						signalStrengthMean = getAvg(livedrivewrapper.get(KPI.RSRP.toString()).getMean(),
								livedrivewrapper.get(KPI.RSCP.toString()).getMean(),
								livedrivewrapper.get(KPI.RSSI.toString()).getMean());
					}
					if (signalStrengthMean == ReportConstants.INDEX_ZER0 || signalQualityMean == ReportConstants.INDEX_ZER0) {
						chi = null;
					} else {
						chi = ((ReportConstants.ZERO_DOT_FOUR * signalStrengthMean + ReportConstants.ZERO_DOT_SIX * signalQualityMean) / ReportConstants.INDEX_THREE) * ReportConstants.HUNDRED;
					}
				} else {
					if (livedrivewrapper.get(KPI.SINR.toString()) != null && livedrivewrapper.get(KPI.SINR.toString()).getMean() != null
							&& livedrivewrapper.get(KPI.RSRP.toString()).getMean() != null) {
						chi = ((ReportConstants.ZERO_DOT_FOUR * livedrivewrapper.get(KPI.RSRP.toString()).getMean()
								+ ReportConstants.ZERO_DOT_SIX * livedrivewrapper.get(KPI.SINR.toString()).getMean()) / ReportConstants.INDEX_THREE) * ReportConstants.HUNDRED;
					}
				}
			}
		} catch (Exception e) {
			chi = null;
			logger.error("error on CHI calculation: " + Utils.getStackTrace(e));
		}
		if (chi != null) {
            return Double.parseDouble(decimalFormat.format(chi));
        } else {
            return chi;
        }
	}

	public static Double calculateDHI(Map<String, LiveDriveWrapper> livedrivewrapper) {
		Double dhi = null;
		try {
			if(livedrivewrapper.get(ReportConstants.DL_THROUGHPUT) != null && livedrivewrapper.get(ReportConstants.WEB_DOWNLOAD_DELAY) != null 
					&& livedrivewrapper.get(ReportConstants.UL_THROUGHPUT) != null && livedrivewrapper.get(ReportConstants.LATENCY) != null 
					&& livedrivewrapper.get(ReportConstants.JITTER) != null) {
				
				dhi = ((ReportConstants.ZERO_DOT_THREE * livedrivewrapper.get(ReportConstants.DL_THROUGHPUT).getMean()
						+ ReportConstants.ZERO_DOT_THREE * livedrivewrapper.get(ReportConstants.WEB_DOWNLOAD_DELAY).getMean()
						+ ReportConstants.ZERO_DOT_FIFTEEN * livedrivewrapper.get(ReportConstants.UL_THROUGHPUT).getMean()
						+ ReportConstants.ZERO_DOT_FIFTEEN * livedrivewrapper.get(ReportConstants.LATENCY).getMean()
						+ ReportConstants.ZERO_DOT_ONE * livedrivewrapper.get(ReportConstants.JITTER).getMean()) 
						/ ReportConstants.INDEX_SIX) * ReportConstants.HUNDRED;
				return Double.parseDouble(decimalFormat.format(dhi));
			}
		} catch (Exception e) {
			logger.error("Exception inside calculateDHI {} ",Utils.getStackTrace(e));
		}
		return dhi;
	}

	public static Double calculateNHI(Double chi, Double dhi) {
		Double nhi = null;
		try {
			nhi = (chi + dhi) / ReportConstants.INDEX_TWO;
		} catch (Exception e) {
			nhi = null;
			logger.error("error on NHI calculation: " + e.getMessage());
		}
		return Double.parseDouble(decimalFormat.format(nhi));
	}

	public static Double getAvg(Double... doubles) {
		int count = ReportConstants.INDEX_ZER0;
		double sum = 0.0;
		for (int i = ReportConstants.INDEX_ZER0; i < doubles.length; i++) {
			if (doubles[i] != null && Double.isNaN(doubles[i])) {
				sum += doubles[i];
				count++;
			}
		}
		if (count != ReportConstants.INDEX_ZER0) {
            return sum / count;
        }
		return 0.0;
	}


	public static long getUniqueCellId(Map<String, List<Double>> liveDriveKpiMap) {
		long count = ReportConstants.INDEX_ZER0;
		try {
			count = liveDriveKpiMap.get(CELLID).stream().distinct().count();
		} catch (Exception e) {
			logger.info("Inside method getUniqueCellId for data {} , {} ",liveDriveKpiMap.get(CELLID),e.getMessage());
		}
		return count;
	}

	public static List<DeviceDataWapper> getHandoverData(List<TrackPosition> driveTrackList) {
		List<DeviceDataWapper> handoverDataList = new ArrayList<>();
		DeviceDataWapper handoverData = null;
		if (driveTrackList != null && !driveTrackList.isEmpty()) {
			for (TrackPosition obj : driveTrackList) {
				handoverData = setHandoverTableData(obj);
				if (handoverData != null) {
					handoverDataList.add(handoverData);
				}
			}
		}
		logger.info("HnadoverdataList returned successfully for live Drive  of size " + handoverDataList.size());
		return handoverDataList;
	}

	public static DeviceDataWapper setHandoverTableData(TrackPosition obj) {
		DeviceDataWapper handoverData = null;
		try {
			String status = null;
			if (LiveDriveReportConstants.PASS_UPPER.equalsIgnoreCase(obj.getEventLogStatus())) {
				status = LiveDriveReportConstants.PASS_UPPER;
			} else if (LiveDriveReportConstants.FAIL_UPPER.equalsIgnoreCase(obj.getEventLogStatus())) {
				status = LiveDriveReportConstants.FAIL_UPPER;
			}
			if (obj.getOldOrAttemptedPci() != null && status != null) {
				String[] pcis = obj.getOldOrAttemptedPci().split("\\|");
				if (pcis != null && pcis.length > ReportConstants.INDEX_ZER0) {
					return new DeviceDataWapper(Integer.parseInt(obj.getOldOrAttemptedCellId()),
							obj.getCellId(), status, Integer.parseInt(pcis[ReportConstants.INDEX_ZER0]), Integer.parseInt(pcis[1]));
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method setHandoverTableData " + e.getMessage());
		}
		return handoverData;
	}

	public static Map<String, List<Double>> convertCsvKPIWise(List<String[]> listArray) {
		Map<String, List<Double>> kpiMap = new HashMap<>();
		List<Double> sinrList = new ArrayList<>();
		List<Double> rsrpList = new ArrayList<>();
		List<Double> cellID = new ArrayList<>();
		List<Double> dlList = new ArrayList<>();
		List<Double> ulList = new ArrayList<>();

		List<Double> httpDlList = new ArrayList<>();
		List<Double> httpUlList = new ArrayList<>();
		List<Double> ftpDlList = new ArrayList<>();
		List<Double> ftpUlList = new ArrayList<>();
		List<Double> webDelayList = new ArrayList<>();
		List<Double> jitterList = new ArrayList<>();
		List<Double> latencyList = new ArrayList<>();
		List<Double> timeStamp = new ArrayList<>();

		listArray.forEach(strings -> {
			if (!strings[DriveHeaderConstants.INDEX_TIMESTAMP].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_TIMESTAMP]))) {
                timeStamp.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_TIMESTAMP]));
            }
			if (!strings[DriveHeaderConstants.INDEX_SINR].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_SINR]))) {
                sinrList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_SINR]));
            }
			if (!strings[DriveHeaderConstants.INDEX_RSRP].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_RSRP]))) {
                rsrpList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_RSRP]));
            }
		
			if (!strings[DriveHeaderConstants.INDEX_DL].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_DL]))) {
				Double dlValue = Double.parseDouble(strings[DriveHeaderConstants.INDEX_DL]);
				String testType = getTestType(strings);
                dlList.add(dlValue);
                if(testType!=null && !testType.isEmpty() && testType.equalsIgnoreCase(KPI.FTP_DOWNLOAD.toString())){
                	ftpDlList.add(dlValue);
                }else if(testType!=null && !testType.isEmpty() && testType.equalsIgnoreCase(KPI.HTTP_DOWNLOAD.toString())){
                	httpDlList.add(dlValue);
                }
            }
			if (!strings[DriveHeaderConstants.INDEX_UL].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_UL]))) {
                ulList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_UL]));
                Double ulValue = Double.parseDouble(strings[DriveHeaderConstants.INDEX_UL]);
				String testType = getTestType(strings);
                ulList.add(ulValue);
                if(testType!=null && !testType.isEmpty() && testType.equalsIgnoreCase(KPI.FTP_DOWNLOAD.toString())){
                	ftpUlList.add(ulValue);
                }else if(testType!=null && !testType.isEmpty() && testType.equalsIgnoreCase(KPI.HTTP_DOWNLOAD.toString())){
                	httpUlList.add(ulValue);
                }
            }
			if (!strings[DriveHeaderConstants.INDEX_LATENCY].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_LATENCY]))) {
                latencyList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_LATENCY]));
            }
			if (!strings[DriveHeaderConstants.INDEX_CELLID].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_CELLID]))) {
                cellID.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_CELLID]));
            }
			if (!strings[DriveHeaderConstants.INDEX_WEBDELAY].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_WEBDELAY]))) {
                webDelayList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_WEBDELAY]));
            }
			if (!strings[DriveHeaderConstants.INDEX_JITTER].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(strings[DriveHeaderConstants.INDEX_JITTER]))) {
                jitterList.add(Double.parseDouble(strings[DriveHeaderConstants.INDEX_JITTER]));
            }
			

		});

		kpiMap.put(TIME, timeStamp);
		kpiMap.put(KPI.SINR.toString(), sinrList);
		kpiMap.put(KPI.RSRP.toString(), rsrpList);
		kpiMap.put(KPI.DL.toString(), dlList);
		kpiMap.put(KPI.UL.toString(), ulList);
		kpiMap.put(KPI.LATENCY.toString(), latencyList);
		kpiMap.put(CELLID, cellID);
		kpiMap.put(KPI.WEBDELAY.toString(), webDelayList);
		kpiMap.put(KPI.JITTER.toString(), jitterList);
		return kpiMap;
	}

	private static String getTestType(String[] strings) {
		return strings[DriveHeaderConstants.INDEX_TEST_TYPE];
	}
	
	public static Map<String, List<Double>> getKPiWiseValueList(List<String[]> listArray, List<KPIWrapper> kpiList, Integer testTypeIndex, Integer timestampIndex) {
		logger.info("Inside method getKPiWiseValueList for test TypeIndex {} ",testTypeIndex);
		Map<String, List<Double>> kpiMap = new HashMap<>();
		if(kpiList!=null){
			kpiList.forEach(kpiWrapper->{
				List<String[]> filteredData = listArray.stream().filter(Objects::nonNull).filter(e -> testTypeIndex != null && testTypeIndex < e.length).filter(array -> (array[testTypeIndex] != null && !array[testTypeIndex].isEmpty())).collect(Collectors.toList());
				List<Double> kpiValueList = new ArrayList<>();
				if(kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.FTP_DL_THROUGHPUT)){
					kpiValueList = processKpiValueList(testTypeIndex,ReportConstants.FTP_DOWNLOAD, kpiWrapper, filteredData);
					kpiMap.put(kpiWrapper.getKpiName(), kpiValueList);
				}else if(kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.FTP_UL_THROUGHPUT)){
					kpiValueList = processKpiValueList(testTypeIndex,ReportConstants.FTP_UPLOAD, kpiWrapper, filteredData);
					kpiMap.put(kpiWrapper.getKpiName(), kpiValueList);
				}else if(kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.HTTP_DL_THROUGHPUT)){
					kpiValueList = processKpiValueList(testTypeIndex,ReportConstants.HTTP_DOWNLOAD, kpiWrapper, filteredData);
					kpiMap.put(kpiWrapper.getKpiName(), kpiValueList);
				}else if(kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.HTTP_UL_THROUGHPUT)){
					kpiValueList = processKpiValueList(testTypeIndex,ReportConstants.HTTP_UPLOAD, kpiWrapper, filteredData);
					kpiMap.put(kpiWrapper.getKpiName(), kpiValueList);
				}else{
					try {
						kpiValueList  = listArray.stream()
								.filter(Objects::nonNull)
								.map(e -> kpiWrapper.getIndexKPI() < e.length ? e[kpiWrapper.getIndexKPI()] : null)
								.filter(e -> e != null && e.trim().length() > ReportConstants.INDEX_ZER0)
								.map(Double::valueOf)
								.collect(Collectors.toList());
						kpiMap.put(kpiWrapper.getKpiName(), kpiValueList);
					} catch (Exception e) {
						logger.error("Exception inside method  in preparing listOfValue for each kpis {} ",e.getMessage());
					}
				}
			});
			List<Double> kpiValueList  = listArray.stream()
					.filter(Objects::nonNull)
					.map(e -> ((timestampIndex != null) && (timestampIndex < e.length)) ? e[timestampIndex] : null)
					.filter(e -> e != null && e.trim().length() > ReportConstants.INDEX_ZER0)
					.map(Double::valueOf)
					.collect(Collectors.toList());
			kpiMap.put(DriveHeaderConstants.TIME, kpiValueList);
		}
		return kpiMap;
	}

	private static List<Double> processKpiValueList(Integer testTypeIndex, String kpiName, KPIWrapper kpiWrapper,
			List<String[]> filteredData) {
			return filteredData.stream()
			.filter(array -> array != null && array.length > testTypeIndex && testTypeIndex != null
			&& kpiName.equalsIgnoreCase(array[testTypeIndex]))
			.map(e -> kpiWrapper != null && kpiWrapper.getIndexKPI() != null
			&& kpiWrapper.getIndexKPI() < e.length ? e[kpiWrapper.getIndexKPI()] : null)
			.filter(e -> e != null && e.trim().length() > ReportConstants.INDEX_ZER0)
			.map(Double::valueOf)
			.collect(Collectors.toList());
			}
	/**Private static void printData(String[] e) {
		try {
			for (int i = 40; i < e.length; i++) {
				System.out.println("e data " + e[i]);
				logger.info("e[{}] , {} ", i, e[i]);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}*/
	
	

	private static List<RangeScoreWrapper> getRangeScoreList(String kpi) {
		List<RangeScoreWrapper> listRangeScore = new ArrayList<>();
		try {

				switch (kpi) {
				case DriveHeaderConstants.RSRP:
					listRangeScore.add(new RangeScoreWrapper(RSRP_GOOD_RANGE, SCORE_GOOD));
					listRangeScore.add(new RangeScoreWrapper(RSRP_AVG_RANGE, SCORE_AVG));
					listRangeScore.add(new RangeScoreWrapper(RSRP_POOR_RANGE, SCORE_POOR));
					
					break;

				case DriveHeaderConstants.SINR:
					listRangeScore.add(new RangeScoreWrapper(SINR_GOOD_RANGE, SCORE_GOOD));
					listRangeScore.add(new RangeScoreWrapper(SINR_AVG_RANGE, SCORE_AVG));
					listRangeScore.add(new RangeScoreWrapper(SINR_POOR_RANGE, SCORE_POOR));
					
					break;

				case ReportConstants.DL_THROUGHPUT:
				case ReportConstants.FTP_DL_THROUGHPUT:
				case ReportConstants.HTTP_DL_THROUGHPUT:
					listRangeScore.add(new RangeScoreWrapper(DL_EXCELENT_RANGE, SCORE_EXCELLENT));
					listRangeScore.add(new RangeScoreWrapper(DL_GOOD_RANGE, SCORE_GOOD));
					listRangeScore.add(new RangeScoreWrapper(DL_AVG_RANGE, SCORE_AVG));
					listRangeScore.add(new RangeScoreWrapper(DL_POOR_RANGE, SCORE_POOR));

					break;

				case ReportConstants.UL_THROUGHPUT:
				case ReportConstants.FTP_UL_THROUGHPUT:
				case ReportConstants.HTTP_UL_THROUGHPUT:
					listRangeScore.add(new RangeScoreWrapper(UL_EXCELENT_RANGE, SCORE_EXCELLENT));
					listRangeScore.add(new RangeScoreWrapper(UL_GOOD_RANGE, SCORE_GOOD));
					listRangeScore.add(new RangeScoreWrapper(UL_AVG_RANGE, SCORE_AVG));
					listRangeScore.add(new RangeScoreWrapper(UL_POOR_RANGE, SCORE_POOR));

					
					break;

				default:
					break;
				}
		} catch (Exception e) {
			logger.error("Exception inside method getRangeScoreList for kpi {} , errMsg {} ",kpi,e.getMessage());
		}

		return listRangeScore;
	}
}
