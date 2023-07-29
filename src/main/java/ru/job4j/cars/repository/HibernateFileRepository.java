package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.File;

import javax.persistence.LockModeType;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class HibernateFileRepository implements FileRepository {
    private final SessionFactory sf;

    @Override
    public Optional<File> findById(long id) {
        Transaction tr = null;
        File result;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            result = session.find(File.class, id);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при поиске файла по id: {}", id);
            throw e;
        }
        return Optional.ofNullable(result);
    }

    @Override
    public File create(File file) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            var post = session.find(AutoPost.class, file.getPost().getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
            post.addFile(file);
            session.persist(file);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при сохранении файла: {}", file);
            throw e;
        }
        return file;
    }

    @Override
    public void delete(File file) {
        Transaction tr = null;
        try (var session = sf.openSession()) {
            tr = session.beginTransaction();
            session.delete(file);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) {
                tr.rollback();
            }
            log.error("Ошибка при удалении файла: {}", file);
            throw e;
        }
    }
}