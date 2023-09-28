package com.e2s.cam.module;

import java.lang.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Aspect
@Component
public class SCIMAuthorizationAspect {
	
	private static final Logger logger = LogManager.getLogger(SCIMAuthorizationAspect.class);

	@Value("${SCIM.API.AccountID}")
	String accessID;
	
	@Value("${SCIM.API.SecretKey}")	
	String secretKey;
	
	@Value("${SCIM.API.RequestExpiry}")	// in seconds
	long requestExpiryGracePeriod;	
	
	@Autowired
	private INonceValidator nonceValidator;
	
	@Around("@annotation(com.e2s.cam.module.SCIMAuthorization)")
    public Object validateActionRequest(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object joinPointResult;
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		
		Map<String, String> reqParams = convertMap(request.getParameterMap());
		
		ErrorResponse e = checkNonce(reqParams);
		if (e == null) { e = checkTimeStamp(reqParams); }
		if (e == null) { e = checkAccountAndAuthorizationHeader(request, reqParams); }
		
		if (e != null) 
		{		
			logger.debug(e.getMessage());
			return new ResponseEntity<ErrorResponseMessage>(e.getMessage(), e.getHttpStatus());
		} 
		else 
		{
			joinPointResult = joinPoint.proceed();
			return joinPointResult;
		}
	}
	
	private ErrorResponse checkNonce(Map<String, String> reqParams) {
		String nonce = reqParams.getOrDefault("nonce", "");
		if (nonceValidator.checkNonce(nonce)) {
			return null;
		}
		return new ErrorResponse(HttpStatus.UNAUTHORIZED, new ErrorResponseMessage("Unauthorised Request: Invalid nonce", HttpStatus.UNAUTHORIZED+"")); 		
	}
	
	private ErrorResponse checkTimeStamp(Map<String, String> reqParams) {
		try {
			String reqdtq = reqParams.getOrDefault("ts", "").toString();
			if (!reqdtq.equals("") ) { 
				long reqdtl = Long.parseLong(reqdtq);
				long currentMillisecond = System.currentTimeMillis();
				if (currentMillisecond - reqdtl <= (requestExpiryGracePeriod * 1000)) {
					return null;
				}
			}		
		} catch (Exception e) {
			logger.debug(e.getMessage());
			// TODO: Log exception here
		}
		return new ErrorResponse(HttpStatus.UNAUTHORIZED, new ErrorResponseMessage("Unauthorised Request: Invalid request date time", HttpStatus.UNAUTHORIZED +""));		
	}
	
	private ErrorResponse checkAccountAndAuthorizationHeader(HttpServletRequest request, Map<String, String> reqParams)  {
		
		String acctIDq = reqParams.get("accountid");
		if (!accessID.equals(acctIDq))
        {
			return new ErrorResponse(HttpStatus.UNAUTHORIZED, new ErrorResponseMessage("Unauthorised Request: Invalid account ID", HttpStatus.UNAUTHORIZED + ""));				
        }
		
		String httpMethod = request.getMethod().toUpperCase();
		String protocol = request.getScheme().toLowerCase();
		String hostName = request.getServerName().toLowerCase();
		String hostPort = request.getServerPort() + "";
		hostName = hostPort == "80" ? hostName : String.format("%s:%s", hostName, hostPort); 
		String requestURI = request.getRequestURI().toLowerCase();
		String parameters = constructAndSortParam(reqParams);
		logger.info("httpMethod = " + httpMethod);
		logger.info("protocol = " + protocol);
		logger.info("hostName = " + hostName);
		logger.info("hostPort = " + hostPort);
		logger.info("requestURI = " + requestURI);
		logger.info("parameters = " + parameters);

		
		String combinedString = String.format("%s&%s://%s%s?%s", httpMethod, protocol,hostName,requestURI,parameters);
		logger.info("combinedString" +combinedString);
		System.out.println("combinedString" +combinedString);
		logger.debug("combinedString = " + combinedString);
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HMACSHA256");
			
			Mac mac = Mac.getInstance("HMACSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(combinedString.getBytes());
			String authString = new String(Base64.getEncoder().encodeToString(rawHmac));
			logger.debug("authString = " + authString);
			String authHeader = getAuthorizationHeader(request);
			logger.debug("authHeader = " + authHeader);
			if (!authHeader.equals(authString))
            {
				return new ErrorResponse(HttpStatus.UNAUTHORIZED, new ErrorResponseMessage("Unauthorised Request","Invalid authorization header"));				
            }
			return null;	
		} catch (Exception ex) {
			//TODO Log exception here
			return new ErrorResponse(HttpStatus.UNAUTHORIZED, new ErrorResponseMessage("Unauthorised Request","Invalid authorization header"));
		}
	}
	
	private String constructAndSortParam(Map<String, String> reqParams) {
		List<String> reqParamsList = new ArrayList<String>();
		
		reqParams.forEach((x,y) -> reqParamsList.add(String.format("%s=%s", x.toLowerCase(), y.toLowerCase())));
		Collections.sort(reqParamsList);
		StringBuilder sb = reqParamsList.parallelStream().collect(StringBuilder::new, (x, y) -> x.append(y),
				(a, b) -> a.append("&").append(b));
		return sb.toString();
	}
	
	private String getAuthorizationHeader(HttpServletRequest request) {
		String authHeader = request.getHeader("authorization");
		if (!"".equals(authHeader)) 
		{
			return authHeader.replaceAll("Bearer", "").trim();
		}
		return "";
	}	
	
	private Map<String, String> convertMap(Map<String, String[]> map) {
		HashMap<String, String> m = new HashMap<String, String>();
		map.forEach((x,y) -> m.put(x, String.join(" ", y))); 
		return m;
	}
}
