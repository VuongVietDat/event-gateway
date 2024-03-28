package vn.com.atomi.loyalty.eventgateway.service;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.eventgateway.dto.input.CardTransactionInfoInput;

public interface CardTransactionService {

  Object uploadTransactionFile(MultipartFile transactionFile) throws IOException;

  void updateCardTransaction(CardTransactionInfoInput cardTransactionInfoInput);
}
