package lk.ijse.csms.model;

import lk.ijse.csms.dto.SuppliesDetails;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SuppliesDetailsModel {
    public static boolean addDetails(List<SuppliesDetails> suppliesDetailsArrayList) throws SQLException, ClassNotFoundException {
        for (SuppliesDetails details:suppliesDetailsArrayList) {
            boolean isAdded= CrudUtil.execute("INSERT INTO suppliesdetails VALUES(?,?,?,?)",
                    details.getSuppliesId(),
                    details.getItemCode(),
                    details.getQuantity(),
                    details.getUnitPrice()
            );
            if(!isAdded)
                return false;
        }
        return true;
    }

    public static double getTotal(String suppliesId) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT quantity,unitPrice FROM suppliesdetails WHERE suppliesId=?",suppliesId);
        double total=0;
        if (rst.next()){
            total+=rst.getInt(1)*rst.getDouble(2);
        }
        return total;
    }

    public static List<SuppliesDetails> search(String suppliesId) throws SQLException, ClassNotFoundException {
        ArrayList<SuppliesDetails>details=new ArrayList<>();
        ResultSet rst=CrudUtil.execute("SELECT * FROM suppliesdetails WHERE suppliesId=?",suppliesId);
        while (rst.next()){
            details.add(new SuppliesDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getInt(3),
                    rst.getDouble(4)
            ));
        }
        return details;
    }
}
