package vn.com.atomi.loyalty.eventgateway.repository.redis;

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

  @Query(value = "SELECT *\n"
      + "FROM LOYALTY_GIFT.eg_card_transaction_file a\n"
      + "WHERE \n"
      + "    (:statusCard IS NULL OR a.status_card = :statusCard)\n"
      + "    And (a.is_deleted = 0)\n"
//      + "    AND (:name IS NULL OR a.name = :name)\n"
      + "    And (:id IS NULL OR a.id = :id)\n"
      + "    AND (:createdBy IS NULL OR a.created_by = :createdBy)\n"
      + "    AND (:startTransactionDate IS NULL OR a.created_at >= :startTransactionDate)\n"
      + "    AND (:endTransactionDate IS NULL OR a.created_at <= :endTransactionDate)", nativeQuery = true)
  Page<CardTransactionFile> getListCardTransactionFile(
      Long id,
      String startTransactionDate,
      String endTransactionDate,
      String statusCard,
      String createdBy,
      Pageable pageable);
}
