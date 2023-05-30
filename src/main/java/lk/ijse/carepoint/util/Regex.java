package lk.ijse.carepoint.util;

public class Regex {
    private static final String USERNAME_REGEX = "^[A-Za-z0-9]{3,}$";
    private static final String PASSWORD_REGEX = "[aA-zZ0-9]{8,20}$";
    private static final String CUSTOMERID_REGEX = "^C[0-9]{4}$";
    private static final String EMPLOYEEID_REGEX = "^E[0-9]{4}$";
    private static final String SUPPLIERID_REGEX = "^S[0-9]{4}$";
    private static final String MEDICINEID_REGEX = "^M[0-9]{4}$";
    private static final String MOBILE_REGEX = "^\\+?\\d{10}$";
   // private static final int AGE_REGEX = Integer.parseInt("^[1-9][0-9]?$|^100$");

    //private static final int CUSTOMERAGE_REGEX = Integer.parseInt("\\b([1-9]|[1-9][0-9]|100)\\b");

    public static boolean validateUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    public static boolean validatePassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean validateCustomerid(String customerid) {return customerid.matches(CUSTOMERID_REGEX);}

    public static boolean validateEmployeeid(String employeeid) {return employeeid.matches(EMPLOYEEID_REGEX);}

    public static boolean validateSupplierid(String supplierid) {return supplierid.matches(SUPPLIERID_REGEX);}

    public static boolean validateMedicineid(String medicineid) {return medicineid.matches(MEDICINEID_REGEX);}

    public static boolean validateContact(String contact) { return contact.matches(MOBILE_REGEX);}

    //public static boolean validateAge(int age) { return age.matches(AGE_REGEX);}

    //public static boolean validateCustomerage(int customerage) {return customerage}
}
