package com.anileren.model;

import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "tckn")
    private String tckn;

    @Column(name = "birth_of_date")
    private Date birthOfDate;

    @OneToOne(cascade = CascadeType.REMOVE)
    //CascadeType.Remove, Customer nesnesi silindiğinde, onunla ilişkili olan Address ve Account nesnelerinin de silinmesini sağlar.
    private Address address;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Account account;
}
