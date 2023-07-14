package com.example.borhanuddin.getlocation;

import java.util.ArrayList;

public class CRUD {

    private ArrayList<String> names =new ArrayList<>();

    public void save(String name)
    {
        names.add(name);
    }

    public ArrayList<String> getNames()
    {

        return names;
    }

    public Boolean update(int position,String newName)
    {
        try {
            names.remove(position);
            names.add(position,newName);

            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean delete(int position)
    {
        try {
            names.remove(position);

            return true;
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;

        }
    }
}

