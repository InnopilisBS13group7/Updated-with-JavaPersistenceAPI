package net.proselyte.springsecurityapp.dao;




import net.proselyte.springsecurityapp.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Integer> {
        public List<Order> findByStatus(String status);
        public List<Order> findByUserId(int userId);
        public List<Order> findByUserIdAndStatus(int userId,String status);
        public List<Order> findAllByItemIdAndStatus(int itemId,String status);

}