package vn.com.atomi.loyalty.eventgateway.event;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.NoArgsConstructor;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.event.DrivenEventListener;
import vn.com.atomi.loyalty.base.event.EventInfo;
import vn.com.atomi.loyalty.base.exception.BaseException;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;
import vn.com.atomi.loyalty.eventgateway.enums.StatusCardTransaction;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionFileRepository;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CardTransactionInfoRepository;
import vn.com.atomi.loyalty.eventgateway.repository.redis.CustomRepository;

/**
 * @author haidv
 * @version 1.0
 */
@NoArgsConstructor
@Service
public class ServiceDrivenEventListener extends DrivenEventListener {

  @SuppressWarnings("unused")
  private ApplicationContext applicationContext;

  @SuppressWarnings("unused")
  private ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  private CardTransactionInfoRepository cardTransactionInfoRepository;

  @Autowired
  private CardTransactionFileRepository cardTransactionFileRepository;

  @Autowired
  private CustomRepository customRepository;

  @Autowired
  private ServiceDrivenEventListener(
      ApplicationContext applicationContext,
      @Qualifier("threadPoolTaskExecutor") ThreadPoolTaskExecutor taskExecutor) {
    super(applicationContext, taskExecutor);
    this.applicationContext = applicationContext;
    this.taskExecutor = taskExecutor;
  }

  @Override
  protected void processHandleErrorEventAsync(EventInfo eventInfo) {
  }

  @Override
  protected void processLogHandleEventAsync(EventInfo eventInfo) {
  }
}
