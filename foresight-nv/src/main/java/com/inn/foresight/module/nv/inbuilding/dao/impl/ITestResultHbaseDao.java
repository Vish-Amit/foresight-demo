package com.inn.foresight.module.nv.inbuilding.dao.impl;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;

public interface ITestResultHbaseDao {
	String insertIBTestResultToHbase(Put put) throws IOException;

}
