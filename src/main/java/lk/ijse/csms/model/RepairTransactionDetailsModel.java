package lk.ijse.csms.model;

import lk.ijse.csms.dto.RepairTransactionDetails;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepairTransactionDetailsModel {
    public static RepairTransactionDetails searchRepairId(String repairId) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT *FROM repairtransactiondetails WHERE repairId=?",repairId);
        if (rst.next()){
            return new RepairTransactionDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3)
            );
        }
        return null;
    }

    public static boolean addDetails(List<RepairTransactionDetails> repairTransactionDetailsArrayList) throws SQLException, ClassNotFoundException {
        for (RepairTransactionDetails details:repairTransactionDetailsArrayList) {
            boolean isAdded=CrudUtil.execute("INSERT INTO repairtransactiondetails VALUES(?,?,?)",
                    details.getTransactionId(),
                    details.getRepairId(),
                    details.getRepairPrice()
            );
            if(!isAdded)
                return false;
        }
        return true;
    }

    public static ArrayList<RepairTransactionDetails> search(String transactionId) throws SQLException, ClassNotFoundException {
        ArrayList<RepairTransactionDetails>details=new ArrayList<>();
        ResultSet rst=CrudUtil.execute("SELECT * FROM repairtransactiondetails WHERE transactionId=?",transactionId);
        while (rst.next()){
            details.add(new RepairTransactionDetails(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3)
            ));
        }
        return details;
    }
}
