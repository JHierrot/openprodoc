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
public class QueryJSON
{

private String Query=null;
private int Initial=0;
private int Final=100;

public static QueryJSON CreateQuery(String QueryJson)
{
Gson g = new Gson();
return(g.fromJson(QueryJson, QueryJSON.class));    
}
//-------------------------------------------------------------------------
/**
 * @return the Query
 */
public String getQuery()
{
return Query;
}  
//-------------------------------------------------------------------------

    /**
     * @return the Initial
     */
    public int getInitial()
    {
        return Initial;
    }

    /**
     * @return the Final
     */
    public int getFinal()
    {
        return Final;
    }

}
