package lk.ijse.carepoint.model;

import lk.ijse.carepoint.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
public class LoginModel {
    public static boolean userCheckedInDB(String username, String password) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM user WHERE username= ? AND password=?";
        ResultSet resultSet = CrudUtil.execute(sql, username, password);
        if(resultSet.next()){
            return true;
        }
        return false;
    }
}
