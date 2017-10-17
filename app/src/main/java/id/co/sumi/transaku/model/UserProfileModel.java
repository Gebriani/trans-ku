package id.co.sumi.transaku.model;

import java.io.Serializable;


public class UserProfileModel implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String businessName;
    private String businessPhone;
    private BusinessTypeModel businessType;
    private ProvinceModel province;
    private CityModel city;
    private String email;
    private String gender;
    private String idCardBase64;
    private String idCardNumber;
    private String idCardPath;
    private int isSeller;
    private int isVerified;
    private String latitude;
    private String longitude;
    private String oldPassword;
    private String password;
    private String phone;
    private String pictureBase64;
    private String picturePath;
    private int postalCode;
    private RoleModel role;
    private String storeAddress;
    private String verificationCode;

    public UserProfileModel() {
    }

    public UserProfileModel(Long id, String name, String address, String businessName, String businessPhone, BusinessTypeModel businessType, ProvinceModel province, CityModel city, String email, String gender, String idCardBase64, String idCardNumber, String idCardPath, int isSeller, int isVerified, String latitude, String longitude, String oldPassword, String password, String phone, String pictureBase64, String picturePath, int postalCode, RoleModel role, String storeAddress, String verificationCode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.businessName = businessName;
        this.businessPhone = businessPhone;
        this.businessType = businessType;
        this.province = province;
        this.city = city;
        this.email = email;
        this.gender = gender;
        this.idCardBase64 = idCardBase64;
        this.idCardNumber = idCardNumber;
        this.idCardPath = idCardPath;
        this.isSeller = isSeller;
        this.isVerified = isVerified;
        this.latitude = latitude;
        this.longitude = longitude;
        this.oldPassword = oldPassword;
        this.password = password;
        this.phone = phone;
        this.pictureBase64 = pictureBase64;
        this.picturePath = picturePath;
        this.postalCode = postalCode;
        this.role = role;
        this.storeAddress = storeAddress;
        this.verificationCode = verificationCode;
    }

    public UserProfileModel(Long id, String verificationCode) {
        this.id = id;
        this.verificationCode = verificationCode;
    }


    public UserProfileModel(Long id, String pictureBase64, String status){
        this.id = id;
        this.pictureBase64 = pictureBase64;
    }



    public UserProfileModel(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProvinceModel getProvince() {
        return province;
    }

    public void setProvince(ProvinceModel province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public BusinessTypeModel getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessTypeModel businessType) {
        this.businessType = businessType;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdCardBase64() {
        return idCardBase64;
    }

    public void setIdCardBase64(String idCardBase64) {
        this.idCardBase64 = idCardBase64;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public int getIsSeller() {
        return isSeller;
    }

    public void setIsSeller(int isSeller) {
        this.isSeller = isSeller;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(int isVerified) {
        this.isVerified = isVerified;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureBase64() {
        return pictureBase64;
    }

    public void setPictureBase64(String pictureBase64) {
        this.pictureBase64 = pictureBase64;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getIdCardPath() {
        return idCardPath;
    }

    public void setIdCardPath(String idCardPath) {
        this.idCardPath = idCardPath;
    }
}