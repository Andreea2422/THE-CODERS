package ro.ubbcluj.map.thecoders.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Events {
     User user;
     private String name;
     private Date date;

     public Events(User user, String name, Date date) {
            this.user = user;
            this.name = name;
            this.date = date;
        }

        public User getUserEV() {
            return user;
        }

        public void setUserEV(User user) {
        this.user = user;
    }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Date getData() {
        return date;
    }

        public void setData(Date data) {
        this.date = data;
    }


    }

