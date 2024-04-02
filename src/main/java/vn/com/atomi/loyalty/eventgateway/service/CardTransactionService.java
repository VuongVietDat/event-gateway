package vn.com.atomi.loyalty.eventgateway.service;

import java.util.Date;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.base.data.ResponsePage;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionFileOutput;
import vn.com.atomi.loyalty.eventgateway.dto.output.CardTransactionInfoOutput;

public interface CardTransactionService {

  void uploadTransactionFile(MultipartFile transactionFile);

  void updateCardTransaction(CardTransactionInfoInput cardTransactionInfoInput);

  CardTransactionFileOutput getDetailCardTransaction(Long id);

  ResponsePage<CardTransactionInfoOutput> getDetailCardTransactionInfo(Long id, Pageable pageable);

  ResponsePage<CardTransactionFileOutput> getListTransactionFile(
      Long id,
      Date startTransactionDate,
      Date endTransactionDate,
      String statusCard,
      String createdBy,
      Pageable pageable);
}
