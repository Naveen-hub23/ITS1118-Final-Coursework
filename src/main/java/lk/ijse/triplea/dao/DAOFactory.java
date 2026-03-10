package lk.ijse.triplea.dao;

import lk.ijse.triplea.dao.custom.impl.ItemDAOImpl;
import lk.ijse.triplea.dao.custom.impl.SupplierDAOImpl;
import lk.ijse.triplea.dao.custom.impl.UserDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;

    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {ITEM ,SUPPLIER,USER}

    public SuperDAO getDAO(DAOTypes types) {
        switch (types) {
            case ITEM:
                return new ItemDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }
}