package com.example.todolist;

import java.io.Serializable;

public class ListItem implements Serializable{

    private String neckL;
    private String descriptionL;
    private String statusL;
    private String kegiatanL;

    public ListItem( String descriptionL,String neckL, String statusL, String kegiatanL)
    {
        this.neckL = neckL;
        this.descriptionL = descriptionL;
        this.statusL = statusL;
        this.kegiatanL = kegiatanL;
    }

    public String getNeckL(){
        return neckL;
    }

    public String getDescriptionL(){
        return descriptionL;
    }

    public String getStatusL(){
        return statusL;
    }

    public String getKegiatanL(){
        return kegiatanL;
    }

}
