package vn.com.atomi.loyalty.eventgateway.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.com.atomi.loyalty.base.constant.DateConstant;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.data.BaseService;
import vn.com.atomi.loyalty.base.event.MessageData;
import vn.com.atomi.loyalty.base.event.MessageInterceptor;
import vn.com.atomi.loyalty.eventgateway.dto.message.AllocationPointMessage;
import vn.com.atomi.loyalty.eventgateway.dto.message.AllocationPointTransactionInput;
import vn.com.atomi.loyalty.eventgateway.dto.message.Lv24HTransactionMessage;
import vn.com.atomi.loyalty.eventgateway.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.eventgateway.enums.PointEventSource;
import vn.com.atomi.loyalty.eventgateway.enums.RuleType;
import vn.com.atomi.loyalty.eventgateway.enums.SourceGroup;
import vn.com.atomi.loyalty.eventgateway.feign.LoyaltyConfigClient;
import vn.com.atomi.loyalty.eventgateway.feign.LoyaltyCoreClient;
import vn.com.atomi.loyalty.eventgateway.service.HandlerTransactionEventService;
import vn.com.atomi.loyalty.eventgateway.utils.Constants;
import vn.com.atomi.loyalty.eventgateway.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class HandlerTransactionEventServiceImpl extends BaseService
    implements HandlerTransactionEventService {

  private final LoyaltyCoreClient loyaltyCoreClient;

  private final LoyaltyConfigClient loyaltyConfigClient;

  private final MessageInterceptor messageInterceptor;

  @Value("${custom.properties.kafka.topic.allocation-point-event.name}")
  private String allocationPointTopic;

  @Override
  public void makeLv24hTransactionHandle(Lv24HTransactionMessage lv24HTransactionMessage) {
    CustomerOutput customerOutput =
        loyaltyCoreClient
            .getCustomer(
                ThreadContext.get(RequestConstant.REQUEST_ID),
                lv24HTransactionMessage.getTransactionFrom().getFromCustNo(),
                null)
            .getData();
    AllocationPointMessage allocationPointMessage =
        AllocationPointMessage.builder()
            .transaction(
                AllocationPointTransactionInput.builder()
                    .transactionAt(
                        Utils.convertToLocalDateTime(
                            lv24HTransactionMessage.getTransactionHeader().getTransTime(),
                            DateConstant.STR_PLAN_YYYY_MM_DD_HH_MM_SS_SSS_STROKE))
                    .transactionGroup(
                        loyaltyConfigClient
                            .getSourceDataMap(
                                ThreadContext.get(RequestConstant.REQUEST_ID),
                                lv24HTransactionMessage.getTransactionHeader().getProductId(),
                                Constants.SourceType.TRANSACTION,
                                SourceGroup.LV24H)
                            .getData()
                            .getDestinationCode())
                    .refNo(lv24HTransactionMessage.getTransactionHeader().getTransCode())
                    .amount(
                        Long.parseLong(
                            lv24HTransactionMessage.getTransactionAmount().getInputAmount()))
                    .currency(lv24HTransactionMessage.getTransactionAmount().getAmountCurrency())
                    .build())
            .type(RuleType.TRANSACTION)
            .pointEventSource(PointEventSource.LV24H)
            .customer(customerOutput)
            .build();
    messageInterceptor.convertAndSend(
        allocationPointTopic,
        customerOutput.getId().toString(),
        new MessageData<>(allocationPointMessage));
  }
}
