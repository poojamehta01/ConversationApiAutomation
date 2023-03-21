package com.api.automation.helpers;

import static com.api.automation.secrets.secrets.API_KEY;
import static com.api.automation.secrets.secrets.API_SECRET;
import static com.api.automation.secrets.secrets.APP_ID;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.SecretKey;

public class JWTTokenGenerator {

  public static String generateJwtToken() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
    // Set the API key and secret

    String apiKey = API_KEY;
    String apiSecret = API_SECRET;

    // Set the endpoint URL and method
    String endpoint = "https://api.nexmo.com/v0.1/users";
    String method = "GET";

    // Set the JWT claims
    long nowMillis = System.currentTimeMillis();
    long expMillis = nowMillis + 3600000;
    Date exp = new Date(expMillis);


    // Build the JWT token
    byte[] keyBytes = apiSecret.getBytes();
    if (keyBytes.length < 256 / 8) {
      keyBytes = Arrays.copyOf(keyBytes, 256 / 8);
    }
    SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    String jwtToken = Jwts.builder()
        .setHeaderParam("alg", "HS256")
        .setHeaderParam("typ", "JWT")
        .setIssuer(apiKey)
        .setIssuedAt(new Date())
        .setExpiration(exp)
        .claim("application_id", "<your_application_id>".replace(
                "<your_application_id>", APP_ID))
        .signWith(key, SignatureAlgorithm.HS256)
        .compact();


    // Make the API request using the JWT token
    Response response = RestAssured.given()
        .header("Authorization", "Bearer " + jwtToken)
        .header("Content-Type", "application/json")
        .request(method, endpoint);

    // Print the response status code and body
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody().asString());
    return jwtToken;
  }
}
