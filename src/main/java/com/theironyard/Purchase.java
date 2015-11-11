package com.theironyard;

import javax.persistence.*;


/**
 * Created by Jack on 11/11/15.
 */
@Entity
public class Purchase {
    @Id
    @GeneratedValue
    Integer id;

    String date;
    String creditCard;
    String cvv;
    String category;

    @ManyToOne
    Customer customer;


}
