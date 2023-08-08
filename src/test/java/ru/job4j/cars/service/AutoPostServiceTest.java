package ru.job4j.cars.service;

import com.querydsl.core.Tuple;
import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.mapper.CompositeMapper;
import ru.job4j.cars.mapper.TupleBannerMapper;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.HibernateAutoPostRepository;
import ru.job4j.cars.repository.HibernateUserRepository;
import ru.job4j.cars.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static ru.job4j.cars.model.QAutoPost.autoPost;

public class AutoPostServiceTest {
    private final SimpleAutoPostService service;
    private final AutoPostRepository postRepository;
    private final AutoPost post;
    private final Collection<Tuple> collection;

    public AutoPostServiceTest() {
        postRepository = Mockito.mock(HibernateAutoPostRepository.class);
        String storageDirectory = "files";
        UserRepository userRepository = Mockito.mock(HibernateUserRepository.class);
        CompositeMapper mapper = Mockito.mock(CompositeMapper.class);
        service = new SimpleAutoPostService(
                postRepository, storageDirectory, userRepository, mapper);
        post = AutoPost.builder()
                .description("description")
                .created(LocalDateTime.now())
                .sold(false)
                .author(new User("login", "root"))
                .car(new Car("name", "owners", Color.BLACK, new Mark("mark")))
                .build();
        post.setId(1L);
        post.setHistory(new TreeSet<>());
        post.setFiles(new TreeSet<>());
        post.addPriceHistory(new PriceHistory(900L));
        var file = new File("file", "path");
        file.setId(2L);
        post.addFile(file);

        var ban = Banner.builder()
                .postId(1L)
                .carName(post.getCar().getName())
                .markName(post.getCar().getMark().getName())
                .price(post.getHistory().last().getPrice())
                .created(post.getCreated())
                .fileId(post.getFiles().last().getId())
                .build();

        Tuple tuple = Mockito.mock(Tuple.class);
        var tbmapper = Mockito.mock(TupleBannerMapper.class);
        when(mapper.mapper(Tuple.class, Banner.class))
                .thenReturn(tbmapper);
        when(tbmapper.convert(tuple))
                .thenReturn(ban);
        collection = new HashSet<>();
        collection.add(tuple);
    }

    @Test
    public void whenFindAll() {
        when(postRepository.findWithPredicate(
                QPredicate.builder()
                        .and(), 10L, 0L))
                .thenReturn(collection);
        var result = service.search(new Criterion().findAll(), QPredicate::and);
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindWithFile() {
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(1, autoPost.files.size()::goe)
                .and(), 10L, 0L))
                .thenReturn(collection);
        var result = service.search(new Criterion().withFile(), QPredicate::and);
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByCarBrand() {
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate("name", autoPost.car.name::eq)
                .and(), 10L, 0L))
                .thenReturn(collection);
        var result = service.search(new Criterion().addBrand("name"), QPredicate::and);
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByColor() {
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(Color.BLACK, autoPost.car.color::eq)
                .and(), 10L, 0L))
                .thenReturn(collection);
        var result = service.search(new Criterion().addColor(Color.BLACK), QPredicate::and);
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByMark() {
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(2L, autoPost.car.mark.id::eq)
                .and(), 10L, 0L))
                .thenReturn(collection);
        var result = service.search(new Criterion().addMarkIds(2L), QPredicate::and);
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindById() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        var result = service.findById(1L);
        assertThat(result, is(Optional.of(post)));
    }

    private void checkPost(Banner banner) {
        assertThat(banner.getPostId(), is(post.getId()));
        assertThat(banner.getCarName(), is(post.getCar().getName()));
        assertThat(banner.getMarkName(), is(post.getCar().getMark().getName()));
        assertThat(banner.getPrice(), is(post.getHistory().last().getPrice()));
        assertThat(banner.getCreated(), is(post.getCreated()));
        assertThat(banner.getFileId(), is(post.getFiles().last().getId()));
    }
}