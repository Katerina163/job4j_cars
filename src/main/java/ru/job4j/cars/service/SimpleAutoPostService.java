package ru.job4j.cars.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.dto.*;
import ru.job4j.cars.mapper.Mapper;
import ru.job4j.cars.model.AutoPost;
import ru.job4j.cars.model.File;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.UserRepository;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import static ru.job4j.cars.model.QAutoPost.autoPost;

@Slf4j
@Service
public class SimpleAutoPostService implements AutoPostService {
    private final AutoPostRepository repository;
    private final String storageDirectory;
    private final UserRepository userRepository;
    private final Mapper<Tuple, Banner> mapper;
    private final Mapper<PostCreateDTO, AutoPost> mapperCreate;
    private final Mapper<PostModifyDTO, AutoPost> mapperModify;
    private final ValidatorFactory factory;

    public SimpleAutoPostService(AutoPostRepository hiberAutoPostRepository,
                                 @Value("${file.directory}") String storageDirectory,
                                 UserRepository hiberUserRepository,
                                 Mapper<Tuple, Banner> tupleBannerMapper,
                                 Mapper<PostCreateDTO, AutoPost> postCreateDTOAutoPostMapper,
                                 Mapper<PostModifyDTO, AutoPost> postModifyDTOAutoPostMapper) {
        repository = hiberAutoPostRepository;
        this.storageDirectory = storageDirectory;
        userRepository = hiberUserRepository;
        mapper = tupleBannerMapper;
        mapperCreate = postCreateDTOAutoPostMapper;
        mapperModify = postModifyDTOAutoPostMapper;
        factory = Validation.buildDefaultValidatorFactory();
    }

    @Override
    public Collection<Banner> search(Criterion criterion, Function<QPredicate, Predicate> function) {
        var predicate = QPredicate.builder();
        if (criterion.isFindAll()) {
            return repository.findWithPredicate(predicate.and(), criterion.getLimit(), criterion.getOffset())
                    .stream().map(mapper::convert).toList();
        }
        if (criterion.isWithFile()) {
            predicate.addPredicate(1, autoPost.files.size()::goe);
        }
        if (criterion.isFresh()) {
            predicate.addBiPredicate(LocalDateTime.now().minusDays(1L), LocalDateTime.now(), autoPost.created::between);
        }
        if (criterion.getBrand() != null && !criterion.getBrand().isEmpty()) {
            predicate.addPredicate(criterion.getBrand(), autoPost.car.name::eq);
        }
        if (!criterion.getColors().isEmpty()) {
            for (var color : criterion.getColors()) {
                if (color == null) {
                    break;
                }
                predicate.addPredicate(color, autoPost.car.color::eq);
            }
        }
        if (!criterion.getMarkIds().isEmpty()) {
            for (var markId : criterion.getMarkIds()) {
                if (markId == null || markId == 0) {
                    break;
                }
                predicate.addPredicate(markId, autoPost.car.mark.id::eq);
            }
        }
        return repository.findWithPredicate(
                        function.apply(predicate), criterion.getLimit(), criterion.getOffset())
                .stream().map(mapper::convert).toList();
    }

    @Override
    public Optional<AutoPost> findById(long id) {
        return repository.findById(id);
    }

    @Override
    public void soldById(long postId, boolean sold) {
        repository.soldById(postId, sold);
    }

    @Transactional
    @Override
    public void delete(long id) {
        var post = repository.findById(id);
        if (post.isPresent()) {
            var set = post.get().getFiles();
            for (var file : set) {
                deleteFile(file.getPath());
            }
            repository.cud(post.get(), session -> session.delete(post.get()));
        }
    }

    private void deleteFile(String path) {
        try {
            Files.deleteIfExists(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public void save(String login, PostCreateDTO dto, MultipartFile file) {
        validation(dto);
        var post = mapperCreate.convert(dto);
        if (!file.isEmpty()) {
            var f = convertFile(file);
            post.addFile(f);
        }
        var user = userRepository.findByLogin(login);
        if (user.isPresent()) {
            user.get().addUserPost(post);
            repository.cud(post, session -> session.persist(post));
        }
    }

    private File convertFile(MultipartFile file) {
        var path = getNewFilePath(file.getOriginalFilename());
        try {
            Files.write(Path.of(path), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new File(file.getOriginalFilename(), path);
    }

    private String getNewFilePath(String sourceName) {
        return storageDirectory + java.io.File.separator + UUID.randomUUID() + sourceName;
    }

    @Transactional
    @Override
    public void modify(PostModifyDTO dto) {
        validation(dto);
        var post = mapperModify.convert(dto);
        repository.cud(post, session -> session.update(post));
    }

    private void validation(Object dto) {
        var validationResult = factory.getValidator().validate(dto);
        if (!validationResult.isEmpty()) {
            for (var error : validationResult) {
                log.error(error.getMessage());
            }
            throw new ConstraintViolationException(validationResult);
        }
    }
}
