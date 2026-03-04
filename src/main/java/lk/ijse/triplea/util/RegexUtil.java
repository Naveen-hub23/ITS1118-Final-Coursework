package lk.ijse.triplea.util;

public class RegexUtil {

        public static final String ID = "^[0-9]+$";
        public static final String NAME = "^[A-Za-z ]{3,50}$"; // Real names (letters only)
        public static final String QTY = "^[0-9]+$";
        public static final String PRICE = "^\\d+(\\.\\d{1,2})?$";
        public static final String PHONE = "^0[0-9]{9}$"; // Matches 10 digits starting with 0 (e.g., 0771234567)
        public static final String EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        public static final String GENERAL_TEXT = "^[A-Za-z0-9, ]+$";
        public static final String USERNAME = "^[A-Za-z0-9]{4,20}$";
}
