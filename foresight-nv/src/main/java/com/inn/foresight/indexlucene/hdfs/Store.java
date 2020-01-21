package com.inn.foresight.indexlucene.hdfs;

/**
 * @lucene.experimental
 */
public interface Store {

  byte[] takeBuffer(int bufferSize);

  void putBuffer(byte[] buffer);

}