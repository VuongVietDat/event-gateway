package vn.com.atomi.loyalty.eventgateway.repository.redis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.CardTransactionFile;

@Repository
public interface CardTransactionFileRepository extends JpaRepository<CardTransactionFile, Long> {

}
