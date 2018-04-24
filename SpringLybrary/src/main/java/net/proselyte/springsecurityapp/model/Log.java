package net.proselyte.springsecurityapp.model;

import javax.persistence.*;

@Entity
@Table(name = "log")
public class Log {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date")
    private String date;
    @Column(name = "info")
    private String info;

    public Log(String date, String info){
        this.date=date;
        this.info=info;
    }

    public String getInfo(){
        return this.info;
    }

    public String getDate(){
        return this.date;
    }

    public int getId(){
        return this.id;
    }

}
