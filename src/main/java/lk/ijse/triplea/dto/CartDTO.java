package lk.ijse.triplea.dto;

public class CartDTO {
    private int id;
    private String name;
    private int qty;
    private double unitPrice;
    private double total;

    public CartDTO() {}

    public CartDTO(String name, int qty, double unitPrice, double total) {
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public CartDTO(int id, String name, int qty, double unitPrice, double total) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQty() {
        return qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getTotal() {
        return total;
    }


    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
