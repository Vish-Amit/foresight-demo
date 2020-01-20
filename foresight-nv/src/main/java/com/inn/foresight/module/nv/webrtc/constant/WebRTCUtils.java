package com.inn.foresight.module.nv.webrtc.constant;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallDataWrapper;

public class WebRTCUtils {

	private WebRTCUtils() {
	}

	public static void setDataByMediaType(HBaseResult hbaseResult, String mediaType, WebRTCCallDataWrapper wrapper,
			WebRTCCallDataWrapper previousWrapper) {
		Double byteSentAS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_SEND_AUDIO_SENDER);
		Double byteSentAR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_SEND_AUDIO_RECIVER);
		Double byteSentVS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_SEND_VIDEO_SENDER);
		Double byteSentVR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_SEND_VIDEO_RECIVER);
		Double byteRecvAS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_RECIVE_AUDIO_SENDER);
		Double byteRecvAR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_RECIVE_AUDIO_RECIVER);
		Double byteRecvVS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_RECIVE_VIDEO_SENDER);
		Double byteRecvVR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_BYTE_RECIVE_VIDEO_RECIVER);

		Double pcktSentAS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_SEND_AUDIO_SENDER);
		Double pcktSentAR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_SEND_AUDIO_RECIVER);
		Double pcktSentVS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_SEND_VIDEO_SENDER);
		Double pcktSentVR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_SEND_VIDEO_RECIVER);
		Double pcktRecvAS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_RECIVE_AUDIO_SENDER);
		Double pcktRecvAR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_RECIVE_AUDIO_RECIVER);
		Double pcktRecvVS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_RECIVE_VIDEO_SENDER);
		Double pcktRecvVR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_RECIVE_VIDEO_RECIVER);
		if (WebRTCConstant.AUDIO.equalsIgnoreCase(mediaType)) {
			populateAudioData(wrapper, byteSentAS, byteSentAR, byteRecvAS, byteRecvAR, pcktSentAS, pcktSentAR,
					pcktRecvAS, pcktRecvAR);
		} else {
			populateVideoData(wrapper, byteSentAS, byteSentAR, byteSentVS, byteSentVR, byteRecvAS, byteRecvAR,
					byteRecvVS, byteRecvVR, pcktSentAS, pcktSentAR, pcktSentVS, pcktSentVR, pcktRecvAS, pcktRecvAR,
					pcktRecvVS, pcktRecvVR);

		}
		calculateDifferanceOfKpi(wrapper, previousWrapper);
	}

	private static void populateVideoData(WebRTCCallDataWrapper wrapper, Double byteSentAS, Double byteSentAR,
			Double byteSentVS, Double byteSentVR, Double byteRecvAS, Double byteRecvAR, Double byteRecvVS,
			Double byteRecvVR, Double pcktSentAS, Double pcktSentAR, Double pcktSentVS, Double pcktSentVR,
			Double pcktRecvAS, Double pcktRecvAR, Double pcktRecvVS, Double pcktRecvVR) {
		wrapper.setBytesSent((byteSentVS != null ? byteSentVS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteSentVR != null ? byteSentVR : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteSentAS != null ? byteSentAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteSentAR != null ? byteSentAR : WebRTCConstant.DEFAULT_ADD_VALUE));
		wrapper.setBytesReceived((byteRecvAS != null ? byteRecvAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteRecvAR != null ? byteRecvAR : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteRecvVR != null ? byteRecvVR : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteRecvVS != null ? byteRecvVS : WebRTCConstant.DEFAULT_ADD_VALUE));
		populatePKTData(wrapper, pcktSentAS, pcktSentAR, pcktSentVS, pcktSentVR, pcktRecvAS, pcktRecvAR, pcktRecvVS,
				pcktRecvVR);
	}

	private static void populatePKTData(WebRTCCallDataWrapper wrapper, Double pcktSentAS, Double pcktSentAR,
			Double pcktSentVS, Double pcktSentVR, Double pcktRecvAS, Double pcktRecvAR, Double pcktRecvVS,
			Double pcktRecvVR) {
		wrapper.setPacketsSent((pcktSentAS != null ? pcktSentAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktSentAR != null ? pcktSentAR : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktSentVS != null ? pcktSentVS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktSentVR != null ? pcktSentVR : WebRTCConstant.DEFAULT_ADD_VALUE));
		wrapper.setPacketsRecieved((pcktRecvAS != null ? pcktRecvAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktRecvAR != null ? pcktRecvAR : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktRecvVS != null ? pcktRecvVS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktRecvVR != null ? pcktRecvVR : WebRTCConstant.DEFAULT_ADD_VALUE));
	}

	private static void populateAudioData(WebRTCCallDataWrapper wrapper, Double byteSentAS, Double byteSentAR,
			Double byteRecvAS, Double byteRecvAR, Double pcktSentAS, Double pcktSentAR, Double pcktRecvAS,
			Double pcktRecvAR) {
		wrapper.setBytesSent((byteSentAS != null ? byteSentAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteSentAR != null ? byteSentAR : WebRTCConstant.DEFAULT_ADD_VALUE));
		wrapper.setBytesReceived((byteRecvAS != null ? byteRecvAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (byteRecvAR != null ? byteRecvAR : WebRTCConstant.DEFAULT_ADD_VALUE));
		populatePacketData(wrapper, pcktSentAS, pcktSentAR, pcktRecvAS, pcktRecvAR);
	}

	private static void populatePacketData(WebRTCCallDataWrapper wrapper, Double pcktSentAS, Double pcktSentAR,
			Double pcktRecvAS, Double pcktRecvAR) {
		wrapper.setPacketsSent((pcktSentAS != null ? pcktSentAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktSentAR != null ? pcktSentAR : WebRTCConstant.DEFAULT_ADD_VALUE));
		wrapper.setPacketsRecieved((pcktRecvAS != null ? pcktRecvAS : WebRTCConstant.DEFAULT_ADD_VALUE)
				+ (pcktRecvAR != null ? pcktRecvAR : WebRTCConstant.DEFAULT_ADD_VALUE));
	}

	private static void calculateDifferanceOfKpi(WebRTCCallDataWrapper wrapper, WebRTCCallDataWrapper previousWrapper) {
		if (previousWrapper != null && wrapper != null) {
			Double value = null;
			if (previousWrapper.getPacketsSent() != null && wrapper.getPacketsSent() != null) {
				value = wrapper.getPacketsSent() - previousWrapper.getPacketsSent();
				if (isPositiveNumber(value)) {
					wrapper.setPcktSentDiff(value);
				}
			} else if (previousWrapper.getPacketsSent() != null) {
				value = previousWrapper.getPcktSentDiff();
				if (isPositiveNumber(value)) {
					wrapper.setPcktSentDiff(value);
				}
			} else {
				if (previousWrapper.getPcktSentDiff() != null) {
					value = previousWrapper.getPcktSentDiff();
					if (isPositiveNumber(value)) {
						wrapper.setPcktSentDiff(value);
					}
				} else {
					value = wrapper.getPacketsSent();
					if (isPositiveNumber(value)) {
						wrapper.setPcktSentDiff(value);
					}
				}
			}

			if (previousWrapper.getPacketsRecieved() != null && wrapper.getPacketsRecieved() != null) {
				value = wrapper.getPacketsRecieved() - previousWrapper.getPacketsRecieved();
				if (isPositiveNumber(value)) {
					wrapper.setPcktRecvDiff(value);
				}
			} else if (previousWrapper.getPacketsRecieved() != null) {
				value = previousWrapper.getPcktRecvDiff();
				if (isPositiveNumber(value)) {
					wrapper.setPcktRecvDiff(value);
				}
			} else {
				if (previousWrapper.getPcktRecvDiff() != null) {
					value = previousWrapper.getPcktRecvDiff();
					if (isPositiveNumber(value)) {
						wrapper.setPcktRecvDiff(value);
					}
				} else {
					value = wrapper.getPacketsRecieved();
					if (isPositiveNumber(value)) {
						wrapper.setPcktRecvDiff(value);
					}
				}
			}

			if (previousWrapper.getPacketsLost() != null && wrapper.getPacketsLost() != null) {
				value = wrapper.getPacketsLost() - previousWrapper.getPacketsLost();
				if (isPositiveNumber(value)) {
					wrapper.setPcktLostDiff(value);
				}
			} else if (previousWrapper.getPacketsLost() != null) {
				value = previousWrapper.getPcktLostDiff();
				if (isPositiveNumber(value)) {
					wrapper.setPcktLostDiff(value);
				}
			} else {
				if (previousWrapper.getPcktLostDiff() != null) {
					value = previousWrapper.getPcktLostDiff();
					if (isPositiveNumber(value)) {
						wrapper.setPcktLostDiff(value);
					}
				} else {
					value = wrapper.getPacketsLost();
					if (isPositiveNumber(value)) {
						wrapper.setPcktLostDiff(value);
					}
				}
			}

			if (previousWrapper.getBytesSent() != null && wrapper.getBytesSent() != null) {
				value = wrapper.getBytesSent() - previousWrapper.getBytesSent();
				if (isPositiveNumber(value)) {
					wrapper.setBtSentDiff(value);
				}
			} else if (previousWrapper.getBytesSent() != null) {
				value = previousWrapper.getBtSentDiff();
				if (isPositiveNumber(value)) {
					wrapper.setBtSentDiff(value);
				}
			} else {
				if (previousWrapper.getBtSentDiff() != null) {
					value = previousWrapper.getBtSentDiff();
					if (isPositiveNumber(value)) {
						wrapper.setBtSentDiff(value);
					}
				} else {
					value = wrapper.getBytesSent();
					if (isPositiveNumber(value)) {
						wrapper.setBtSentDiff(value);
					}
				}
			}

			if (previousWrapper.getBytesReceived() != null && wrapper.getBytesReceived() != null) {
				value = wrapper.getBytesReceived() - previousWrapper.getBytesReceived();
				if (isPositiveNumber(value)) {
					wrapper.setBtRecvDiff(value);
				}
			} else if (previousWrapper.getBytesReceived() != null) {
				value = previousWrapper.getBtRecvDiff();
				if (isPositiveNumber(value)) {
					wrapper.setBtRecvDiff(value);
				}
			} else {
				if (previousWrapper.getBtRecvDiff() != null) {
					value = previousWrapper.getBtRecvDiff();
					if (isPositiveNumber(value)) {
						wrapper.setBtRecvDiff(value);
					}
				} else {
					value = wrapper.getBytesReceived();
					if (isPositiveNumber(value)) {
						wrapper.setBtRecvDiff(value);
					}
				}
			}

		}
	}

	public static void setPacketLossByMediaType(HBaseResult hbaseResult, String mediaType,
			WebRTCCallDataWrapper wrapper) {

		Double pcktLostAS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_AUDIO_SENDER);
		Double pcktLostAR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_AUDIO_RECIVER);
		Double pcktLostVS = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_VIDEO_SENDER);
		Double pcktLostVR = hbaseResult.getStringAsDouble(WebRTCConstant.COLUMN_PACKETLOSS_VIDEO_RECIVER);

		if (WebRTCConstant.AUDIO.equalsIgnoreCase(mediaType)) {

			if (pcktLostAS != null || pcktLostAR != null) {
				wrapper.setPacketsLost(
						(pcktLostAS != null && pcktLostAS >= 0.0 ? pcktLostAS : WebRTCConstant.DEFAULT_ADD_VALUE)
								+ (pcktLostAR != null ? pcktLostAR : WebRTCConstant.DEFAULT_ADD_VALUE));
			}
			
		} else {
			setPacketLossData(wrapper, pcktLostAS, pcktLostAR, pcktLostVS, pcktLostVR);
		}
	}

	private static void setPacketLossData(WebRTCCallDataWrapper wrapper, Double pcktLostAS, Double pcktLostAR,
			Double pcktLostVS, Double pcktLostVR) {

		if (pcktLostAS != null || pcktLostAR != null || pcktLostVS != null || pcktLostVR != null) {
			wrapper.setPacketsLost((pcktLostAS != null && pcktLostAS >= 0.0 ? pcktLostAS
					: WebRTCConstant.DEFAULT_ADD_VALUE)
					+ (pcktLostAR != null && pcktLostAR >= 0.0 ? pcktLostAR : WebRTCConstant.DEFAULT_ADD_VALUE)
					+ (pcktLostVS != null && pcktLostVS >= 0.0 ? pcktLostVS : WebRTCConstant.DEFAULT_ADD_VALUE)
					+ (pcktLostVR != null && pcktLostVR >= 0.0 ? pcktLostVR : WebRTCConstant.DEFAULT_ADD_VALUE));
		}
	}

	private static boolean isPositiveNumber(Double value) {
		return value != null && value >= 0.0;
	}
}
