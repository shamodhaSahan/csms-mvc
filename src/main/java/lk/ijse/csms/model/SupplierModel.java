package lk.ijse.csms.model;

import lk.ijse.csms.dto.Supplier;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public static String getNextSupplierId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT supplierId FROM supplier ORDER BY supplierId DESC LIMIT 1");
        if(rst.next()){
            return String.format("S%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "S001";
    }

    public static List<Supplier> getAllSupplier() throws SQLException, ClassNotFoundException {
        List<Supplier>allSupplier=new ArrayList<>();
        ResultSet rst= CrudUtil.execute("SELECT * FROM supplier");
        while (rst.next()){
            allSupplier.add(new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5)
            ));
        }
        return allSupplier;
    }

    public static boolean deleteSupplier(String supplierId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM supplier WHERE supplierId=?",supplierId);
    }

    public static String searchName(String name) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT supplierId FROM supplier WHERE name=?",name);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean addSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO supplier VALUES(?,?,?,?,?)",
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getAddress(),
                supplier.getTelephoneNumber(),
                supplier.getEmail()
        );
    }

    public static boolean updateSupplier(Supplier supplier) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE supplier set name=?, address=?, telephoneNumber=?, email=? where supplierId=?",
                supplier.getName(),
                supplier.getAddress(),
                supplier.getTelephoneNumber(),
                supplier.getEmail(),
                supplier.getSupplierId()
        );
    }

    public static String searchId(String selectedItem) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT name FROM supplier WHERE supplierId=?",selectedItem);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }
}
