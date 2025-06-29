package com.twd.Pos.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tables")
@NoArgsConstructor
public class Tables {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String location;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @JsonBackReference
    private List<Order> orders;

    @ElementCollection
    private List<Long> mergedTables;

    public Tables(String name, String status, String type, String location) {
        this.name = name;
        this.status = status;
        this.type = type;
        this.location = location;
    }



}
