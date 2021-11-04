package C195Master.Model;

import java.sql.Date;
import java.sql.Timestamp;

/********************************************
 *
 *          Aaron Joseph Artz
 *      Western Governors University
 *               C195
 *            8/29/2020
 *
 *******************************************/

public class Address {
    public static int addressId = 0;
    public String address;
    public String address2;
    public int cityId =0;
    public String postalCode;
    public String phone;
    public Date createDate;
    public String createBy;
    public Timestamp lastUpdate;
    public String lastUpdateBy;

    public Address(int addressID, String address, String address2, int cityID, String postalCode, String phone, Date createDate, String createBy, Timestamp lastUpdate, String lastUpdateBy){
        this.addressId=addressID;
        this.address=address;
        this.address2=address2;
        this.cityId=cityID;
        this.postalCode=postalCode;
        this.phone=phone;
        this.createDate=createDate;
        this.createBy=createBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdateBy=lastUpdateBy;
    }

    // Getters
    public static int getAddressId() {
        return addressId;
    }
    public  String getAddress() {
        return address;
    }
    public  String getAddress2() {
        return address2;
    }
    public int getCityId() {
        return cityId;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getPhone() {
        return phone;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public String getCreateBy() {
        return  createBy;
    }
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    // Setters


    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }
    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public void setCityId(int cityID) {
        this.cityId = cityID;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public void setAddressId(int addressID) {
        this.addressId = addressID;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

}
