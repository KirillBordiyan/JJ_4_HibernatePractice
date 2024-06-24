package org.example.hw;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.annotations.HQLSelect;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.annotations.processing.SQL;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Homework {
    /**
     * docker run --name social-hw -p 5555:5432 -e POSTGRES_USER=admin -e POSTGRES_PASSWORD=1234 -e POSTGRES_DB=social -d postgres
     * docker exec -ti social-hw psql social -U admin
     * <p>
     * <p>
     * Используя hibernate, создать таблицы:
     * 1. Post (публикация) (id, title)
     * 2. PostComment (комментарий к публикации) (id, text, post_id)
     * Написать стандартные CRUD-методы: создание, загрузка, удаление.
     * <p>
     * Доп. задания:
     * 1. * В сущностях post и postComment добавить поля timestamp с датами.
     * 2. * Создать таблицу users(id, name) и в сущностях post и postComment добавить ссылку на юзера.
     * 3. * Реализовать методы:
     * 3.1 Загрузить все комментарии публикации
     * 3.2 Загрузить все публикации по идентификатору юзера
     * 3.3 ** Загрузить все комментарии по идентификатору юзера
     * 3.4 **** По идентификатору юзера загрузить юзеров, под чьими публикациями он оставлял комменты.
     * // userId -> List<User>
     */
    public static void main(String[] args) {
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {

            firstInput(sessionFactory);

            //min
//            createPost(sessionFactory);
//            createComment(sessionFactory);
//            listPrinter(selectAllPosts(sessionFactory));
//            listPrinter(selectAllComments(sessionFactory));
//            deletePost(sessionFactory);
//            deleteComment(sessionFactory);

            //dop



        }
    }

    private static boolean deleteComment(SessionFactory sessionFactory){
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Input id to delete comment: ");
            Long id = Long.parseLong(scanner.nextLine());
            PostComment postComment = session.find(PostComment.class, id);

            if(postComment == null){
                throw new NullPointerException("post_id=" + id);
            }

            session.beginTransaction();
            session.remove(postComment);
            session.getTransaction().commit();

            return true;
        } catch (NullPointerException e){
            System.out.println("No ant match by " + e.getMessage());
            return false;
        }
    }

    private static boolean deletePost(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Input id to delete post: ");
            Long id = Long.parseLong(scanner.nextLine());
            Post post = session.find(Post.class, id);

            if(post == null){
                throw new NullPointerException("id=" + id);
            }

            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();

            return true;
        } catch (NullPointerException e){
            System.out.println("No ant match by " + e.getMessage());
            return false;
        }
    }

    private static List<PostComment> selectAllComments(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    criteriaMaker(session, PostComment.class)
            ).getResultList();
        }
    }

    private static List<Post> selectAllPosts(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    criteriaMaker(session, Post.class)
            ).getResultList();
        }
    }

    private static void createComment(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            Transaction transaction = session.beginTransaction();
            System.out.println("Input comment text: ");
            String text = scanner.nextLine();
            System.out.println("Input post id: ");
            Long id = Long.parseLong(scanner.nextLine());

            Post post = session.find(Post.class, id);
            PostComment comment = new PostComment(text, post);

            post.addComment(comment);
            session.merge(post);

            transaction.commit();
        }
    }

    private static void createPost(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            Transaction transaction = session.beginTransaction();
            System.out.println("Input post title: ");
            session.persist(new Post(scanner.nextLine()));
            transaction.commit();
        }
    }

    private static void EXAMPLE(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tr = session.beginTransaction();
            Post post = new Post();
            post.setTitle("title 1");
            session.persist(post);

            PostComment pc = new PostComment();
            pc.setText("text comment 1");
            pc.setPost(post);

            session.persist(pc);

            System.out.println(post);
            System.out.println(pc);
            tr.commit();
        }
    }

    private static void firstInput(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();

            for (int i = 1; i <= 10; i++) {
                Post post = new Post("Title of post #" + i);
                session.persist(post);
            }

            for (int i = 1; i <= 10; i++) {
                Post post = session.find(
                        Post.class,
                        ThreadLocalRandom.current().nextInt(1, 11));

                PostComment pc = new PostComment("Text of comment #" + i, post);
                post.addComment(pc);

                session.merge(post);
            }

            transaction.commit();
        }
    }

    private static void listPrinter(List<?> list) {
        list.forEach(System.out::println);
    }

    private static <T> CriteriaQuery<T> criteriaMaker(Session session, Class<T> resultClass) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(resultClass);
        criteria.from(resultClass);
        return criteria;
    }
}
