package com.remotehcs.remotehcs.api;

public class LoginResponse
{
    private String token;

    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [token = "+token+"]";
    }
}