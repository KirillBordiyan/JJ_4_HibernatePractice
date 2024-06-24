package org.example.hw;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
@Getter
@Setter
@NoArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "timestamp")
    private Timestamp timestamp;

    public PostComment(String text, Post post) {
        this.text = text;
        this.post = post;
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "PostComment(" +
                "id=" + id +
                ", text='" + text + '\''+
                ", post_id=" +post.getId() + ")";
    }
}
