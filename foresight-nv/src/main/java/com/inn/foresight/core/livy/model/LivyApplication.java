
package com.inn.foresight.core.livy.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class LivyApplication.
 * 
 * @author Zafar
 */
@NamedQueries({
		@NamedQuery(name = "getLivyApplicationByName", query = "select l from LivyApplication l where name=:name"),
		@NamedQuery(name = "getApplicationNameByModule", query = "select name from LivyApplication ")
})

@FilterDefs({
		@FilterDef(name = "technologyFilterForLivy", parameters = {@ParamDef(name = "technology", type = "java.lang.String") }),
		@FilterDef(name = "domainFilterForLivy", parameters = {@ParamDef(name = "domain", type = "java.lang.String") }),
		@FilterDef(name = "vendorFilterForLivy", parameters = {@ParamDef(name = "vendor", type = "java.lang.String") }),
		@FilterDef(name = "moduleFilterForLivy", parameters = {@ParamDef(name = "module", type = "java.lang.String") }),})

@Filters({ @Filter(name = "technologyFilterForLivy", condition = "technology=:technology"),
		@Filter(name = "domainFilterForLivy", condition = "domain=:domain"),
		@Filter(name = "vendorFilterForLivy", condition = "vendor=:vendor"), 
		@Filter(name = "moduleFilterForLivy", condition = "module=:module"), })


