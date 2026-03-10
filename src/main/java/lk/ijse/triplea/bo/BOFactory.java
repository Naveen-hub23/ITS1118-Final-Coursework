package lk.ijse.triplea.bo;

import lk.ijse.triplea.bo.custom.impl.ItemBOImpl;
import lk.ijse.triplea.bo.custom.impl.SupplierBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {ITEM, SUPPLIER}

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            default:
                return null;
        }
    }
}