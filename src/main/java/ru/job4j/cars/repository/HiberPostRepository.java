package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class HiberPostRepository implements PostRepository {
    private final SessionFactory sf;
    private CrudRepository crudRepository;

    @Override
    public Collection<AutoPost> findAllNew() {
        return crudRepository.query(
                getAutoPostQuery("where ap.created between :fStart and :fEnd order by ap.created"),
                AutoPost.class, Map.of(
                        "fStart", Date.valueOf(LocalDate.now().minusDays(1L)),
                        "fEnd", Date.valueOf(LocalDate.now())));
    }

    private String getAutoPostQuery(String condition) {
        return "select distinct ap from AutoPost ap left join fetch ap.priceHistories"
                + " left join fetch ap.car as car left join fetch ap.files "
                + condition;
    }

    @Override
    public Collection<AutoPost> findAll() {
        return crudRepository.query(
                getAutoPostQuery(""), AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findWithFile() {
        return crudRepository.query(
                getAutoPostQuery("where size(ap.files) >= 1"), AutoPost.class);
    }

    @Override
    public Collection<AutoPost> findCarBrand(String brand) {
        return crudRepository.query(
                getAutoPostQuery("where car.name = :fBrand"),
                AutoPost.class, Map.of("fBrand", brand));
    }

    @Override
    public Collection<AutoPost> findUsersCar(String login) {
        return crudRepository.query(
                getAutoPostQuery("where ap.user.login = :fUser"),
                AutoPost.class, Map.of("fUser", login));
    }

    @Override
    public void add(AutoPost post) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            session.save(post.getCar().getEngine());
            session.save(post.getCar());
            for (var owner : post.getCar().getOwners()) {
                session.save(owner);
            }
            session.save(post);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void delete(AutoPost post) {
        crudRepository.run(session -> session.delete(post));
    }

    @Override
    public void deleteById(int id) {
        Transaction tx = null;
        try (Session session = sf.openSession()) {
            tx = session.beginTransaction();
            session.createQuery("delete from File where postId = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            var post = new AutoPost();
            post.setId(id);
            session.delete(post);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public Optional<AutoPost> findById(int id) {
        String s = " left join fetch car.owners left join fetch ap.user"
                + " left join fetch car.engine where ap.id = :fId";
        return crudRepository.optional(
                getAutoPostQuery(s),
                AutoPost.class, Map.of("fId", id));
    }

    @Override
    public void update(AutoPost post) {
        crudRepository.run(session -> session.merge(post));
    }

    @Override
    public void soldById(int id, boolean sold) {
        crudRepository.run(
                "update AutoPost set sold = :fSold where id = :fId",
                Map.of("fSold", sold, "fId", id));
    }
}
