package com.nagel.phonebook;

/* zipcode model class, defined Serializable so that objects of the class can be
 * transferred as parameters to other activities. Class consists of get methods. */

public class Zipcode implements java.io.Serializable
{
    private String code;
    private String city;

    public Zipcode(String code, String city)
    {
        this.code = code;
        this.city = city;
    }

    public String getCode()
    {
        return code;
    }

    public String getCity()
    {
        return city;
    }

    public String toString()
    {
        return getCode() + " " + getCity();
    }
}