package com.sportmatch.identityservice.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportmatch.identityservice.common.response.ErrorResponse;
import com.sportmatch.identityservice.common.enums.DateStyle;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
public final class CommonFunction {

  private static final String ERROR_FILE = "errors.yml";
  private static final String VALIDATION_FILE = "validations.yml";
  private static final String URL_REGEX =
      "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";

  private CommonFunction() {}

  /**
   * Parse object to json string
   *
   * @param ob {@link Object} to parse
   * @return String
   */
  public static String convertToJSONString(Object ob) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(ob);
    } catch (Exception e) {
      log.error(e.getMessage());
      return null;
    }
  }

  /**
   * Generate a queue name
   *
   * @param userId User's id
   * @return String
   */
  public static String generateQueueName(UUID userId) {
    return generateCode(64) + "|" + userId;
  }

  /**
   * Generate a random code
   *
   * @param length Code length
   * @return String code
   */
  public static String generateCode(int length) {
    List<CharacterRule> rules =
        Arrays.asList(
            new CharacterRule(EnglishCharacterData.UpperCase, 1),
            new CharacterRule(EnglishCharacterData.LowerCase, 1),
            new CharacterRule(EnglishCharacterData.Digit, 1));

    PasswordGenerator generator = new PasswordGenerator();
    return generator.generatePassword(length, rules);
  }

  /**
   * Generate a random code
   *
   * @param length Code length
   * @return String code
   */
  public static String generateCodeDigit(int length) {
    List<CharacterRule> rules = Arrays.asList(new CharacterRule(EnglishCharacterData.Digit, 1));

    PasswordGenerator generator = new PasswordGenerator();
    return generator.generatePassword(length, rules);
  }

  /**
   * Get current date time
   *
   * @return Timestamp
   */
  public static LocalDateTime getCurrentDateTime() {
    Date date = new Date();
    return new Timestamp(date.getTime()).toLocalDateTime();
  }

  /**
   * Extract exception error
   *
   * @param error String error
   * @return ErrorResponse
   */
  @SuppressWarnings("unchecked")
  public static ErrorResponse getExceptionError(String error) {
    ReadYAML readYAML = new ReadYAML();
    Map<String, Object> errors = readYAML.getValueFromYAML(ERROR_FILE);
    Map<String, Object> objError = (Map<String, Object>) errors.get(error);
    String code = (String) objError.get("code");
    String message = (String) objError.get("message");
    return new ErrorResponse(code, message);
  }

  /**
   * Extract validation error
   *
   * @param resource file error
   * @param fieldName field error
   * @param error String error
   * @return ErrorResponse
   */
  @SuppressWarnings("unchecked")
  public static ErrorResponse getValidationError(String resource, String fieldName, String error) {
    if (fieldName.contains("[")) {
      fieldName = handleFieldName(fieldName);
    }

    ReadYAML readYAML = new ReadYAML();
    Map<String, Object> errors = readYAML.getValueFromYAML(VALIDATION_FILE);
    Map<String, Object> fields = (Map<String, Object>) errors.get(resource);
    Map<String, Object> objErrors = (Map<String, Object>) fields.get(fieldName);
    Map<String, Object> objError = (Map<String, Object>) objErrors.get(error);
    String code = (String) objError.get("code");
    String message = (String) objError.get("message");
    return new ErrorResponse(code, message);
  }

  /**
   * Convert time to string
   *
   * @param timestamp {@link Timestamp}
   * @param dateStyle {@link DateStyle} Destination
   * @return Time format
   */
  public static String formatDateToString(Timestamp timestamp, DateStyle dateStyle) {
    SimpleDateFormat dateFormat;
    if (dateStyle == DateStyle.DATE_TIME) {
      dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    } else {
      dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    }
    return dateFormat.format(timestamp);
  }

  /**
   * Convert date into pattern "yyyy-MM-dd"
   *
   * @param inputDate Input date
   * @return Timestamp
   * @throws ParseException {@link ParseException} Error during parser
   */
  public static Timestamp yyyyMMddFormat(String inputDate) throws ParseException {
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(inputDate);
    return new Timestamp(date.getTime());
  }

  /**
   * Convert date into pattern "yyyy-MM-dd"
   *
   * @param inputDate Input date
   * @return Timestamp
   * @throws ParseException {@link ParseException} Error during parser
   */
  public static Timestamp yyyyMMddHHmmSSFormat(String inputDate) throws ParseException {
    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputDate);
    return new Timestamp(date.getTime());
  }

  /**
   * Convert camel case to snake case
   *
   * @param input string type camel case
   * @return String type snake case
   */
  public static String convertToSnakeCase(String input) {
    return input.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
  }

  public static Timestamp getMinTime(int year, int month, int day) throws ParseException {
    if (day == 0) {
      String time = year + "-" + convertDayAndMonthToString(month) + "-01 00:00:00";
      return CommonFunction.yyyyMMddHHmmSSFormat(time);
    }
    String time =
        year
            + "-"
            + convertDayAndMonthToString(month)
            + "-"
            + convertDayAndMonthToString(day)
            + " 00:00:00";
    return CommonFunction.yyyyMMddHHmmSSFormat(time);
  }

  public static Timestamp getMaxTime(int year, int month, int day) throws ParseException {
    if (day == 0) {
      String time =
          year
              + "-"
              + convertDayAndMonthToString(month)
              + "-"
              + convertDayAndMonthToString(getDay(year, month))
              + " 23:59:59";
      return CommonFunction.yyyyMMddHHmmSSFormat(time);
    }

    String time =
        year
            + "-"
            + convertDayAndMonthToString(month)
            + "-"
            + convertDayAndMonthToString(day)
            + " 23:59:59";
    return CommonFunction.yyyyMMddHHmmSSFormat(time);
  }

  public static String convertDayAndMonthToString(int value) {
    if (value < 10) {
      return "0" + value;
    }
    return "" + value;
  }

  public static int getDay(int year, int month) {
    if (month == 4 || month == 6 || month == 9 || month == 11) {
      return 31;
    }

    if (month == 2) {
      if (checkIsLeap(year)) {
        return 29;
      }
      return 28;
    }
    return 31;
  }

  public static boolean checkIsLeap(int year) {
    boolean isLeap = false;
    if (year % 4 == 0) {
      if (year % 100 == 0) {
        if (year % 400 == 0) isLeap = true;
      } else isLeap = true;
    }
    return isLeap;
  }

  public static String handleFieldName(String fieldName) {
    String index = fieldName.substring(fieldName.indexOf("[") + 1, fieldName.indexOf("]"));
    return fieldName.replaceAll(index, "");
  }


  public static Timestamp convertMillisecondToTimeStamp(String date) {
    return new Timestamp(Long.parseLong(date));
  }

  public static boolean isLessThanTwoDays(Timestamp date) {
    Instant currentTime = Instant.now();
    Instant startAt = date.toInstant();
    Duration difference = Duration.between(currentTime, startAt);
    return difference.toDays() < 2;
  }

  public static String handleContentSearch(String content) {
    return content.replaceAll(
            "([\\[\\]\\-\\\\\\~\\`\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\=\\{\\}\\|\\:\\;\\'\\<\\>\\,\\.\\?\\/])",
            "\\\\$1");
  }
}