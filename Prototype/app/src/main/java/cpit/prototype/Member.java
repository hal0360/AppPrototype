package cpit.prototype;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Member {
    public int id;
    public String username;
    public String password;
    public String email;
    public String gender;
    public String date_of_birth;
    public double entry;

    public Member(int ii, String u, String p, String e, String g, String d, double en){
        id = ii;
        username = u;
        password = p;
        email = e;
        gender = g;
        date_of_birth = d;
        entry = en;
    }
}
