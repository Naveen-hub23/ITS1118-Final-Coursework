package lk.ijse.triplea.bo;

import lk.ijse.triplea.bo.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {ITEM, SUPPLIER, USER, PURCHASE_ORDER, ORDER};

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case USER:
                return new UserBOImpl();
            case PURCHASE_ORDER:
                return new PurchaseOrderBOImpl();
            case ORDER:
                return new OrderBOImpl();
            default:
                return null;
        }
    }
}