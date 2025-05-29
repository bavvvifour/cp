package sfu.cp.rep;

import org.springframework.data.jpa.repository.JpaRepository;
import sfu.cp.model.PriceHistory;
import sfu.cp.model.Product;
import sfu.cp.model.User;

import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductOrderByChangeDateDesc(Product product);
    List<PriceHistory> findByProduct_UserOrderByChangeDateDesc(User user);
}