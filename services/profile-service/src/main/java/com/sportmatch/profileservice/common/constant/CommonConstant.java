package com.sportmatch.profileservice.common.constant;

import java.util.UUID;

public final class CommonConstant {

  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  public static final UUID UNKNOWN = UUID.fromString("00000000-0000-0000-0000-000000000000");
  public static final String ROLE_PREFIX = "ROLE_";
  public static final String RULE_PASSWORD =
      "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,32})"; // (?=.*[@#$%])
  public static final String RULE_ROLE = "ADMIN|INSTRUCTOR|LEARNER";
  public static final String RULE_EMAIL =
      "^(?=.{1,64}@)[\\p{L}0-9_-]+(\\.[\\p{L}0-9_-]+)*@"
          + "[^-][\\p{L}0-9-]+(\\.[\\p{L}0-9-]+)*(\\.[\\p{L}]{2,})$";
  public static final UUID SYSTEM_ID = new UUID(0, 0);

  public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
  public static final String NOTIFICATION_QUEUE = "notifications.queue";
  public static final String ADMIN_NOTIFICATION_EXCHANGE = "admin.notifications.exchange";
  public static final String ADMIN_NOTIFICATION_QUEUE = "admin.notifications.queue";

  public static final String BASE_PACKAGE_ENDPOINT = "com.pbl.elearning.web";
  ;

  private CommonConstant() {}
}