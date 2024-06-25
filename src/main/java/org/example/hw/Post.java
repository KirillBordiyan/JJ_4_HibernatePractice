package org.example.hw;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;
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
        this.timestamp = new Timestamp(randomDate());
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

    private long randomDate(){
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        Random random = new Random();
        int daysAgo = random.nextInt(7) + 1;
        int hoursAgo = random.nextInt(24);
        int minutesAgo = random.nextInt(60);
        int secondsAgo = random.nextInt(60);

        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(currentTimestamp.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, - daysAgo);
        calendar.add(Calendar.HOUR_OF_DAY, - hoursAgo);
        calendar.add(Calendar.MINUTE, - minutesAgo);
        calendar.add(Calendar.SECOND, - secondsAgo);

        return calendar.getTimeInMillis();
    }
}
