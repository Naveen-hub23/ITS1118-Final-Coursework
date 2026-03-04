package lk.ijse.triplea.dto;

import java.sql.Date;
import java.util.List;

public class OrderDTO {

    private String orderId;
    private Date date;
    private double total;
    private List<OrderDetailDTO> details;

    public OrderDTO() {}

    public OrderDTO(Date date, double total, List<OrderDetailDTO> details) {
        this.date = date;
        this.total = total;
        this.details = details;
    }

    public OrderDTO(String orderId, Date date, double total, List<OrderDetailDTO> details) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.details = details;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }


    public List<OrderDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List<OrderDetailDTO> details) {
        this.details = details;
    }

}