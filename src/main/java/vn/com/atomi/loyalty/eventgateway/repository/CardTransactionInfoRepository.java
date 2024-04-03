package vn.com.atomi.loyalty.eventgateway.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionInfo;

@Repository
public interface CardTransactionInfoRepository extends JpaRepository<CardTransactionInfo, Long> {

  Optional<CardTransactionInfo> findByDeletedFalseAndId(Long id);

  Page<CardTransactionInfo> findByDeletedFalseAndCardTransactionFileId(Long id, Pageable pageable);
}
