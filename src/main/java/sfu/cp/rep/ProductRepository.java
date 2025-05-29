package sfu.cp.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sfu.cp.model.Product;
import sfu.cp.model.User;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserId(Long userId);
    List<Product> findByUser(User user);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.priceHistory ph ORDER BY ph.changeDate DESC")
    List<Product> findAllWithPriceHistory();

    List<Product> findByUserAndStatusNot(User user, Product.Status status);
}
