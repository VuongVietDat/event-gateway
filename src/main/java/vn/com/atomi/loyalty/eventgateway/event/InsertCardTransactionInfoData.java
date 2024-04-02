package vn.com.atomi.loyalty.eventgateway.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsertCardTransactionInfoData extends EvenData {
  private MultipartFile multipartFile;
  private CardTransactionFile cardTransactionFile;
}
