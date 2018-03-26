package net.proselyte.springsecurityapp.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {
    public User(String email, String password, String name, String surname, String cookieId, String status, int fine, String address, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.cookieId = cookieId;
        this.status = status;
        this.fine = fine;
        this.address = address;
        this.phone = phone;
    }

    public User(String email, String password, String name, String surname) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    public User(String email, String password, String name, String surname, String status) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.status = status;
    }

    public User(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ColumnDefault("")
    @Column(name = "email")
    private String email;
    @ColumnDefault("")
    @Column(name = "password")
    private String password;
    @ColumnDefault("")
    @Column(name = "name")
    private String name;
    @ColumnDefault("")
    @Column(name = "surname")
    private String surname;
    @ColumnDefault("")
    @Column(name = "cookieId")
    private String cookieId;
    @ColumnDefault("disabled")
    @Column(name = "status")
    private String status;
    @Column(name = "fine")
    @ColumnDefault("0")
    private int fine;
    @ColumnDefault("")
    @Column(name = "address")
    private String address;
    @ColumnDefault("")
    @Column(name = "phone")
    private String phone;

    public int getPositionInQueue(List<User> queue){
        int i = 0;
        for (User u:queue){
            if (u.getId() == this.id)
                break;
            i++;
        }
        if (queue.indexOf(this) == i) System.out.println("It works");
//        return queue.indexOf(this);
        return i;
    }




    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", cookieId='" + cookieId + '\'' +
                ", status='" + status + '\'' +
                ", fine=" + fine +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

