package vn.com.atomi.loyalty.eventgateway.repository.redis;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;
import vn.com.atomi.loyalty.eventgateway.enums.StatusCardTransaction;

@Repository
public interface CardTransactionFileRepository extends JpaRepository<CardTransactionFile, Long> {

  Optional<CardTransactionFile> findByDeletedFalseAndId(Long id);

  @Query(
      value =
          "SELECT a "
              + "FROM CardTransactionFile a "
              + "WHERE "
              + "    (:statusCard IS NULL OR a.statusCard = :statusCard) "
              + "    And (a.deleted = false ) "
              + "    And (:id IS NULL OR a.id = :id) "
              + "    AND (:createdBy IS NULL OR a.createdBy = :createdBy) "
              + "    AND (:startTransactionDate IS NULL OR a.createdAt >= :startTransactionDate) "
              + "    AND (:endTransactionDate IS NULL OR a.createdAt <= :endTransactionDate)")
  Page<CardTransactionFile> getListCardTransactionFile(
      Long id,
      LocalDateTime startTransactionDate,
      LocalDateTime endTransactionDate,
      StatusCardTransaction statusCard,
      String createdBy,
      Pageable pageable);
}
