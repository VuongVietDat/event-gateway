package vn.com.atomi.loyalty.eventgateway.event;

import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import vn.com.atomi.loyalty.base.constant.RequestConstant;
import vn.com.atomi.loyalty.base.event.BaseRetriesMessageListener;
import vn.com.atomi.loyalty.base.event.MessageData;
import vn.com.atomi.loyalty.base.event.RetriesMessageData;
import vn.com.atomi.loyalty.base.redis.HistoryMessage;
import vn.com.atomi.loyalty.base.utils.JsonUtils;
import vn.com.atomi.loyalty.eventgateway.dto.message.Lv24HTransactionMessage;
import vn.com.atomi.loyalty.eventgateway.utils.Utils;

/**
 * @author haidv
 * @version 1.0
 */
@SuppressWarnings({"rawtypes"})
@RequiredArgsConstructor
@Component
public class Lv24HTransactionEventListenerBaseRetries
    extends BaseRetriesMessageListener<LinkedHashMap> {

  @RabbitListener(queues = "${custom.properties.rabbitmq.queue.lv24h-transaction-event.name}")
  public void lv24hTransactionEvent(
      String data,
      @Header("amqp_consumerQueue") String queue,
      @Header("timestamp") String timestamp) {
    ThreadContext.put(RequestConstant.REQUEST_ID, Utils.generateUniqueId());
    ThreadContext.put(RequestConstant.BROKER_TYPE, RequestConstant.BROKER_RABBIT);
    ThreadContext.put(RequestConstant.MESSAGE_EVENT, queue);
    LOGGER.info("[RabbitConsumer][{}][{}] Incoming: {}", queue, timestamp, data);
    Lv24HTransactionMessage input = JsonUtils.fromJson(data, Lv24HTransactionMessage.class);
    if (input == null) {
      LOGGER.info("[RabbitConsumer][{}][{}]  ignore!", queue, timestamp);
      ThreadContext.clearAll();
      return;
    }
    var messageId =
        String.format("%s_%s_%s", queue, timestamp, input.getTransactionHeader().getTransCode());
    try {
      if (Boolean.FALSE.equals(
          super.historyMessageRepository.put(
              new HistoryMessage(messageId, queue, RequestConstant.BROKER_RABBIT)))) {
        LOGGER.warn("[RabbitConsumer][{}][{}]  message has been processed", queue, timestamp);
        return;
      }
      handleMessageEvent(input, messageId);
      LOGGER.info("[RabbitConsumer][{}][{}]  successful!", queue, timestamp);
    } catch (Exception e) {
      LOGGER.error("[RabbitConsumer][{}][{}]  Exception revert ", queue, timestamp, e);
      var retryData = new MessageData<>(input);
      retryData.updateMessageId(messageId);
      super.messageInterceptor.convertAndSendRetriesEvent(
          new RetriesMessageData(messageId, JsonUtils.toJson(retryData), queue, 300, 15));
    } finally {
      ThreadContext.clearAll();
    }
  }

  @KafkaListener(
      topics = "${custom.properties.kafka.topic.lv24h-transaction-event-retries.name}",
      groupId = "${custom.properties.messaging.kafka.groupId}",
      concurrency = "1",
      containerFactory = "kafkaListenerContainerFactory")
  public void workflowEventRetriesListener(
      String data,
      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
      @Header(KafkaHeaders.RECEIVED_PARTITION) String partition,
      @Header(KafkaHeaders.OFFSET) String offset,
      Acknowledgment acknowledgment) {
    super.messageRetriesListener(data, topic, partition, offset, acknowledgment);
  }

  private void handleMessageEvent(Lv24HTransactionMessage input, String messageId) {}

  @Override
  protected void handleMessageEvent(
      String topic, String partition, String offset, MessageData input, String messageId) {}
}
