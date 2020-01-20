package com.inn.foresight.module.nv.livedrive.utils;

import java.util.Map;

import com.inn.foresight.module.nv.livedrive.constants.KPI;
import com.inn.foresight.module.nv.report.LiveDriveWrapper;

public class CountRangeUtils {
	
	@SuppressWarnings("unused")
	private void incrUl(Double ulAvg, String netWorkType, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.UL.toString();
		if (ulAvg < 0.256)
			livedrivewrapper.get(kpi).incrCountRange1();
		if (ulAvg >= 0.256 && ulAvg <= 0.768)
			livedrivewrapper.get(kpi).incrCountRange2();
		if (ulAvg > 0.768 && ulAvg <= 3)
			livedrivewrapper.get(kpi).incrCountRange3();
		if (ulAvg > 3)
			livedrivewrapper.get(kpi).incrCountRange4();
		livedrivewrapper.get(kpi).addValue(ulAvg);
	}

	@SuppressWarnings("unused")
	private void incrDl(Double dlAvg, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.DL.toString();
		if (dlAvg != null) {
			if (dlAvg < 0.512)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (dlAvg >= 0.512 && dlAvg <= 2)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (dlAvg > 2 && dlAvg <= 8)
				livedrivewrapper.get(kpi).incrCountRange3();
			if (dlAvg > 8)
				livedrivewrapper.get(kpi).incrCountRange4();

			livedrivewrapper.get(kpi).addValue(dlAvg);
		}

	}

	@SuppressWarnings("unused")
	private void incrWebDelay(Double responseTime, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.WEBDELAY.toString();
		if (responseTime != null) {

			if (responseTime >= 12)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (responseTime >= 6 && responseTime < 12)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (responseTime >= 3 && responseTime < 6)
				livedrivewrapper.get(kpi).incrCountRange3();
			if (responseTime < 3)
				livedrivewrapper.get(kpi).incrCountRange4();

			livedrivewrapper.get(kpi).addValue(responseTime);
		}
	}

	@SuppressWarnings("unused")
	private void incrLatency(Double avgLatency, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.LATENCY.toString();
		if (avgLatency != null) {
			if (avgLatency > 80)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (avgLatency >= 50 && avgLatency <= 80)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (avgLatency >= 30 && avgLatency < 50)
				livedrivewrapper.get(kpi).incrCountRange3();
			if (avgLatency < 30)
				livedrivewrapper.get(kpi).incrCountRange4();

			livedrivewrapper.get(kpi).addValue(avgLatency);
		}
	}

	@SuppressWarnings("unused")
	private void incrJitter(Double avgJitter, Map<String, LiveDriveWrapper> livedrivewrapper) {

		String kpi = KPI.JITTER.toString();
		if (avgJitter != null) {
			if (avgJitter >= 100)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (avgJitter >= 60 && avgJitter < 100)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (avgJitter >= 30 && avgJitter < 60)
				livedrivewrapper.get(kpi).incrCountRange3();
			if (avgJitter < 30)
				livedrivewrapper.get(kpi).incrCountRange4();

			livedrivewrapper.get(kpi).addValue(avgJitter);
		}
	}

	@SuppressWarnings("unused")
	private void incrRsrp(Integer rsrp, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.RSRP.toString();
		if (rsrp != null) {
			if (rsrp < -100)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (rsrp >= -100 && rsrp <= -90)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (rsrp > -90)
				livedrivewrapper.get(kpi).incrCountRange3();

			livedrivewrapper.get(kpi).addValue(Double.valueOf(rsrp));
		}
	}

	@SuppressWarnings("unused")
	private void incrSinr(Double sinr, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.SINR.toString();
		if (sinr != null) {
			if (sinr < -2)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (sinr >= -2 && sinr < 5)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (sinr >= 5)
				livedrivewrapper.get(kpi).incrCountRange3();

			livedrivewrapper.get(kpi).addValue(sinr);
		}
	}

	@SuppressWarnings("unused")
	private void incrRscp(Integer rscp, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.RSCP.toString();
		if (rscp != null) {
			if (rscp < -97)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (rscp >= -97 && rscp <= -87)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (rscp > -87)
				livedrivewrapper.get(kpi).incrCountRange3();
			livedrivewrapper.get(kpi).addValue(Double.valueOf(rscp));
		}
	}

	@SuppressWarnings("unused")
	private void incrEcNo(Integer ecNo, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.ECNO.toString();
		if (ecNo != null) {
			if (ecNo < -16)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (ecNo >= -16 && ecNo < -12)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (ecNo >= -12)
				livedrivewrapper.get(kpi).incrCountRange3();

			livedrivewrapper.get(kpi).addValue(Double.valueOf(ecNo));
		}
	}

	@SuppressWarnings("unused")
	private void incrRssi(Integer rssi, Map<String, LiveDriveWrapper> livedrivewrapper) {
		String kpi = KPI.RSSI.toString();
		if (rssi != null) {
			if (rssi < -84)
				livedrivewrapper.get(kpi).incrCountRange1();
			if (rssi >= -84 && rssi <= -74)
				livedrivewrapper.get(kpi).incrCountRange2();
			if (rssi > -74)
				livedrivewrapper.get(kpi).incrCountRange3();
			livedrivewrapper.get(kpi).addValue(Double.valueOf(rssi));
		}
	}

	@SuppressWarnings({ "unused", "unlikely-arg-type" })
	private void incrRxQuality(Integer rxQuality, Map<String, LiveDriveWrapper> livedrivewrapper) {
		if (rxQuality != null) {
			if (rxQuality > 5)
				livedrivewrapper.get(KPI.RXQUALITY).incrCountRange1();

			if (rxQuality == 4 || rxQuality == 5)
				livedrivewrapper.get(KPI.RXQUALITY).incrCountRange2();

			if (rxQuality <= 3)
				livedrivewrapper.get(KPI.RXQUALITY).incrCountRange3();
			livedrivewrapper.get(KPI.RXQUALITY).addValue(Double.valueOf(rxQuality));
		}
	}
}
