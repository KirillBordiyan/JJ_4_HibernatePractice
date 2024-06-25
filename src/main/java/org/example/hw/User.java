package org.example.hw;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Post> postList;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<PostComment> postCommentList;

    public User(String name) {
        this.name = name;
        this.postList = new ArrayList<>();
        this.postCommentList = new ArrayList<>();
    }

    public void addPost(Post post) {
        postList.add(post);
    }

    public void addComment(PostComment comment) {
        postCommentList.add(comment);
    }

    @Override
    public String toString() {
        return "User(" +
                "id=" + id +
                ", name='" + name + ")\n";
    }
}
