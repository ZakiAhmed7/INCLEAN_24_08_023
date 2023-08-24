package com.gisfy.inclenJson.PayLoads;

import com.google.gson.annotations.SerializedName;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("data")
    private Data data;
    @SerializedName("message")
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Data{
        @SerializedName("token")
        private String token;
        @SerializedName("name")
        private Details name;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public Details getName() {
            return name;
        }

        public void setName(Details name) {
            this.name = name;
        }
    }

    public static class Details{
        @SerializedName("id")
        private int id;
        @SerializedName("first_name")
        private String first_name;
        @SerializedName("last_name")
        private String last_name;
        @SerializedName("phone_number")
        private String phone_number;
        @SerializedName("email")
        private String email;
        @SerializedName("email_verified_at")
        private String email_verified_at;
        @SerializedName("state")
        private String state;
        @SerializedName("district")
        private String district;
        @SerializedName("taluk")
        private String taluka;
        @SerializedName("address")
        private String address;
        @SerializedName("userroleid")
        private int userRoleId;
        @SerializedName("isdeleted")
        private boolean isDeleted;
        @SerializedName("username")
        private String username;
        @SerializedName("created_at")
        private String created_at;
        @SerializedName("updated_at")
        private String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail_verified_at() {
            return email_verified_at;
        }

        public void setEmail_verified_at(String email_verified_at) {
            this.email_verified_at = email_verified_at;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getTaluka() {
            return taluka;
        }

        public void setTaluka(String taluka) {
            this.taluka = taluka;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getUserRoleId() {
            return userRoleId;
        }

        public void setUserRoleId(int userRoleId) {
            this.userRoleId = userRoleId;
        }

        public boolean isDeleted() {
            return isDeleted;
        }

        public void setDeleted(boolean deleted) {
            isDeleted = deleted;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
