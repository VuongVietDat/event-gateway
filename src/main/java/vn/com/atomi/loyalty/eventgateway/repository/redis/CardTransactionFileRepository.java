package vn.com.atomi.loyalty.eventgateway.repository.redis;

import java.util.Date;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;

@Repository
public interface CardTransactionFileRepository extends JpaRepository<CardTransactionFile, Long> {

  Optional<CardTransactionFile> findByDeletedFalseAndId(Long id);

  @Query(
      value =
          "SELECT * "
              + "FROM eg_card_transaction_file a "
              + "WHERE "
              + "    (:statusCard IS NULL OR a.status_card = :statusCard) "
              + "    And (a.is_deleted = 0) "
              + "    And (:id IS NULL OR a.id = :id) "
              + "    AND (:createdBy IS NULL OR a.created_by = :createdBy) "
              + "    AND (:startTransactionDate IS NULL OR a.created_at >= :startTransactionDate) "
              + "    AND (:endTransactionDate IS NULL OR a.created_at <= :endTransactionDate)",
      nativeQuery = true)
  Page<CardTransactionFile> getListCardTransactionFile(
      Long id,
      Date startTransactionDate,
      Date endTransactionDate,
      String statusCard,
      String createdBy,
      Pageable pageable);
}