@Entity
@Table(name = "LivyApplication")
@XmlRootElement(name = "LivyApplication")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class LivyApplication implements Serializable {

	/** The logger. */
	public static final Logger logger = LogManager.getLogger(LivyApplication.class);
	
	/** The object mapper. */
	public static Gson objectMapper = new Gson();

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4084875109500857320L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "livyapplicationid_pk")
	private Long id;

	/** The name. */
	@Basic
	@Column(name = "name")
	private String name;

	/** The driver memory. */
	@Basic
	@Column(name = "drivememory")
	private String driverMemory;

	/** The executor memory. */
	@Basic
	@Column(name = "executormemory")
	private String executorMemory;

	/** The num executor. */
	@Basic
	@Column(name = "executor")
	private Integer numExecutor;

	/** The queue. */
	@Basic
	@Column(name = "queue")
	private String queue;

	/** The executor core. */
	@Basic
	@Column(name = "executorcore")
	private Integer executorCore;

	/** The session id. */
	@Basic
	@Column(name = "sessionid")
	private Integer sessionId;

	/** The code template. */
	@Basic
	@Column(name = "codetemplate")
	private String codeTemplate;

	/** The properties. */
	@Basic
	@Column(name = "property")
	private String properties;

	/** The sessioninfo. */
	@Basic
	@Column(name = "sessioninfo")
	private String sessioninfo;

	/** The server session map. */
	@Transient
	private Map<String, Integer> serverSessionMap = new HashMap<>();

	/** The basic template. */
	@Basic
	@Column(name = "basictemplate")
	private String basicTemplate;
	
	/** The domain. */
	@Basic
	@Column(name = "domain")
	@Enumerated(EnumType.STRING)
	private Domain domain;
	
	/** The technology. */
	@Basic
	@Column(name = "technology")
	@Enumerated(EnumType.STRING)
	private Technology technology;

	/** The vendor. */
	@Basic
	@Column(name = "vendor")
	@Enumerated(EnumType.STRING)
	private Vendor vendor;
	
	/** The module. */
	@Basic
	@Column(name = "module")
	private String module;
	
	/**
	 * Instantiates a new livy application.
	 *
	 * @param id the id
	 * @param name the name
	 * @param driverMemory the driver memory
	 * @param executorMemory the executor memory
	 * @param numExecutor the num executor
	 * @param queue the queue
	 * @param executorCore the executor core
	 * @param sessionId the session id
	 * @param codeTemplate the code template
	 * @param properties the properties
	 * @param sessioninfo the sessioninfo
	 */
	public LivyApplication(Long id, String name, String driverMemory, String executorMemory, Integer numExecutor,
			String queue, Integer executorCore, Integer sessionId, String codeTemplate, String properties,
			String sessioninfo) {
		super();
		this.id = id;
		this.name = name;
		this.driverMemory = driverMemory;
		this.executorMemory = executorMemory;
		this.numExecutor = numExecutor;
		this.queue = queue;
		this.executorCore = executorCore;
		this.sessionId = sessionId;
		this.codeTemplate = codeTemplate;
		this.properties = properties;
		this.sessioninfo = sessioninfo;

	}

	/**
	 * Instantiates a new livy application.
	 */
	public LivyApplication() {
		super();
	}

	
	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 *
	 * @param module the new module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * Gets the session id.
	 *
	 * @return the session id
	 */
	public Integer getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session id.
	 *
	 * @param sessionId the new session id
	 */
	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the driver memory.
	 *
	 * @return the driver memory
	 */
	public String getDriverMemory() {
		return driverMemory;
	}

	/**
	 * Sets the driver memory.
	 *
	 * @param driverMemory the new driver memory
	 */
	public void setDriverMemory(String driverMemory) {
		this.driverMemory = driverMemory;
	}

	/**
	 * Gets the executor memory.
	 *
	 * @return the executor memory
	 */
	public String getExecutorMemory() {
		return executorMemory;
	}

	/**
	 * Sets the executor memory.
	 *
	 * @param executorMemory the new executor memory
	 */
	public void setExecutorMemory(String executorMemory) {
		this.executorMemory = executorMemory;
	}

	/**
	 * Gets the num executor.
	 *
	 * @return the num executor
	 */
	public Integer getNumExecutor() {
		return numExecutor;
	}

	/**
	 * Sets the num executor.
	 *
	 * @param numExecutor the new num executor
	 */
	public void setNumExecutor(Integer numExecutor) {
		this.numExecutor = numExecutor;
	}

	/**
	 * Gets the queue.
	 *
	 * @return the queue
	 */
	public String getQueue() {
		return queue;
	}

	/**
	 * Sets the queue.
	 *
	 * @param queue the new queue
	 */
	public void setQueue(String queue) {
		this.queue = queue;
	}

	/**
	 * Gets the executor core.
	 *
	 * @return the executor core
	 */
	public Integer getExecutorCore() {
		return executorCore;
	}

	/**
	 * Sets the executor core.
	 *
	 * @param executorCore the new executor core
	 */
	public void setExecutorCore(Integer executorCore) {
		this.executorCore = executorCore;
	}

	/**
	 * Gets the code template.
	 *
	 * @return the codeTemplate
	 */
	public String getCodeTemplate() {
		return codeTemplate;
	}

	/**
	 * Sets the code template.
	 *
	 * @param codeTemplate            the codeTemplate to set
	 */
	public void setCodeTemplate(String codeTemplate) {
		this.codeTemplate = codeTemplate;
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public String getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties the new properties
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}

	/**
	 * Gets the sessioninfo.
	 *
	 * @return the sessioninfo
	 */
	public String getSessioninfo() {
		return sessioninfo;
	}

	/**
	 * Sets the sessioninfo.
	 *
	 * @param sessioninfo the new sessioninfo
	 */
	public void setSessioninfo(String sessioninfo) {
		this.sessioninfo = sessioninfo;
	}

	/**
	 * Gets the basic template.
	 *
	 * @return the basic template
	 */
	public String getBasicTemplate() {
		return basicTemplate;
	}

	/**
	 * Sets the basic template.
	 *
	 * @param basicTemplate the new basic template
	 */
	public void setBasicTemplate(String basicTemplate) {
		this.basicTemplate = basicTemplate;
	}

	/**
	 * Sets the server session map.
	 *
	 * @param serverSessionMap the server session map
	 */
	public void setServerSessionMap(Map<String, Integer> serverSessionMap) {
		this.serverSessionMap = serverSessionMap;
	}
	

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public Technology getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the property map.
	 *
	 * @return the property map
	 */
	public Map<String, String> getPropertyMap() {
		try {
			logger.debug("properties--------_{}", properties);
			return objectMapper.fromJson(properties, new TypeToken<HashMap<String, String>>() {}.getType());
		} catch (Exception e) {
			logger.error("Exception on getting Property Map , Error Msg :{}",ExceptionUtils.getStackTrace(e));
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Sets the server session map.
	 *
	 * @return the map
	 */
	public Map<String, Integer> setServerSessionMap() {
		try {
			if (this.sessioninfo != null)
				this.serverSessionMap = objectMapper.fromJson(this.sessioninfo,	new TypeToken<HashMap<String, Integer>>() {}.getType());
			else
				this.serverSessionMap = new HashMap<>();
		} catch (Exception e) {
			logger.error("Exception on Setting Server Session on Map, Error Msg :{}",ExceptionUtils.getStackTrace(e));
			this.serverSessionMap = new HashMap<>();
		}
		logger.info("Livy server session Map => {}", serverSessionMap);
		return this.serverSessionMap;
	}

	/**
	 * Gets the server session map.
	 *
	 * @return the server session map
	 */
	public Map<String, Integer> getServerSessionMap() {
		return serverSessionMap;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LivyApplication [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", driverMemory=");
		builder.append(driverMemory);
		builder.append(", executorMemory=");
		builder.append(executorMemory);
		builder.append(", numExecutor=");
		builder.append(numExecutor);
		builder.append(", queue=");
		builder.append(queue);
		builder.append(", executorCore=");
		builder.append(executorCore);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append(", codeTemplate=");
		builder.append(codeTemplate);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", sessioninfo=");
		builder.append(sessioninfo);
		builder.append(", serverSessionMap=");
		builder.append(serverSessionMap);
		builder.append(", basicTemplate=");
		builder.append(basicTemplate);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", technology=");
		builder.append(technology);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", module=");
		builder.append(module);
		builder.append("]");
		return builder.toString();
	}
}