package com.e2s.cam.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.e2s.cam.Error.ErrorResponses;
import com.e2s.cam.Error.UnAuthorizedException;
import com.e2s.cam.entity.TokenDetails;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Component
public class AuthenticationService {
	private static final Logger logger = LogManager.getLogger(AuthenticationService.class);


	private static final String SECRET_KEY = "123456";
	String accessID="24km8bjqinulx6av";
	
	static String secretKey="8TQLUU7QUKIDYHDM";
	
	@Value("${SCIM.API.RequestExpiry}")	// in seconds
	long requestExpiryGracePeriod;	
	@Autowired
	NonceValidator nonceValidator;
	private static final int REQUEST_TIMESTAMP_GRACE_PERIOD_IN_MINUTES = 3; // Adjust as per your requirements

	public String generateToken(HttpServletRequest request,TokenDetails tokenDetails) {
		// Example usage
		String httpMethod = tokenDetails.getHttpMethod();
		System.out.println("httpMethod " + httpMethod);

		String url =tokenDetails.getUrl();
		System.out.println("httpMethod " + url);

		Map<String, String> params = new TreeMap<>();
		params=tokenDetails.getParams();
//		params.put("nonce", "2147483647");
//		params.put("ts", "1689507657901");
//		params.put("accountid", "123456");
		System.out.println("params " + params);

		String authToken = generateAuthToken(tokenDetails.getHostName(),httpMethod, url, params,request);

		System.out.println("Generated Auth Token: " + authToken);
		return authToken;
	}

	public static String generateAuthToken(String hostName1,String httpMethod1, String url, Map<String, String> params,HttpServletRequest request) {
		
		String httpMethod = request.getMethod().toUpperCase();
//		String protocol = request.getScheme().toLowerCase();
		String hostName = hostName1;
//		String hostPort = request.getServerPort() + "";
		String protocol = "https";
		String hostPort =  "443";
//		hostName = hostPort == "80" ? hostName : String.format("%s:%s", hostName, hostPort); 
		String requestURI = url.toLowerCase();
		AuthenticationService authenticationService=new AuthenticationService();
		String parameters = authenticationService.constructAndSortParam(params);
		logger.info("test logger");
		logger.info("httpMethod = " + httpMethod);
		logger.info("protocol = " + protocol);
		logger.info("hostName = " + hostName);
		logger.info("hostPort = " + hostPort);
		logger.info("requestURI = " + requestURI);
		logger.info("parameters = " + parameters);

		
		String combinedString = String.format("%s&%s://%s%s?%s", httpMethod, protocol,hostName,requestURI,parameters);
		System.out.print("combinedString" +combinedString);
		try {
			SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HMACSHA256");
			
			Mac mac = Mac.getInstance("HMACSHA256");
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(combinedString.getBytes());
			String authString = new String(Base64.getEncoder().encodeToString(rawHmac));
			return authString;	
		} catch (Exception ex) {
			ex.printStackTrace();
			}
		return null;	

	}

	private String constructAndSortParams(Map<String, String> params) {
		String[] sortedKeys = params.keySet().stream().sorted().toArray(String[]::new);

		StringBuilder sortedParams = new StringBuilder();
		for (String key : sortedKeys) {
			sortedParams.append(key.toLowerCase()).append("=").append(params.get(key)).append("&");
		}

		sortedParams.deleteCharAt(sortedParams.length() - 1);

		return sortedParams.toString();
	}

