package lk.ijse.csms.model;


import lk.ijse.csms.dto.Repair;
import lk.ijse.csms.dto.RepairTransactionDetails;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepairModel {
    public static String getNextRepairId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT repairId FROM repair ORDER BY repairId DESC LIMIT 1");
        if(rst.next()){
            return String.format("R%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "R001";
    }
    public static int getNonReturnRepairCount() throws SQLException, ClassNotFoundException {
        List<Repair> nonReturnRepairList=getAllNonReturnRepair();
        return nonReturnRepairList.size();
    }

    public static List<Repair> getAllNonReturnRepair() throws SQLException, ClassNotFoundException {
        List<Repair>nonReturnRepairList=new ArrayList<>();
        ResultSet rst= CrudUtil.execute("SELECT *FROM repair WHERE status=? || status=?" ,"repaired","repairing");
        while (rst.next()){
            nonReturnRepairList.add(new Repair(
                    rst.getString(1),
                    LocalDate.parse(rst.getString(2)),
                    LocalDate.parse(rst.getString(3)),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return nonReturnRepairList;
    }

    public static List<Repair> getAllReturnedRepair() throws SQLException, ClassNotFoundException {
        ArrayList<Repair>returnedReturnRepairList=new ArrayList<>();
        ResultSet rst= CrudUtil.execute("SELECT *FROM repair WHERE status=?","returned");
        while (rst.next()){
            returnedReturnRepairList.add(new Repair(
                    rst.getString(1),
                    LocalDate.parse(rst.getString(2)),
                    LocalDate.parse(rst.getString(3)),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return returnedReturnRepairList;
    }

    public static boolean deleteRepair(String repairId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM repair WHERE repairId=?",repairId);
    }

    public static boolean addRepair(Repair repair) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO repair VALUES(?,?,?,?,?,?)",
                repair.getRepairId(),
                repair.getReceiveDate(),
                repair.getReturnDate(),
                repair.getStatus(),
                repair.getDescription(),
                repair.getCustomerId()
        );
    }

    public static boolean updateRepair(Repair repair) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE repair set receiveDate=?, returnDate=?, status=?, description=?, customerId=? where repairId=?",
                repair.getReceiveDate(),
                repair.getReturnDate(),
                repair.getStatus(),
                repair.getDescription(),
                repair.getCustomerId(),
                repair.getRepairId()
        );
    }

    public static String searchRepairId(String repairId) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT description FROM repair WHERE repairId=?",repairId);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean updateRepairForTransaction(List<RepairTransactionDetails> repairTransactionDetailsArrayList) throws SQLException, ClassNotFoundException {
        for (RepairTransactionDetails details:repairTransactionDetailsArrayList) {
            boolean isAdded=CrudUtil.execute("UPDATE repair set status=? where repairId=?",
                    "returned",
                    details.getRepairId());
            if (!isAdded)
                return false;
        }
        return true;
    }
}
