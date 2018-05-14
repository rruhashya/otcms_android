package com.hajepierre.otcms.db;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeanpierre on 4/30/18.
 */

public class Profile
{
    private int id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String nationality;

    private String gender;

    private String yearOfBirth;

    private String nationalId;

    private int status;

    public Profile()
    {

    }

    public Profile(String nationalId)
    {

        this.nationalId = nationalId;
    }

    public Profile(String firstName, String lastName, String phoneNumber, String email, String nationality, String gender, String yearOfBirth, String nationalId)
    {

        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationality = nationality;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
        this.nationalId = nationalId;
    }


    public int getId()
    {

        return id;
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public int getStatus()
    {

        return status;
    }

    public void setStatus(int status)
    {

        this.status = status;
    }

    public String getFirstName()
    {

        return firstName;
    }

    public void setFirstName(String firstName)
    {

        this.firstName = firstName;
    }

    public String getLastName()
    {

        return lastName;
    }

    public void setLastName(String lastName)
    {

        this.lastName = lastName;
    }

    public String getPhoneNumber()
    {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {

        this.phoneNumber = phoneNumber;
    }

    public String getEmail()
    {

        return email;
    }

    public void setEmail(String email)
    {

        this.email = email;
    }

    public String getNationality()
    {

        return nationality;
    }

    public void setNationality(String nationality)
    {

        this.nationality = nationality;
    }

    public String getGender()
    {

        return gender;
    }

    public void setGender(String gender)
    {

        this.gender = gender;
    }

    public String getYearOfBirth()
    {

        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth)
    {

        this.yearOfBirth = yearOfBirth;
    }

    public String getNationalId()
    {

        return nationalId;
    }

    public void setNationalId(String nationalId)
    {

        this.nationalId = nationalId;
    }

    @Override
    public String toString()
    {

        return "Profile{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", email='" + email + '\'' + ", nationality='" + nationality + '\'' + ", gender='" + gender + '\'' + ", yearOfBirth='" + yearOfBirth + '\'' + ", nationalId='" + nationalId + '\'' + '}';
    }

    public JSONObject toJson()
    {

        try
        {
            JSONObject object = new JSONObject();
            object.put("idpassnumber", nationalId);
            object.put("fname", firstName);
            object.put("lname", lastName);
            object.put("pnumber", phoneNumber);
            object.put("email", email);
            object.put("nationality", nationality);
            object.put("gender", gender);
            object.put("dob", yearOfBirth);
            return object;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
