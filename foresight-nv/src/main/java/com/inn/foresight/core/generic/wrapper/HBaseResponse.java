package com.inn.foresight.core.generic.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class HBaseResponse {

	private List<com.inn.commons.hadoop.hbase.rest.Row> Row;
	
	public List<com.inn.commons.hadoop.hbase.rest.Row> getRow() {
		return Row;
	}

	public void setRow(List<com.inn.commons.hadoop.hbase.rest.Row> row) {
		Row = row;
	}

	@Override
	public String toString() {
	
		return "HbaseResponse [row=" + Row + " ]";
	}
}
