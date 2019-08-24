/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package APIRest.beans;

import com.google.gson.Gson;

/**
 *
 * @author jhier
 */
public class User
{
private String Name;
private String Password;

public static User CreateUser(String json)
{
Gson g = new Gson();
return(g.fromJson(json, User.class));    
}

/**
 * @return the Name
 */
public String getName()
{
return Name;
}

/**
 * @param Name the Name to set
 */
public void setName(String Name)
{
this.Name = Name;
}

/**
 * @return the Password
 */
public String getPassword()
{
return Password;
}

/**
 * @param Password the Password to set
 */
public void setPassword(String Password)
{
this.Password = Password;
}
}
