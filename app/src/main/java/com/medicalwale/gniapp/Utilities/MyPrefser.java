package com.medicalwale.gniapp.Utilities;

import android.content.Context;

import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.medicalwale.gniapp.Model.UserBean;

public class MyPrefser extends Prefser{

    //Prefser prefser;
    Context context;

    public static String USERDATA = "USERDATA";
    public static String isUserLogin = "isUserLogin";
    public static String UserType = "UserType";

    public MyPrefser(Context context) {
        super(context);
        this.context = context;
        //prefser = new Prefser(this.context);
    }

    public UserBean getUserData()
    {
        return get(MyPrefser.USERDATA,UserBean.class,new UserBean());
    }

    public void saveUserData(UserBean userBean)
    {
        put(MyPrefser.USERDATA,userBean);
    }

    public void setUserLogin(boolean b)
    {
        put(MyPrefser.isUserLogin,b);

        if(!b)
        {
            remove(USERDATA);
        }
    }

    public boolean checkUserLogin(UserBean userBean)
    {
        return get(MyPrefser.isUserLogin,Boolean.class,false);
    }

    public String getString(String key)
    {
        return get(key,String.class,"");
    }

    public int getInt(String key)
    {
        return get(key,Integer.class,0);
    }
}
