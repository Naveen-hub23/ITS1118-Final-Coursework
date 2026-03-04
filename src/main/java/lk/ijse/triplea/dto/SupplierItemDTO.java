package lk.ijse.triplea.dto;

public class SupplierItemDTO {
    private int supplierId;
    private int itemId;
    private String itemName;
    private double costPrice; // <--- RENAMED from 'cost' to 'costPrice'

    public SupplierItemDTO() {}

    public SupplierItemDTO(int supplierId, String itemName, double costPrice) {
        this.supplierId = supplierId;
        this.itemName = itemName;
        this.costPrice = costPrice;
    }

    public SupplierItemDTO(int supplierId, int itemId, String itemName, double costPrice) {
        this.supplierId = supplierId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.costPrice = costPrice;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // --- FIX IS HERE: Renamed getCost() to getCostPrice() ---
    public double getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice = costPrice;
    }
}