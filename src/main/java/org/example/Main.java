package org.example;

import org.example.entity.Author;
import org.example.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;


public class Main {
    // docker exec -ti postgres_sem psql -U postgres
    public static void main(String[] args) {

        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
//            withCRUD(sessionFactory);
            sessinFactory(sessionFactory);
        }
    }

    private static void sessinFactory(SessionFactory sessionFactory){
//        try (Session session = sessionFactory.openSession()) {
//            Book book = session.find(Book.class, 1L);
//            System.out.println(book);
//        }

        try (Session session = sessionFactory.openSession()) {
            Author author = session.find(Author.class, 2L);
            System.out.println(author);

            for (Book book : author.getBooks()) {
                System.out.println(book);
            }
        }

    }


    private static void withCRUD(SessionFactory sessionFactory) {
        try (Session session = sessionFactory.openSession()) {

            Author author = session.find(Author.class, 1L); // select
            System.out.println("1: " + author);
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction t = session.beginTransaction();

            Author author = new Author();
            author.setId(2L);
            author.setName("Auth2");

            session.persist(author); // insert
            t.commit();
        }


        try (Session session = sessionFactory.openSession()) {

            Author author = session.find(Author.class, 2L);
            author.setName("UPDATED");

            Transaction t = session.beginTransaction();
            session.merge(author); // update
            t.commit();

        }

        try (Session session = sessionFactory.openSession()) {
            Author author = session.find(Author.class, 1L);

            Transaction t = session.beginTransaction();
            session.remove(author); // delete
            t.commit();

        }
    }
}