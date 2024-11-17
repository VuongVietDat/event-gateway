package vn.com.atomi.loyalty.eventgateway.utils;

/**
 * @author haidv
 * @version 1.0
 */
public class Constants {

  public static final String DEFAULT_DAY_START_TIME = "00:00:00";

  public static final String DEFAULT_DAY_END_TIME = "23:59:59";

  public static class Dictionary {
    public static final String RULE_TYPE = "RULE_TYPE";
    public static final String RULE_BONUS_TYPE = "RULE_BONUS_TYPE";
  }

  public static class SourceType {
    public static final String TRANSACTION = "TRANSACTION";
    public static final String PRODUCT_LINE = "PRODUCT_LINE";
  }
  public static class NotificationErrorCode {
    public static final String SUCCESS = "0";
    public static final String TIMEOUT = "-1";
  }

  private Constants() {
    throw new IllegalStateException("Utility class");
  }
}
