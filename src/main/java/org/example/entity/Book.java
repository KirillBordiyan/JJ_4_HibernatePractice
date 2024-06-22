package org.example.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "book")
@Getter
@Setter
@ToString
public class Book {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @ManyToOne
//    @JoinColumn(name = "author_id", nullable = false)
//    private Author author;

    @ManyToMany
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;
}
