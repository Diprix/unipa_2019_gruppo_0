package it.eng.unipa.filesharing.model;

import javax.persistence.*;
import java.io.Serializable;

//@Entity
public class WebPushMessage implements Serializable {

    //@Id
    //@Column(name = "idUser")
    private String idUser;

    //@Column(name = "endPoint")
    private Object endPoint;

    //IMPLEMENT FOREIN KEY CONSTRACT


    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setEndPoint(Object endPoint) {
        this.endPoint = endPoint;
    }

    public Object getEndPoint() {
        return endPoint;
    }
}
