package com.inn.foresight.module.nv.app.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ConfigEnum;

/**
 * The Class AppAuthenticationFilter.
 *
 * @author innoeye
 * date - 30-Jan-2018 4:22:28 PM
 */


@WebFilter(filterName = "appAuthFilter", urlPatterns = { "/rest/NVConsumer/*","/rest/BBM/*","/rest/CustomNVConsumer/*" })
public class AppAuthenticationFilter implements Filter {
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(AppAuthenticationFilter.class);

	
	/**
	 * Called by Web Container at the time of instantiating the filter.
	 *
	 * @param filterConfig the filterConfig
	 * @throws ServletException the servlet exception
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {	
		logger.info("App Authentication Filter intialized");
	}

	/**
	 * Do filter.
	 * 
	 * @param request the request
	 * @param response the response
	 * @param chain the chain
	 * @throws ServletException the servlet exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("Inside doFilter method");

		String xApiKey = ConfigUtils.getString(ConfigEnum.NV_APP_X_API_KEY);
		String xApiKeyValue = ConfigUtils.getString(ConfigEnum.NV_APP_X_API_KEY_VALUE);

		logger.info("xApiKey : {} xApiKeyValue : {}", xApiKey, xApiKeyValue);

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String reqXApiKeyValue = httpRequest.getHeader(xApiKey);

		logger.info("Header values xApiKey = {} ", reqXApiKeyValue);
		if (xApiKeyValue.equalsIgnoreCase(reqXApiKeyValue)) {
			logger.info("Success");
			chain.doFilter(request, response);
			logger.info("after filter");
		} else {
			logger.info("unauthrized access");
			HttpServletResponse res = (HttpServletResponse) response;
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			throw new ServletException("unauthorized access");
		}
	}

	/** Destroy. */
	@Override
	public void destroy() {	
		logger.info("App Authentication Filter destroyed");
	}

}
