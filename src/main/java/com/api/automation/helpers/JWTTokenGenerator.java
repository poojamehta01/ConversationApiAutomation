package com.api.automation.helpers;


import static com.api.automation.secrets.secrets.API_KEY;
import static com.api.automation.secrets.secrets.API_SECRET;
import static com.api.automation.secrets.secrets.APP_ID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

  public class JWTTokenGenerator {

    public static void main(String[] args) {
      String apiKey = API_KEY;
      String apiSecret = API_SECRET;
      String appId = APP_ID;

      String jwtToken = generateJWT();

      System.out.println("JWT Token: " + jwtToken);
    }

    public static String generateJWT() {
      long nowMillis = System.currentTimeMillis();
      Date now = new Date(nowMillis);

      long expMillis = nowMillis + 3600000; // Token expires in 1 hour
      Date exp = new Date(expMillis);

      String jwtToken = Jwts.builder()
          .setIssuedAt(now)
          .setExpiration(exp)
          .setIssuer(API_KEY)
          .claim("application_id", APP_ID)
          .signWith(SignatureAlgorithm.HS256, API_SECRET)
          .compact();

      return jwtToken;
    }
  }


