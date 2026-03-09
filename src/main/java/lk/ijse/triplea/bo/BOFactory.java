package lk.ijse.triplea.bo;

import lk.ijse.triplea.bo.custom.impl.ItemBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {ITEM}

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case ITEM:
                return new ItemBOImpl();
            default:
                return null;
        }
    }
}