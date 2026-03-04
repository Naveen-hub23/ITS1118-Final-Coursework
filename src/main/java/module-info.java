//module lk.ijse.triplea {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires java.sql;
//    requires org.controlsfx.controls;
//
//    opens lk.ijse.triplea.controllers to javafx.fxml;
//    opens lk.ijse.triplea.dto to javafx.base;
//
//    exports lk.ijse.triplea;
//}
module lk.ijse.triplea {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires net.sf.jasperreports.core;



    opens lk.ijse.triplea.controllers to javafx.fxml;
    opens lk.ijse.triplea.dto to javafx.base;

    exports lk.ijse.triplea;
}

