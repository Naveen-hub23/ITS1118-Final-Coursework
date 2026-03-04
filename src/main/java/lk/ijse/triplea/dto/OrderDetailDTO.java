package lk.ijse.triplea.dto;

public class OrderDetailDTO {

    private String orderId;
    private int itemId;
    private int qty;
    private double unitPrice;

    public OrderDetailDTO() {}

    public OrderDetailDTO( int itemId, int qty) {
        this.itemId = itemId;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public OrderDetailDTO(String orderId, int itemId, int qty, double unitPrice) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getQty() {
        return qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

}