package net.proselyte.springsecurityapp.model;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
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
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "cookieId")
    private String cookieId;
    @Column(name = "status")
    private String status;
    @Column(name = "fine")
    private int fine;
    @Column(name = "address")
    private String address;
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
                "}\n";
    }

    public boolean isAppropriateForSearch(String s,String searchType){
        boolean check = false;
        String doc = id + ' ' + name + ' ' + surname + ' ' + status + ' ' + email + " " + address + ' ' + phone;
        if (searchType.equals("name")) doc = name + " " + surname;
        if (searchType.equals("address")) doc = address;
        if (searchType.equals("phone")) doc = phone;
        if (searchType.equals("id")) doc = ""+id;
        if (searchType.equals("type")) doc = status;
        String[] l = doc.split(" ");
        for (String i : l){
            if (editdist(i,s) < 3) check = true;
        }
        return check;
    }

    public static int editdist(String S1, String S2) {
        int m = S1.length(), n = S2.length();
        int[] D1;
        int[] D2 = new int[n + 1];

        for(int i = 0; i <= n; i ++)
            D2[i] = i;

        for(int i = 1; i <= m; i ++) {
            D1 = D2;
            D2 = new int[n + 1];
            for(int j = 0; j <= n; j ++) {
                if(j == 0) D2[j] = i;
                else {
                    int cost = (S1.charAt(i - 1) != S2.charAt(j - 1)) ? 1 : 0;
                    if(D2[j - 1] < D1[j] && D2[j - 1] < D1[j - 1] + cost)
                        D2[j] = D2[j - 1] + 1;
                    else if(D1[j] < D1[j - 1] + cost)
                        D2[j] = D1[j] + 1;
                    else
                        D2[j] = D1[j - 1] + cost;
                }
            }
        }
        return D2[n];
    }
}

