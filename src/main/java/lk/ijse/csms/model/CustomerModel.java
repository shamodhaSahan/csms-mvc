package lk.ijse.csms.model;

import lk.ijse.csms.dto.Customer;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    public static int getCustomerCount() throws SQLException, ClassNotFoundException {
        List<Customer> allCustomerList=getAllCustomer();
        return allCustomerList.size();
    }

    public static List<Customer> getAllCustomer() throws SQLException, ClassNotFoundException {
        List<Customer>allCustomerList=new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT *FROM customer");
        while (rst.next()){
            allCustomerList.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)

            ));
        }
        return allCustomerList;
    }

    public static boolean deleteCustomer(String customerId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM customer WHERE customerId=?",customerId);
    }

    public static String getNextCustomerId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT customerId FROM customer ORDER BY customerId DESC LIMIT 1");
        if(rst.next()){
            return String.format("C%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "C001";
    }

    public static String existsCustomer(String nic) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT customerId FROM customer WHERE nic=?",nic);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean addCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO customer VALUES(?,?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getName(),
                customer.getAddress(),
                customer.getNic(),
                customer.getTelephoneNumber(),
                customer.getEmail()
        );
    }

    public static boolean updateCustomer(Customer customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE customer set name=?, address=?, nic=?, telephoneNumber=?, email=? where customerId=?",
                customer.getName(),
                customer.getAddress(),
                customer.getNic(),
                customer.getTelephoneNumber(),
                customer.getEmail(),
                customer.getCustomerId()
        );
    }

    public static ArrayList<String> getAllCustomerId() throws SQLException, ClassNotFoundException {
        ArrayList<String>customerIdList=new ArrayList<>();
        ResultSet rst=CrudUtil.execute("SELECT customerId FROM customer");
        while (rst.next()){
            customerIdList.add(rst.getString(1));
        }
        return customerIdList;
    }

    public static ArrayList<String> getAllCustomerName() throws SQLException, ClassNotFoundException {
        ArrayList<String>customerNameList=new ArrayList<>();
        ResultSet rst=CrudUtil.execute("SELECT name FROM customer");
        while (rst.next()){
            customerNameList.add(rst.getString(1));
        }
        return customerNameList;
    }

    public static String searchCustomerId(String cusName) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT customerId FROM customer WHERE name=?",cusName);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static String searchCustomerName(String cusId) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT name FROM customer WHERE customerId=?",cusId);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static String searchTelNumber(String cusId) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT telephoneNumber FROM customer WHERE customerId=?",cusId);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean clearAll() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from customer");
    }
}