	public ErrorResponses checkAccountAndAuthorizationHeader(HttpServletRequest request, String authorizationHeader,
			Map<String, String> requestParams) {
		System.out.println("inside validation ");
		String accountId = requestParams.get("accountid");
		System.out.println("test 0000000001");
		if (!accountId.equalsIgnoreCase("123456")) {
			throw new UnAuthorizedException("Invalid account");
		}

		String httpMethod = request.getMethod().toUpperCase();
		String protocol = request.getScheme().toLowerCase();
		String hostName = request.getServerName().toLowerCase();
		String hostPort = request.getServerPort() + "";
		hostName = hostPort == "80" ? hostName : String.format("%s:%s", hostName, hostPort);
		String requestURI = request.getRequestURI().toLowerCase();
		String sortedParams = constructAndSortParams(requestParams);
		String combinedString = String.format("%s&%s://%s%s?%s", httpMethod, protocol, hostName, requestURI,
				sortedParams);
		System.out.println(combinedString);

		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(secretKeySpec);
			byte[] rawHmac = mac.doFinal(combinedString.getBytes(StandardCharsets.UTF_8));

			String calculatedSignature = Base64.getEncoder().encodeToString(rawHmac);

			String signedAuthToken = authorizationHeader.replace("Bearer ", "");

			if (!calculatedSignature.equals(signedAuthToken)) {
				return new ErrorResponses(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
			}

			return null;
		} catch (Exception ex) {
			// Handle exceptions and log them appropriately
			return new ErrorResponses(HttpStatus.UNAUTHORIZED, "Invalid authorization header");
		}
	}

	private ErrorResponses checkNonce(Map<String, String> reqParams) {
		System.out.println("inside nin");
		String nonce = reqParams.get("nonce");
		System.out.println("nnn " + nonce);
		if (nonceValidator.checkNonce(nonce)) {
			System.out.println("non valid");
			return null;
		}
		return new ErrorResponses(HttpStatus.UNAUTHORIZED, "Invalid nonce");
	}

	private ErrorResponses checkTimestamp(Map<String, String> reqParams) {
		try {
			String requestTimestamp = reqParams.get("ts");
			if (!requestTimestamp.isEmpty()) {
				long requestTimestampLong = Long.parseLong(requestTimestamp);
				long currentTimestamp = getCurrentMillis();
				System.out.println("currentTimestamp " + currentTimestamp);
				System.out.println("requestTimestampLong " + requestTimestampLong);
				System.out.println("afterminus " + (currentTimestamp - requestTimestampLong));
				System.out.println("time period " + REQUEST_TIMESTAMP_GRACE_PERIOD_IN_MINUTES * 1000);

				if ((currentTimestamp - requestTimestampLong) <= REQUEST_TIMESTAMP_GRACE_PERIOD_IN_MINUTES * 1000) {
					return null;
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in ti me stamp");
		}
		System.out.println("time stamp fail");
		return new ErrorResponses(HttpStatus.UNAUTHORIZED, "Invalid request date time");
	}

	public Object validateActionRequest(final ProceedingJoinPoint joinPoint) throws Throwable {
		Object joinPointResult;
		System.out.print("is execute");
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String authorizationHeader = request.getHeader("Authorization");
		Map<String, String> reqParams = new HashMap<>();
		Map<String, String[]> parameterMap = request.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String key = entry.getKey();
			String[] values = entry.getValue();
			if (values != null && values.length > 0) {
				reqParams.put(key, values[0]);
			}
		}
		ErrorResponses e = null;
		e = checkNonce(reqParams);
		if (e == null) {
			e = checkTimestamp(reqParams);
			System.out.println("time stamp " + e);
		}
		if (e == null) {
			e = checkAccountAndAuthorizationHeader(request, authorizationHeader, reqParams);
		}
		if (e != null) {
			return e;
		} else {
			joinPointResult = joinPoint.proceed();
			return joinPointResult;
		}
	}
	 public long getCurrentMillis() {
	        Instant instant = Instant.now();
	        return instant.toEpochMilli();
	    }
	 private String constructAndSortParam(Map<String, String> reqParams) {
			List<String> reqParamsList = new ArrayList<String>();
			
			reqParams.forEach((x,y) -> reqParamsList.add(String.format("%s=%s", x.toLowerCase(), y.toLowerCase())));
			Collections.sort(reqParamsList);
			StringBuilder sb = reqParamsList.parallelStream().collect(StringBuilder::new, (x, y) -> x.append(y),
					(a, b) -> a.append("&").append(b));
			return sb.toString();
		}
	 
}
