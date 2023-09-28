package com.e2s.cam.module;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/***
 * This implementation is only meant for development /testing and should not be used in production
 */

@Component
public class NonceValidatorInMemoryImpl implements INonceValidator {

	@Value("#{new Boolean('${Nonce.UniquenessCheck}')}")
	boolean uniquenessCheck; //Optional
	
	private static Map<String , LocalDateTime> nonceMap = new HashMap<String , LocalDateTime>();
	
	@Override
	public boolean checkNonce(String nonce) {
		boolean result = isUnsignedInt32(nonce);
		
		if (result && uniquenessCheck) {
			if (!nonceMap.containsKey(nonce)) {
				nonceMap.put(nonce, LocalDateTime.now());
			} else {
				result = false;
			}
		}
		
		return result;
	}
	
	private boolean isUnsignedInt32( String input ) {
	    try {
	    	//Max = 4294967295 [null, empty string, negative number and value > Max will failed the check and return false]
	        Integer.parseUnsignedInt( input ); 
	        return true; 
	    } 
	    catch( Exception e ) {
	        return false;
	    } 
	}
}
