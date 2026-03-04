package lk.ijse.triplea.dto;

public class ItemDTO {

    private int id;
    private String name;
    private int qty;
    private double unitPrice;

    public ItemDTO() {}


    public ItemDTO(String name, int qty, double unitPrice) {
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }


    public ItemDTO(int id, String name, int qty, double unitPrice) {
        this.id = id;
        this.name = name;
        this.qty = qty;
        this.unitPrice = unitPrice;
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


    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}