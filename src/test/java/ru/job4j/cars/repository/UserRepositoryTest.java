package ru.job4j.cars.repository;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.User;
import ru.job4j.cars.utill.HibernateTestUtil;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class UserRepositoryTest {
    private final UserRepository repository;
    private final
    AutoPostRepository postRepository;

    public UserRepositoryTest() {
        var sf = HibernateTestUtil.buildSessionFactory();
        repository = new HiberUserRepository(sf);
        postRepository = new HiberAutoPostRepository(sf);
    }

    @Before
    public void before() {
        HibernateTestUtil.insertPosts();
    }

    @Test
    public void whenFindByLogin() {
        HibernateTestUtil.insertParticipates();
        HibernateTestUtil.insertPrice();
        HibernateTestUtil.insertFiles();
        User result = repository.findByLogin("Ivanov").get();
        assertThat(result.getLogin(), is("Ivanov"));
        assertThat(result.getPassword(), is("root"));
        assertThat(result.getParticipates().size(), is(1));
        assertThat(result.getUserPosts().size(), is(1));
        for (var post : result.getUserPosts()) {
            assertThat(post.getDescription(), is("Fiat description"));
            assertThat(post.getHistory().size(), is(1));
        }
        for (var post : result.getParticipates()) {
            assertThat(post.getDescription(), is("Audi description"));
            assertThat(post.getHistory().size(), is(2));
            assertThat(post.getFiles().size(), is(1));
        }
    }

    @Test
    public void whenFindByLoginAndPassword() {
        HibernateTestUtil.insertParticipates();
        User result = repository.findByLoginAndPassword("Ivanov", "root").get();
        assertThat(result.getLogin(), is("Ivanov"));
        assertThat(result.getPassword(), is("root"));
    }

    @Test
    public void whenCreateAndFindByLogin() {
        User user = new User("login", "password");
        repository.create(user);
        User result = repository.findByLogin(user.getLogin()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenCreateAndFindByLoginAndPassword() {
        User user = new User("login", "password");
        repository.create(user);
        User result = repository.findByLoginAndPassword(user.getLogin(), user.getPassword()).get();
        assertThat(result.getLogin(), is("login"));
        assertThat(result.getPassword(), is("password"));
    }

    @Test
    public void whenUserSubscribe() {
        var user = new User("login", "password");
        user = repository.create(user);
        var post = postRepository.findById(1).get();
        repository.participate(user.getId(), post.getId(), Set::add);
        var result = repository.findByLogin("login").get();
        assertThat(result.getParticipates().size(), is(1));
    }
}