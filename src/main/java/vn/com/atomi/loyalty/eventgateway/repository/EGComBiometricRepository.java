package vn.com.atomi.loyalty.eventgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.EGCompleteBiometric;

import java.util.List;
import java.util.Optional;

@Repository
public interface EGComBiometricRepository extends JpaRepository<EGCompleteBiometric, Long> {

    Optional<EGCompleteBiometric> findById(Integer id);

    List<EGCompleteBiometric> findByDeletedFalseAndIsPlusPointFalse();

}
