package tr.com.java.commerceplatform.store.repository;

import tr.com.java.commerceplatform.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
