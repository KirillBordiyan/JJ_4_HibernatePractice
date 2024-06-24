package org.example.hw;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;


    @OneToMany(mappedBy = "post",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER)
    private List<PostComment> comments;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public Post(String title) {
        this.title = title;
        this.comments = new ArrayList<>();
        this.timestamp = new Timestamp(System.currentTimeMillis()
                - ThreadLocalRandom.current().nextInt(1000000, 100000000));
    }

    public void addComment(PostComment comment){
        comments.add(comment);
    }

    @Override
    public String toString() {
        return "Post(" +
                id +
                ", '" + title + '\'' +
                ", " + comments.toString();
    }
}
