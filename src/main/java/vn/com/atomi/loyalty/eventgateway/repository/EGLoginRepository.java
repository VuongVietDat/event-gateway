package vn.com.atomi.loyalty.eventgateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.com.atomi.loyalty.eventgateway.entity.EGLogin;

import java.util.List;

@Repository
public interface EGLoginRepository extends JpaRepository<EGLogin, Long> {

    List<EGLogin> findByDeletedFalse();
}
