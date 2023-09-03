package lk.ijse.csms.model;


import lk.ijse.csms.db.DBConnection;
import lk.ijse.csms.dto.Transaction;
import lk.ijse.csms.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionModel {
    public static String getNextTransactionId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT transactionId FROM transaction ORDER BY transactionId DESC LIMIT 1");
        if(rst.next()){
            return String.format("T%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "T001";
    }
    public static int getAllTransactionCount() throws SQLException, ClassNotFoundException {
        return 0;
    }

    public static double getTodayAllIncome() throws SQLException, ClassNotFoundException {
        return 0;
    }

    private static List<Transaction> searchDate(LocalDate date) throws SQLException, ClassNotFoundException {
        return null;
    }

    public static int searchDateTransactionCount(LocalDate date) throws SQLException, ClassNotFoundException {
        List<Transaction>toDayAllTransaction= searchDate(date);
        return toDayAllTransaction.size();
    }

    public static Transaction searchCustomer(String customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT *FROM transaction WHERE customerId=?",customerId);
        if (rst.next()){
            return new Transaction(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getTime(3),
                    rst.getString(4),
                    rst.getString(5),
                    null,null
            );
        }
        return null;
    }

    public static LocalDate getDate(String transactionId) throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT date FROM transaction WHERE transactionId=?",transactionId);
        if (rst.next()){
            return LocalDate.parse(rst.getString(1));
        }
        return null;
    }

    public static boolean addTransaction(Transaction transaction) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            if (CrudUtil.execute("INSERT INTO transaction VALUES(?,?,?,?,?)", transaction.getTransactionId(), transaction.getDate(), transaction.getTime(), transaction.getCustomerId(), transaction.getType())) {
                if (RepairModel.updateRepairForTransaction(transaction.getRepairTransactionDetailsArrayList())) {
                    if (RepairTransactionDetailsModel.addDetails(transaction.getRepairTransactionDetailsArrayList())) {
                        if (ItemTransactionDetailsModel.addDetails(transaction.getItemTransactionDetailsArrayList())) {
                            if (ItemModel.updateSellingItem((transaction.getItemTransactionDetailsArrayList()))) {
                                connection.commit();
                                return true;
                            }
                        }
                    }
                }
            }
            throw new SQLException("Fail to save data...!");
        } catch (SQLException | ClassNotFoundException e) {
            connection.rollback();
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static ArrayList<Transaction> getAllTransaction() throws SQLException, ClassNotFoundException {
        ResultSet rst=CrudUtil.execute("SELECT *FROM transaction");
        ArrayList<Transaction>transactionArrayList=new ArrayList<>();
        while (rst.next()){
            transactionArrayList.add(new Transaction(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getTime(3),
                    rst.getString(4),
                    rst.getString(5),
                    null,null
            ));
        }
        return transactionArrayList;
    }
}
