package com.easytech.smartcustomerapp.pojo;

import java.io.Serializable;

/**
 * Created by Bana on 5/4/16.
 */
public class Customer implements Serializable {

    private Long custId;

    private String fname;
    private String lname;
    private String race;

    private Long dob;

    private String gender;
    private String address;
    private String postalCode;
    private String contact;
    private String email;
    private String password;

    private Long membershipId;
    private Long loyaltyPoints;
    private Long dateRegistered;

    public Customer() {

    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Long getDob() {
        return dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getMembershipId() {
        return membershipId;
    }

    public void setmembershipId(Long membershipId) {
        this.membershipId = membershipId;
    }

    public Long getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Long loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", race='" + race + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", membershipId=" + membershipId +
                ", loyaltyPoints=" + loyaltyPoints +
                ", dateRegistered=" + dateRegistered +
                '}';
    }
}
