package ru.job4j.cars.repository;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.File;
import ru.job4j.cars.utill.HibernateTestUtil;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FileRepositoryTest {
    private final FileRepository repository = new HiberFileRepository(HibernateTestUtil.buildSessionFactory());

    @Before
    public void before() {
        HibernateTestUtil.insertFiles();
    }

    @Test
    public void whenFindById() {
        var result = repository.findById(1).get();
        assertThat(result.getName(), is("name"));
        assertThat(result.getPath(), is("files\\df86b195-a5ec-4b99-9dbd-387ccc05585ename.png"));
    }

    @Test
    public void whenCreate() {
        var file = new File("test", "files\\test.png");
        var post = new AutoPost();
        post.setId(1L);
        post.addFile(file);
        var result = repository.create(file);
        assertThat(result.getPath(), is(file.getPath()));
        assertThat(result.getName(), is(file.getName()));
    }

    @Test
    public void whenDelete() {
        var file = repository.findById(1).get();
        repository.delete(file);
        assertThat(repository.findById(1), is(Optional.empty()));
    }
}