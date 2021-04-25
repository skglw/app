package com.example.client_app;

public class MapItem
{
    public int id;
    public String name;

    public MapItem(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}
