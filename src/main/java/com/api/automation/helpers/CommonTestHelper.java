package com.api.automation.helpers;

import com.api.automation.enums.FileExtensions;
import com.api.automation.listners.TestNgParameters;
import com.api.automation.utils.PropertiesUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Commons Test Helper Methods
 *
 */
@Slf4j
public class CommonTestHelper {

  public static int randomNumber(int minNumber, int maxNumber) {
    Faker faker = new Faker();
    return faker.number().numberBetween(minNumber, maxNumber);
  }

  public static double randomDoubleNumber(int minNumber, int maxNumber) {
    Faker faker = new Faker();
    return faker.number().randomDouble(2, minNumber, maxNumber);
  }

  public static String randomRegex(String regex) {
    FakeValuesService fakeValuesService =
        new FakeValuesService(new Locale("en-GB"), new RandomService());
    return fakeValuesService.regexify(regex);
  }

  public static String randomBothify(String bothify) {
    FakeValuesService fakeValuesService =
        new FakeValuesService(new Locale("en-GB"), new RandomService());
    return fakeValuesService.bothify(bothify);
  }

  public static String randomMobile() {
    Faker faker = new Faker();
    return faker.numerify("98########");
  }

  public static String randomName() {
    Faker faker = new Faker();
    return faker.address().firstName();
  }

  public static String randomAddress() {
    Faker faker = new Faker();
    return faker.address().streetName();
  }

  public static String randomCity() {
    Faker faker = new Faker();
    return faker.address().cityName();
  }

  public static String randomEmail() {
    Faker faker = new Faker();
    return faker.internet().emailAddress();
  }

  public static String getCurrentTimestamp(String format) {
    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);
    LocalDateTime now = LocalDateTime.now();
    return dtf.format(now);
  }

  /**
   * Gets the secret keys either from environment variables or maven arguments or properties file.
   *
   * @param key Maven argument Key
   * @param fileName Secret Properties fileName present in resources folder with the above key
   * @param envName Environment Variable Key for the Secrets
   * @return Secret Value for the given key
   */
  public static String getSecretValues(String key, String fileName, String envName) {
    String envValue = System.getenv(envName);
    if (envValue != null && !envValue.isBlank()) {
      return envValue;
    }
    String mavenSecretValue = System.getProperty(key);
    if (mavenSecretValue != null && !mavenSecretValue.isBlank()) {
      return mavenSecretValue;
    }
    return PropertiesUtil.getFileProperties(fileName).getProperty(key);
  }

  // getTimeStamp after/before x number of days
  public static String getTimeStamp(String format, int noOfDaysAfter) {
    final SimpleDateFormat dtf = new SimpleDateFormat(format);
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.DAY_OF_YEAR, noOfDaysAfter);
    return dtf.format(calendar.getTime());
  }

  public static Date getTimeStamp(String requestDate, String dateFormat) throws ParseException {
    return new SimpleDateFormat(dateFormat).parse(requestDate);
  }

  public static String getTimeStamp(TemporalAccessor accessor, String dateFormat) {
    return DateTimeFormatter.ofPattern(dateFormat).format(accessor);
  }

  public static LocalDateTime getLocalDateTime(String requestDate, String dateFormat) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    return LocalDateTime.parse(requestDate, dateTimeFormatter);
  }

  public static LocalDateTime getCurrentLocalDateTime(ZoneId zoneId, String dateFormat) {
    ZonedDateTime zoneDateTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
    String dateTime = getTimeStamp(zoneDateTime, dateFormat);
    return getLocalDateTime(dateTime, dateFormat);
  }

  public static boolean validateDateFormat(String date, String dateFormat) {
    if (date == null || date.trim().equals("")) {
      return false;
    }
    SimpleDateFormat format = new SimpleDateFormat(dateFormat);
    try {
      format.parse(date);
      log.info(date + " is valid date format");
    } catch (ParseException e) {
      log.error(date + " is Invalid Date format");
      return false;
    }
    return true;
  }

  public static String getJsonString(String filePath)
      throws IOException, org.json.simple.parser.ParseException {
    JSONParser jsonParser = new JSONParser();
    JSONObject jsonObject;
    InputStream inputStream = CommonTestHelper.class.getResourceAsStream(filePath);
    assert inputStream != null;
    jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(inputStream));
    log.info(jsonObject.toJSONString());
    return jsonObject.toJSONString();
  }

  public static String getJsonString(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return jsonString;
  }

  public static Map<String, Object> getObjectMap(Object obj) {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.convertValue(obj, Map.class);
  }

  public static <T> T getObject(String filePath, Class<T> obj) {
    T object = null;
    try (InputStream inputStream = CommonTestHelper.class.getResourceAsStream(filePath)) {
      ObjectMapper mapper = new ObjectMapper();
      object = mapper.readValue(inputStream, obj);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return object;
  }

  /**
   * Gets the required parameter either from maven, testNg or config.properties file.<br>
   * config.properties should be present in the resources folder.
   *
   * @param testNgParameters Parameters from testNg.xml file
   * @param key Key for which value is required from maven, testNg or .properties file
   * @return String value for the key passed.
   */
  public static String getConfigParameter(
      TestNgParameters testNgParameters, String key, String fileName) {
    String mavenConfigParam = System.getProperty(key);
    if (mavenConfigParam != null && !mavenConfigParam.isBlank()) {
      return mavenConfigParam;
    }
    if (testNgParameters != null) {
      String testNgParams = testNgParameters.getTestNGParamValue(key);
      if (testNgParams != null && !testNgParams.isBlank()) {
        return testNgParams;
      }
    }
    return PropertiesUtil.getFileProperties(fileName).getProperty(key);
  }

  public String readPubKey(String filePath) throws IOException {
    File pubKeyFile = new File(filePath);
    String key = Files.readString(pubKeyFile.toPath(), Charset.defaultCharset());
    return key.replace("-----BEGIN PUBLIC KEY-----", "")
        .replaceAll(System.lineSeparator(), "")
        .replace("-----END PUBLIC KEY-----", "");
  }

  public String getSignature(String clientId, String filePath)
      throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
      InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String clientIdWithEpochTimeStamp = clientId + "." + Instant.now().getEpochSecond();
    byte[] keyBytes = Files.readAllBytes(new File(filePath).toPath());
    String publicKeyContent = new String(keyBytes);
    publicKeyContent =
        publicKeyContent
            .replaceAll("[\\t\\n\\r]", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "");
    KeyFactory kf = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpecX509 =
        new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return Base64.getEncoder()
        .encodeToString(cipher.doFinal(clientIdWithEpochTimeStamp.getBytes()));
  }

  public String getSignatureWithPublicKey(String clientId, String publicKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
      InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    String clientIdWithEpochTimeStamp = clientId + "." + Instant.now().getEpochSecond();
    publicKey =
        publicKey
            .replaceAll("[\\t\\n\\r]", "")
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "");
    KeyFactory kf = KeyFactory.getInstance("RSA");
    X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey));
    RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);
    final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return Base64.getEncoder()
        .encodeToString(cipher.doFinal(clientIdWithEpochTimeStamp.getBytes()));
  }

  public static List<String> getAvailableFileExtensions() {
    return Arrays.stream(FileExtensions.values())
        .map(FileExtensions::getFileExtension)
        .collect(Collectors.toList());
  }

  public static boolean doesPatternMatch(String regexToBeMatched, String stringToBeMatched) {
    Pattern pattern = Pattern.compile(regexToBeMatched, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(stringToBeMatched);
    return matcher.find();
  }
}
