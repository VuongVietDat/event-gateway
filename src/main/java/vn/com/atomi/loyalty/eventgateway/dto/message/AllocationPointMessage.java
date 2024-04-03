package vn.com.atomi.loyalty.eventgateway.dto.message;

import lombok.*;
import vn.com.atomi.loyalty.eventgateway.dto.output.CustomerOutput;
import vn.com.atomi.loyalty.eventgateway.enums.RuleType;

/**
 * @author haidv
 * @version 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AllocationPointMessage {

  private CustomerOutput customer;

  private AllocationPointTransactionInput transaction;

  private RuleType type;
}
