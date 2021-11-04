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

public class City {
    public int  cityID = 0;
    public String city;
    public int countryID;
    public Date createDate;
    public String createBy;
    public Timestamp lastUpdate;
    public String lastUpdateBy;

    public City(int cityID, String city, int countryID, Date createDate, String createBy, Timestamp lastUpdate, String lastUpdateBy) {
        this.cityID = cityID;
        this.city = city;
        this.countryID = countryID;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }

    //Getters
    public int getCityID() {
        return cityID;
    }
    public String getCity() {
        return city;
    }
    public int getCountryID() {
        return countryID;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    //Setters

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    // Takes in city name and returns matching City ID
    public static int getCityId(String city) {
        int cityId = 0;
        String getCityIdStatement = "SELECT cityId FROM city WHERE city = '" + city +"';";
        try {
            PreparedStatement statement = dbConnect.prepareStatement(getCityIdStatement);
            ResultSet resultSset = statement.executeQuery();
            while (resultSset.next()) {
                cityId = resultSset.getInt("cityId");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return cityId;
    }

}
