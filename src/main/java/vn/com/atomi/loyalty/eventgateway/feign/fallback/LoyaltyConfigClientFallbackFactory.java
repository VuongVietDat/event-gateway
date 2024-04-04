package vn.com.atomi.loyalty.eventgateway.feign.fallback;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.data.ResponseData;
import vn.com.atomi.loyalty.eventgateway.dto.output.SourceDataMapOutput;
import vn.com.atomi.loyalty.eventgateway.enums.SourceGroup;
import vn.com.atomi.loyalty.eventgateway.feign.LoyaltyConfigClient;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class LoyaltyConfigClientFallbackFactory implements FallbackFactory<LoyaltyConfigClient> {

  private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

  @Override
  public LoyaltyConfigClient create(Throwable cause) {
    LOGGER.error("An exception occurred when calling the LoyaltyCoreClient", cause);
    return new LoyaltyConfigClient() {
      @Override
      public ResponseData<SourceDataMapOutput> getSourceDataMap(
          String requestId, String sourceId, String sourceType, SourceGroup sourceGroup) {
        log.info("getLv24MapProduct: set default empty object");
        return new ResponseData<SourceDataMapOutput>().success(new SourceDataMapOutput());
      }
    };
  }
}
