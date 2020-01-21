package com.inn.foresight.core.livy.utils;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;

/**
 * The Class CuratorClient.
 * 
 *  @author Zafar
 */
public class CuratorClient {
	
	/** The zk address. */
	private String zkAddress;
	
	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(CuratorClient.class);

	
	
	/**
	 * Instantiates a new curator client.
	 *
	 * @param zkAddress the zk address
	 */
	public CuratorClient(String zkAddress) {
		super();
		this.zkAddress = zkAddress;
		
	}

	/**
	 * Gets the child nodes.
	 *
	 * @param parentPath the parent path
	 * @return the child nodes
	 */
	public List<String> getChildNodes(String parentPath) {
		CuratorFramework zkClient = CuratorFrameworkFactory.builder().connectString(zkAddress).retryPolicy(new RetryNTimes(5, 1000)).build();
		zkClient.start();
		List<String> servers = null;
		try {
			servers = zkClient.getChildren().forPath(parentPath);
		} catch (Exception e) {
			logger.error("Getting Child Nodes by CuratorFramework Error Msg :{}",Utils.getStackTrace(e));
		}
		logger.info("List Of Livy servers:: {}",servers);
		zkClient.close();
		return servers;
	}
}