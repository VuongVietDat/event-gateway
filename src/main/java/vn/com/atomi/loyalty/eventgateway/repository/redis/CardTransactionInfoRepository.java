package vn.com.atomi.loyalty.eventgateway.repository.redis;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;

@Repository
public interface CardTransactionInfoRepository extends JpaRepository<CardTransactionInfo, Long> {

  Optional<CardTransactionInfo> findById(Long id);

  @Query(value = "SELECT  * "
      + "FROM LOYALTY_GIFT.eg_card_transaction_info i "
      + "WHERE "
      + "   i.card_transaction_file_id = :id" , nativeQuery = true)
  Page<CardTransactionInfo> findByCondition(Long id, Pageable pageable);

}
