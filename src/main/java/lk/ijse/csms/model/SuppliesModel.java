package lk.ijse.csms.model;

import lk.ijse.csms.db.DBConnection;
import lk.ijse.csms.dto.Supplies;
import lk.ijse.csms.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesModel {
    public static String getNextSuppliesId() throws SQLException, ClassNotFoundException {
        ResultSet rst= CrudUtil.execute("SELECT suppliesId FROM supplies ORDER BY suppliesId DESC LIMIT 1");
        if(rst.next()){
            return String.format("G%03d", Integer.parseInt(rst.getString(1).substring(1))+1);
        }
        return "G001";
    }

    public static boolean addSupplies(Supplies supplies) throws SQLException, ClassNotFoundException {
        try{
            DBConnection.getInstance().getConnection().setAutoCommit(false);
            if (CrudUtil.execute("INSERT INTO supplies VALUES(?,?,?,?)",supplies.getSuppliesId(),supplies.getDate(),supplies.getTime(),supplies.getSupplierId())){
                if (SuppliesDetailsModel.addDetails(supplies.getSuppliesDetailsArrayList())){
                    if (ItemModel.updateSuppliedItem(supplies.getSuppliesDetailsArrayList())){
                        DBConnection.getInstance().getConnection().commit();
                        return true;
                    }
                }
            }
            DBConnection.getInstance().getConnection().rollback();
            return false;
        }finally {
            DBConnection.getInstance().getConnection().setAutoCommit(true);
        }
    }

    public static ArrayList<Supplies> getAllSupplies() throws SQLException, ClassNotFoundException {
        ArrayList<Supplies>suppliesArrayList=new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT *FROM supplies");
        while (rst.next()){
            suppliesArrayList.add(new Supplies(
                    rst.getString(1),
                    rst.getDate(2),
                    rst.getTime(3),
                    rst.getString(4),
                    null
            ));
        }
        return suppliesArrayList;
    }
}
