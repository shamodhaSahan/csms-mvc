package lk.ijse.csms.model;

import lk.ijse.csms.dto.User;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserModel {
    public static String getNextUserId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT userId FROM user ORDER BY userId DESC LIMIT 1");
        if(rst.next()){
            return String.format("U%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "U001";
    }
    public static User searchUserName(String userName) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select * from user where userName = ?",userName);
        if (rst.next()){
            return new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    public static boolean searchExistsUserName(String userId,String userName) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select userid from user where userName = ?",userName);
        if (rst.next()){
            if (!userId.equals(rst.getString(1)))
                return true;
        }
        return false;
    }

    public static User searchUserId(String userId) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("select * from user where userid = ?",userId);
        if (rst.next()){
            return new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            );
        }
        return null;
    }

    public static ArrayList<User> getAllUser() throws SQLException, ClassNotFoundException {
        ArrayList<User>userArrayList=new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM user");
        while (rst.next()){
            userArrayList.add(new User(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6),
                    rst.getString(7)
            ));
        }
        return userArrayList;
    }

    public static boolean deleteUser(String userId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM user WHERE userid=?",userId);
    }

    public static String existsUser(String nic) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT userid FROM user WHERE nic=?",nic);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean addUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO user VALUES(?,?,?,?,?,?,?)",
                user.getUserId(),
                user.getUserName(),
                user.getUserPassword(),
                user.getNic(),
                user.getTelephoneNumber(),
                user.getEmail(),
                user.getRank()
        );
    }

    public static boolean updateUser(User user) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE user set userName=?, userPassword=?, nic=?, telephoneNumber=?, email=?, `rank` =? where userid=?",
                user.getUserName(),
                user.getUserPassword(),
                user.getNic(),
                user.getTelephoneNumber(),
                user.getEmail(),
                user.getRank(),
                user.getUserId()
        );
    }

    public static boolean clearAll() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from user");
    }
}
