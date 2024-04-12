package com.e2s.cam.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class NonceValidator {
	@Autowired
	private RedisService redisService;
    int secondsIn3Days = 3 * 24 * 60 * 60;

    public Boolean checkNonce(String nonce) {
        // Check if the nonce is a positive 32-bit integer number
        try {
            int nonceValue = Integer.parseInt(nonce);
            if (nonceValue <= 0) {
            	System.out.println("jjjj");
                return false;
            }
        } catch (NumberFormatException e) {
        	System.out.println("jjjj1");

            return false;
        }
//      String isNonExist=redisService.getData(nonce);
//      System.out.println("isNonExist"+isNonExist );
//      if(!Objects.equals(isNonExist, null)) {
//    	  return false;
//      }else {
//    	  redisService.setValue(nonce, nonce,secondsIn3Days ); 
//      }
//       
        return true;
    }

  
}