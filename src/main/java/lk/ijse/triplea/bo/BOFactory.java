package lk.ijse.triplea.bo;

import lk.ijse.triplea.bo.custom.impl.ItemBOImpl;
import lk.ijse.triplea.bo.custom.impl.SupplierBOImpl;
import lk.ijse.triplea.bo.custom.impl.UserBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {ITEM, SUPPLIER, USER}

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }
}