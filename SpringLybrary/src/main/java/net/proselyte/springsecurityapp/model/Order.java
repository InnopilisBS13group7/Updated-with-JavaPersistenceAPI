package net.proselyte.springsecurityapp.model;

import net.proselyte.springsecurityapp.controller.Controller;
import net.proselyte.springsecurityapp.service.DocumentService;
import net.proselyte.springsecurityapp.service.OrderService;
import net.proselyte.springsecurityapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order extends Controller{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "userId")
    private int userId;
    @Column(name = "itemId")
    private int itemId;
    @Column(name = "startTime")
    private long startTime;
    @Column(name = "finishTime")
    private long finishTime;
    @Column(name = "status")
    private String status;

    public Order() {
    }

    public Order(int userId, int itemId, long startTime, long finishTime, String status) {
        this.userId = userId;
        this.itemId = itemId;
        this.startTime = startTime;
        this.finishTime = finishTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", status='" + status + '\'' +
                "}\n";
    }

    public static final Comparator<Order> COMPARE_BY_START_TIME = new Comparator<Order>() {
        @Override
        public int compare(Order lhs, Order rhs) {
            return (lhs.getStartTime() > rhs.getStartTime()) ? 1 : 0;
        }
    };


    public int overdueDays(){
        Date date = new Date();
        return (date.getTime() > getFinishTime())? (int)((date.getTime()-getFinishTime())/1000/3600/24) : 0;
    }
}
