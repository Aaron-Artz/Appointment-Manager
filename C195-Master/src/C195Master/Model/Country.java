package C195Master.Model;

import java.sql.*;

import static C195Master.Model.User.dbConnect;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class Country {
    //countryID generated through database sequence
    private int countryID = 0;
    private String country;
    private Date createDate;
    private String createBy;
    private Timestamp lastUpdate;
    private String lastUpdateBy;

    public Country() {};
/*
    public Country(int countryID, String country, Date createDate, String createBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.countryID = countryID;
        this.country = country;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

 */

    // Getters
    public int getCountryID() { return countryID; }
    public String getCountry() {
        return country;
    }
    public Date getCreateDate(){
        return createDate;
    }
    public String getCreateBy(){
        return createBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy(){
        return lastUpdateBy;
    }

    // Setters
    public void setCountryID(int countryID){
        this.countryID = 0;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }


    // Takes in country name and returns matching Country ID
    public static int getCountryId(String country) {
        int countryId = 0;
        String getCountryIdStatement = "SELECT countryId FROM country WHERE country = '" + country +"';";
        try {
            PreparedStatement statement = dbConnect.prepareStatement(getCountryIdStatement);
            ResultSet resultSset = statement.executeQuery();
            while (resultSset.next()) {
                countryId = resultSset.getInt("countryId");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return countryId;
    }

}
