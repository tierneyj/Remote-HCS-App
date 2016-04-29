package com.remotehcs.remotehcs.record;


public class PatientData
{
    private String mname;

    private String lname;

    private String sex;

    private String status;

    private String address;

    private String state;

    private String med_his;

    private String pid;

    private String pubpid;

    private String phone_contact;

    private String date;

    private String country_code;

    private String city;

    private String email;

    private String phone_cell;

    private String postal_code;

    private String dob;

    private String gov_id;

    private String fname;

    public String getMname ()
    {
        return mname;
    }

    public void setMname (String mname)
    {
        this.mname = mname;
    }

    public String getLname ()
    {
        return lname;
    }

    public void setLname (String lname)
    {
        this.lname = lname;
    }

    public String getSex ()
    {
        return sex;
    }

    public void setSex (String sex)
    {
        this.sex = sex;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getState ()
    {
        return state;
    }

    public void setState (String state)
    {
        this.state = state;
    }

    public String getMed_his ()
{
    return med_his;
}

    public void setMed_his (String med_his)
    {
        this.med_his = med_his;
    }

    public String getPid ()
    {
        return pid;
    }

    public void setPid (String pid)
    {
        this.pid = pid;
    }

    public String getPubpid ()
    {
        return pubpid;
    }

    public void setPubpid (String pubpid)
    {
        this.pubpid = pubpid;
    }

    public String getPhone_contact ()
    {
        return phone_contact;
    }

    public void setPhone_contact (String phone_contact)
    {
        this.phone_contact = phone_contact;
    }

    public String getDate ()
    {
        return date;
    }

    public void setDate (String date)
    {
        this.date = date;
    }

    public String getCountry_code ()
    {
        return country_code;
    }

    public void setCountry_code (String country_code)
    {
        this.country_code = country_code;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getPhone_cell ()
    {
        return phone_cell;
    }

    public void setPhone_cell (String phone_cell)
    {
        this.phone_cell = phone_cell;
    }

    public String getPostal_code ()
    {
        return postal_code;
    }

    public void setPostal_code (String postal_code)
    {
        this.postal_code = postal_code;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getGov_id ()
    {
        return gov_id;
    }

    public void setGov_id (String gov_id)
    {
        this.gov_id = gov_id;
    }

    public String getFname ()
    {
        return fname;
    }

    public void setFname (String fname)
    {
        this.fname = fname;
    }

//    @Override
//    public String toString()
//    {
//        String str;
//        str = "{";
//        str += "\"fname\":\"" + fname + "\",";
//        str += "\"lname\":\"" + lname + "\",";
//        str += "\"lname\":\"" + lname + "\",";
//        str += "\"email\":\"" + email + "\"";
//        str += "}";
//
//        return str;
//
//    }


    @Override
    public String toString() {
        return "{" +
                "\"mname\":\"" + mname + '\"' +
                ",\"lname\":\"" + lname + '\"' +
                ",\"sex\":\"" + sex + '\"' +
                ",\"status\":\"" + status + '\"' +
                ",\"address\":\"" + address + '\"' +
                ",\"state\":\"" + state + '\"' +
                ",\"med_his\":\"" + med_his + '\"' +
                ",\"pid\":\"" + pid + '\"' +
                ",\"pubpid\":\"" + pubpid + '\"' +
                ",\"date\":\"" + date + '\"' +
                ",\"country_code\":\"" + country_code + '\"' +
                ",\"city\":\"" + city + '\"' +
                ",\"email\":\"" + email + '\"' +
                ",\"phone_cell\":\"" + phone_cell + '\"' +
                ",\"postal_code\":\"" + postal_code + '\"' +
                ",\"dob\":\"" + dob + '\"' +
                ",\"gov_id\":\"" + gov_id + '\"' +
                ",\"fname\":\"" + fname + '\"' +
                '}';
    }
}