package com.medicalwale.gniapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBean {

    @SerializedName("card_type")
    @Expose
    private String card_type;


    @SerializedName("otp_code")
    @Expose
    private int otp_code;
    @SerializedName("user")
    @Expose
    private String user;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("height")
    @Expose
    private String height;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("marital_status")
    @Expose
    private String marital_status;
    @SerializedName("blood_group")
    @Expose
    private String blood_group;
    @SerializedName("diet_fitness")
    @Expose
    private String diet_fitness;
    @SerializedName("organ_donor")
    @Expose
    private String organ_donor;
    @SerializedName("sex_history")
    @Expose
    private String sex_history;

    @SerializedName("card_no")
    @Expose
    private String card_no;

    @SerializedName("profile_percentage")
    @Expose
    private String profile_percentage;

    public String getIs_Miss_belly_activated() {
        return is_Miss_belly_activated;
    }

    public void setIs_Miss_belly_activated(String is_Miss_belly_activated) {
        this.is_Miss_belly_activated = is_Miss_belly_activated;
    }

    @SerializedName("is_Miss_belly_activated")
    @Expose
    private String is_Miss_belly_activated;

    public String getIs_mail() {
        return is_mail;
    }

    public void setIs_mail(String is_mail) {
        this.is_mail = is_mail;
    }

    @SerializedName("is_mail")
    @Expose
    private String is_mail;

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public int getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(int otp_code) {
        this.otp_code = otp_code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMarital_status() {
        return marital_status;
    }

    public void setMarital_status(String marital_status) {
        this.marital_status = marital_status;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getDiet_fitness() {
        return diet_fitness;
    }

    public void setDiet_fitness(String diet_fitness) {
        this.diet_fitness = diet_fitness;
    }

    public String getOrgan_donor() {
        return organ_donor;
    }

    public void setOrgan_donor(String organ_donor) {
        this.organ_donor = organ_donor;
    }

    public String getSex_history() {
        return sex_history;
    }

    public void setSex_history(String sex_history) {
        this.sex_history = sex_history;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getProfile_percentage() {
        return profile_percentage;
    }

    public void setProfile_percentage(String profile_percentage) {
        this.profile_percentage = profile_percentage;
    }

}
