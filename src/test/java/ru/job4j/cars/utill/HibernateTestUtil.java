package ru.job4j.cars.utill;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@UtilityClass
public class HibernateTestUtil {
    private final static SessionFactory sf = buildSessionFactory();

    public static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();
        return configuration.buildSessionFactory();
    }

    public static Configuration buildConfiguration() {
        return new Configuration();
    }

    public static void insertUsers() {
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            s.save(new User("Ivanov", "root"));
            s.save(new User("Petrov", "root"));
            s.save(new User("Sidorov", "root"));
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении user");
            throw e;
        }
    }

    public static void insertMarks() {
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            s.save(new Mark("Fiat"));
            s.save(new Mark("Lamborghini"));
            s.save(new Mark("Audi"));
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении marks");
            throw e;
        }
    }

    public static void insertCars() {
        insertMarks();
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            helpToInsertCar(s, "Fiat", "Иван Иванов", "500", Color.GREEN);
            helpToInsertCar(s, "Lamborghini", "Александра Александровна", "Huracán Evo", Color.RED);
            helpToInsertCar(s, "Audi", "Андрей Андреевич, Василий Васильевич", "RS 5", Color.YELLOW);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении cars");
            throw e;
        }
    }

    public static void insertPosts() {
        insertUsers();
        insertCars();
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            helpToInsertPost(s, Map.of("login", "Ivanov", "carName", "500", "description", "Fiat description"),
                    LocalDateTime.now(), false);
            helpToInsertPost(s, Map.of("login", "Petrov", "carName", "Huracán Evo", "description", "Lamborghini description"),
                    LocalDateTime.now().minusDays(10), false);
            helpToInsertPost(s, Map.of("login", "Petrov", "carName", "RS 5", "description", "Audi description"),
                    LocalDateTime.now().minusHours(10), true);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении posts");
            throw e;
        }
    }

    public static void insertParticipates() {
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            helpToInsertParticipates(s, "Petrov", "Fiat description");
            helpToInsertParticipates(s, "Sidorov", "Fiat description");
            helpToInsertParticipates(s, "Sidorov", "Lamborghini description");
            helpToInsertParticipates(s, "Ivanov", "Audi description");
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении participates");
            throw e;
        }
    }

    public static void insertPrice() {
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();

            helpToInsertPrice(s, "Fiat description", 450000L);
            helpToInsertPrice(s, "Lamborghini description", 16500000L);
            helpToInsertPrice(s, "Audi description", 4200000L);
            helpToInsertPrice(s, "Audi description", 5120000L);

            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении price history");
            throw e;
        }
    }

    public static void insertFiles() {
        Transaction tr = null;
        try (var s = sf.openSession()) {
            tr = s.beginTransaction();
            var post = s.createQuery("from AutoPost where description = :description", AutoPost.class)
                    .setParameter("description", "Audi description").uniqueResult();
            var file = new File("name", "files\\df86b195-a5ec-4b99-9dbd-387ccc05585ename.png");
            post.addFile(file);
            s.save(file);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при добавлении files");
            throw e;
        }
    }

    private void helpToInsertCar(Session s, String mark, String owners, String carName, Color color) {
        var markCar = s.createQuery("from Mark where name = :name", Mark.class)
                .setParameter("name", mark).uniqueResult();
        s.save(new Car(carName, owners, color, markCar));
    }

    private void helpToInsertPost(Session s, Map<String, String> map, LocalDateTime date, boolean isSold) {
        var user = s.createQuery("from User where login = :login", User.class)
                .setParameter("login", map.get("login")).uniqueResult();
        var car = s.createQuery("from Car where name = :name", Car.class)
                .setParameter("name", map.get("carName")).uniqueResult();
        s.save(AutoPost.builder()
                .description(map.get("description"))
                .created(date)
                .sold(isSold)
                .author(user)
                .car(car)
                .build());
    }

    private void helpToInsertParticipates(Session s, String login, String description) {
        var user = s.createQuery("from User where login = :login", User.class)
                .setParameter("login", login).uniqueResult();
        var post = s.createQuery("from AutoPost where description = :description", AutoPost.class)
                .setParameter("description", description).uniqueResult();
        user.addParticipates(post);
    }

    private void helpToInsertPrice(Session s, String description, long price) {
        var post = s.createQuery("from AutoPost where description = :description", AutoPost.class)
                .setParameter("description", description).uniqueResult();
        var priceHistory = new PriceHistory(price);
        post.addPriceHistory(priceHistory);
        s.save(priceHistory);
    }
}