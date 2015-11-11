package com.theironyard;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Jack on 11/11/15.
 */
@Entity
public class Customer {
    @Id
    @GeneratedValue
    Integer id;

    String name;
    String email;

    @OneToMany(mappedBy = "customer")
    List<Purchase> purchases;
}