/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.inn.foresight.indexlucene.hdfs;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HdfsLocalityReporter {
  public static final String LOCALITY_BYTES_TOTAL = "locality.bytes.total";
  public static final String LOCALITY_BYTES_LOCAL = "locality.bytes.local";
  public static final String LOCALITY_BYTES_RATIO = "locality.bytes.ratio";
  public static final String LOCALITY_BLOCKS_TOTAL = "locality.blocks.total";
  public static final String LOCALITY_BLOCKS_LOCAL = "locality.blocks.local";
  public static final String LOCALITY_BLOCKS_RATIO = "locality.blocks.ratio";

  private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

  private final ConcurrentMap<HdfsDirectory,ConcurrentMap<FileStatus,BlockLocation[]>> cache;


  public HdfsLocalityReporter() {
    cache = new ConcurrentHashMap<>();
  }

  

  public void registerDirectory(HdfsDirectory dir) {
    logger.info("Registering direcotry {} for locality metrics.", dir.getHdfsDirPath().toString());
    cache.put(dir, new ConcurrentHashMap<FileStatus, BlockLocation[]>());
  }

 

}
