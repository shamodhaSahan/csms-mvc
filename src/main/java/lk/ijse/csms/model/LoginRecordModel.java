package lk.ijse.csms.model;

import lk.ijse.csms.dto.LoginRecord;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginRecordModel {
    public static String getNextLoginId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT loginId FROM loginrecord ORDER BY loginId DESC LIMIT 1");
        if(rst.next()){
            return String.format("L%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "L001";
    }


    public static boolean saveLogin(LoginRecord loginRecord) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO loginrecord VALUES(?, ?, ?, ?)",
                loginRecord.getLoginId(),
                loginRecord.getDate(),
                loginRecord.getTime(),
                loginRecord.getUserId()
        );
    }
    public static List<LoginRecord> getAllLogin() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT * FROM loginrecord");
        List<LoginRecord> loginRecordArrayList=new ArrayList<>();
        while (rst.next()){
            loginRecordArrayList.add(new LoginRecord(
                    rst.getString(1),
                    rst.getDate(2).toLocalDate(),
                    rst.getTime(3).toLocalTime(),
                    rst.getString(4)
            ));
        }
        return loginRecordArrayList;
    }
}