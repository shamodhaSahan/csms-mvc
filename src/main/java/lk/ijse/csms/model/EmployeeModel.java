package lk.ijse.csms.model;

import lk.ijse.csms.dto.Employee;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public static List<Employee> getAllEmployee() throws SQLException, ClassNotFoundException {
        List<Employee>allEmployeeList=new ArrayList<>();
        ResultSet rst= CrudUtil.execute("SELECT *FROM employee");
        while (rst.next()){
            allEmployeeList.add(new Employee(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            ));
        }
        return allEmployeeList;
    }

    public static boolean deleteEmployee(String employeeId) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM employee WHERE employeeId=?",employeeId);
    }

    public static String getNextEmployeeId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT employeeId FROM employee ORDER BY employeeId DESC LIMIT 1");
        if(rst.next()){
            return String.format("E%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "E001";
    }

    public static String existsEmployee(String nic) throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT employeeId FROM employee WHERE nic=?",nic);
        if (rst.next()){
            return rst.getString(1);
        }
        return null;
    }

    public static boolean addEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO employee VALUES(?,?,?,?,?,?)",
                employee.getEmployeeId(),
                employee.getName(),
                employee.getAddress(),
                employee.getNic(),
                employee.getTelephoneNumber(),
                employee.getRank()
        );
    }

    public static boolean updateEmployee(Employee employee) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE employee set name=?, address=?, nic=?, telephoneNumber=?, `rank`=? where employeeId=?",
                employee.getName(),
                employee.getAddress(),
                employee.getNic(),
                employee.getTelephoneNumber(),
                employee.getRank(),
                employee.getEmployeeId()
        );
    }

    public static boolean clearAll() throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from employee");
    }
}
