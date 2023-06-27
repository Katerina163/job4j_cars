package ru.job4j.cars.service;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.Banner;
import ru.job4j.cars.dto.Criterion;
import ru.job4j.cars.dto.QPredicate;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.job4j.cars.model.QAutoPost.autoPost;

public class AutoPostServiceTest {
    private final SimpleAutoPostService service;
    private final AutoPostRepository postRepository;
    private final CarRepository carRepository;
    private final String storageDirectory;
    private final MarkRepository markRepository;
    private final UserRepository userRepository;
    private AutoPost post;

    public AutoPostServiceTest() {
        postRepository = Mockito.mock(HiberAutoPostRepository.class);
        carRepository = Mockito.mock(HiberCarRepository.class);
        storageDirectory = "files";
        markRepository = Mockito.mock(HiberMarkRepository.class);
        userRepository = Mockito.mock(HiberUserRepository.class);
        service = new SimpleAutoPostService(
                postRepository, carRepository, storageDirectory, markRepository, userRepository
        );
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
    }

    @Test
    public void whenFindAll() {
        Collection<AutoPost> collection = new HashSet<>();
        collection.add(post);
        when(postRepository.findWithPredicate(
                QPredicate.builder()
                        .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().findAll());
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindWithFile() {
        Collection<AutoPost> collection = new HashSet<>();
        collection.add(post);
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(1, autoPost.files.size()::goe)
                .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().withFile());
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Ignore
    @Test
    public void whenFindAllNew() {
        Collection<AutoPost> collection = new ArrayList<>();
        collection.add(post);
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addBiPredicate(LocalDateTime.now().minusDays(1L), LocalDateTime.now(), autoPost.created::between)
                .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().fresh());
        assertThat(result.size(), is(1));
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByCarBrand() {
        Collection<AutoPost> collection = new HashSet<>();
        collection.add(post);
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate("name", autoPost.car.name::eq)
                .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().addBrand("name"));
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByColor() {
        Collection<AutoPost> collection = new HashSet<>();
        collection.add(post);
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(Color.BLACK, autoPost.car.color::eq)
                .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().addColor(Color.BLACK));
        Banner banner = null;
        for (var p : result) {
            banner = p;
        }
        checkPost(banner);
    }

    @Test
    public void whenFindByMark() {
        Collection<AutoPost> collection = new HashSet<>();
        collection.add(post);
        when(postRepository.findWithPredicate(QPredicate.builder()
                .addPredicate(2L, autoPost.car.mark.id::eq)
                .and()))
                .thenReturn(collection);
        var result = service.search(new Criterion().addMarkId(2L));
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

    @Ignore
    @Test
    public void whenSaveAndFindById() throws IOException {
        var file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("name");
        when(file.getBytes()).thenReturn(new byte[]{1});
        when(markRepository.findById(1))
                .thenReturn(Optional.of(new Mark("mark")));
        when(userRepository.findByLogin("login"))
                .thenReturn(Optional.of(new User("login", "root")));

        var map = Map.of("description", "description",
                "price", "100",
                "car.name", "name",
                "color", "RED",
                "mark.id", "1",
                "owners", "owners");

        service.save("login", map, file);

        ArgumentCaptor<AutoPost> post = ArgumentCaptor.forClass(AutoPost.class);
        verify(postRepository).cud(post.capture(), session -> session.persist(post));
        assertThat(post.getValue().getDescription(), is(map.get("description")));
        assertThat(post.getValue().getHistory().first().getPrice(), is(100L));
        assertThat(post.getValue().getCar().getName(), is(map.get("car.name")));
        assertThat(post.getValue().getCar().getColor(), is(Color.RED));
        assertThat(post.getValue().getCar().getMark().getName(), is("mark"));
        assertThat(post.getValue().getCar().getOwners(), is(map.get("owners")));
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