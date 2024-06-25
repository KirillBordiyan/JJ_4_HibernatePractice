package org.example.hw;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Homework {
    /**

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
//            System.out.println("delete post: " + deletePost(sessionFactory));
//            System.out.println("delete comment: " + deleteComment(sessionFactory));

            //opt
//            listPrinter(loadAllCommentsByPostId(sessionFactory));
//            listPrinter(loadAllPostByUserId(sessionFactory));
//            listPrinter(loadAllCommentByUserId(sessionFactory));
//            listPrinter(loadAllUsersByCommentatorId(sessionFactory));
        }
    }

    //opt
    private static List<User> loadAllUsersByCommentatorId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Input user_id to find other users whose posts he commented on: ");
            Long user_id = Long.parseLong(scanner.nextLine());

            User user = session.find(User.class, user_id);

            List<User> lookingFor = new ArrayList<>();

            user.getPostCommentList()
                    .forEach(el -> {
                        User to = session.find(
                                el.getUser().getClass(),
                                el.getPost().getUser().getId());
                        lookingFor.add(to);
                    });

            return lookingFor;
        }
    }

    private static List<PostComment> loadAllCommentByUserId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Input user_id to find user's comments : ");
            Long user_id = Long.parseLong(scanner.nextLine());

            return session.find(User.class, user_id).getPostCommentList();
        }
    }

    private static List<Post> loadAllPostByUserId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Input user_id to find posts: ");
            Long user_id = Long.parseLong(scanner.nextLine());

            return session.find(User.class, user_id).getPostList();
        }
    }

    private static List<PostComment> loadAllCommentsByPostId(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Input post_id to find comment: ");
            Long post_id = Long.parseLong(scanner.nextLine());

            return session.find(Post.class, post_id).getComments();
        }
    }


    //min
    private static boolean deleteComment(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Input id to delete comment: ");
            Long id = Long.parseLong(scanner.nextLine());
            PostComment postComment = session.find(PostComment.class, id);

            if (postComment == null) {
                throw new NullPointerException("post_id=" + id);
            }

            session.beginTransaction();
            session.remove(postComment);
            session.getTransaction().commit();

            return true;
        } catch (NullPointerException e) {
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

            if (post == null) {
                throw new NullPointerException("id=" + id);
            }

            session.beginTransaction();
            session.remove(post);
            session.getTransaction().commit();

            return true;
        } catch (NullPointerException e) {
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
            System.out.println("Input user id (which write comment): ");
            Long user_id = Long.parseLong(scanner.nextLine());
            System.out.println("Input comment text: ");
            String text = scanner.nextLine();
            System.out.println("Input post id: ");
            Long post_id = Long.parseLong(scanner.nextLine());

            User user = session.find(User.class, user_id);
            Post post = session.find(Post.class, post_id);

            PostComment comment = new PostComment(user, text, post);

            user.addComment(comment);
            post.addComment(comment);
            session.merge(user);

            transaction.commit();
        }
    }

    private static void createPost(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession();
             Scanner scanner = new Scanner(System.in)
        ) {
            Transaction transaction = session.beginTransaction();

            System.out.println("Input user id (which write post): ");
            Long user_id = Long.parseLong(scanner.nextLine());

            User user = session.find(User.class, user_id);
            System.out.println("Input post title: ");

            Post post = new Post(user, scanner.nextLine());

            user.addPost(post);
            session.merge(user);
            transaction.commit();
        }
    }

    private static void firstInput(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {

            Transaction transaction = session.beginTransaction();

            for (int i = 1; i <= 5; i++) {
                User user = new User("Name user #" + i);
                session.persist(user);
            }

            for (int i = 1; i <= 10; i++) {

                User user = session.find(
                        User.class,
                        ThreadLocalRandom.current().nextInt(1, 6)
                );
                Post post = new Post(user, "Title of post #" + i);

                user.addPost(post);
                session.merge(user);
            }

            for (int i = 1; i <= 20; i++) {
                Post post = session.find(
                        Post.class,
                        ThreadLocalRandom.current().nextInt(1, 11));

                User user = session.find(
                        User.class,
                        ThreadLocalRandom.current().nextInt(1, 6));

                PostComment pc = new PostComment(user, "Text of comment #" + i, post);

                user.addComment(pc);
                post.addComment(pc);

                session.merge(user);
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
