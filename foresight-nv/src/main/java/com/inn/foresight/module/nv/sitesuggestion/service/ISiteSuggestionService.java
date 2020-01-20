package com.inn.foresight.module.nv.sitesuggestion.service;

import java.io.IOException;
import java.io.InputStream;

import com.inn.foresight.module.nv.sitesuggestion.model.FriendlySiteSuggestion;

public interface ISiteSuggestionService {


	FriendlySiteSuggestion persistFriendlySiteSuggestion(String json, InputStream inputFile, String fileName) throws IOException;

	String persistFileToHDFS(InputStream fileStream, String fileName, FriendlySiteSuggestion friendlySiteSuggestion)
			throws IOException;

	String getSiteSuggestionData(int decryZoom, double decodeNELat, double decodeNELng, double decodeSWLat,
			double decodeSWLng);

	Object getSiteAcquisitionLayerData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng,
			String fromDate, String toDate, String buildingType, String siteType, Integer displayZoomLevel);

}
