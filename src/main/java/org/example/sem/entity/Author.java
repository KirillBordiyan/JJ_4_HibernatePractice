package org.example.sem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

//    @OneToMany
//    @JoinTable(name = "book",
//    joinColumns = @JoinColumn(name = "id"),
//    inverseJoinColumns = @JoinColumn(name = "author_id"))

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "author_book",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books;

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

