package com.sportmatch.identityservice.common.constant;

public final class MessageConstant {

  public static final String USER_NOT_FOUND = "user_not_found";

  // Page not found
  public static final String PAGE_NOT_FOUND = "page_not_found";
  // Forbidden error
  public static final String FORBIDDEN_ERROR = "forbidden_error";
  // Unauthorized
  public static final String UNAUTHORIZED = "unauthorized";

  // Internal server error
  public static final String INTERNAL_SERVER_ERROR = "internal_server_error";

  // User
  public static final String CHANGE_CREDENTIAL_FAIL = "change_password_failed";
  public static final String RESET_CREDENTIAL_FAIL = "reset_password_failed";
  public static final String INCORRECT_EMAIL_OR_PASSWORD = "incorrect_email_or_password";
  public static final String ACCOUNT_NOT_EXISTS = "account_not_exists";
  public static final String ACCOUNT_BLOCKED = "account_blocked";
  public static final String ACCOUNT_NOT_ACTIVATED = "account_not_activated";
  public static final String USER_IS_ENABLED = "user_is_enabled";
  public static final String INCORRECT_CONFIRMATION_TOKEN = "incorrect_confirmation_token";
  public static final String CHANGE_CREDENTIAL_NOT_CORRECT = "old_password_not_correct";
  public static final String REGISTER_EMAIL_ALREADY_IN_USE = "email_already_in_use";
  public static final String PASSWORD_FIELDS_MUST_MATCH = "password_fields_must_match";
  public static final String NEW_PASSWORD_NOT_SAME_OLD_PASSWORD =
      "new_password_not_same_old_password";
  public static final String TOKEN_REMOVE_ACCOUNT_NOT_CORRECT = "token_remove_account_not_correct";
  public static final String TOKEN_REMOVE_ACCOUNT_EXPIRED = "token_remove_account_expired";
  public static final String MAXIMUM_UPLOAD_SIZE_EXCEEDED = "maximum_upload_size_exceeded";

  // Authentication
  public static final String INVALID_TOKEN = "invalid_token";
  public static final String EXPIRED_TOKEN = "expired_token";
  public static final String REVOKED_TOKEN = "revoked_token";
  public static final String INVALID_REFRESH_TOKEN = "invalid_refresh_token";
  public static final String EXPIRED_REFRESH_TOKEN = "expired_refresh_token";

  public static final String USER_INFO_ALREADY_EXISTS = "user_info_already_exists";

  public static final String USER_INFO_NOT_FOUND = "user_info_not_found";



  //common
  public static final String NOT_FOUND = "not_found";

  public static final String BAD_REQUEST = "bad_request";


  private MessageConstant() {}
}