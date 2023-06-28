package ru.job4j.cars.service;

import org.junit.Test;
import org.mockito.Mockito;
import ru.job4j.cars.model.PriceHistory;
import ru.job4j.cars.repository.AutoPostRepository;
import ru.job4j.cars.repository.PriceHistoryRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class PriceHistoryServiceTest {
    private final PriceHistoryService service;
    private final AutoPostRepository postRepository;
    private final PriceHistoryRepository priceRepository;

    public PriceHistoryServiceTest() {
        postRepository = Mockito.mock(AutoPostRepository.class);
        priceRepository = Mockito.mock(PriceHistoryRepository.class);
        service = new SimplePriceHistoryService(priceRepository);
    }

    @Test
    public void whenCreate() {
        var priceHistory = new PriceHistory(1000L);
        when(priceRepository.save(1000L, 1L))
                .thenReturn(Optional.of(priceHistory));

        var result = service.save(1000L, 1L).get();

        assertThat(result.getPrice(), is(1000L));
        assertThat(result.getCreated().getDayOfWeek(), is(LocalDateTime.now().getDayOfWeek()));
    }

    @Test
    public void whenCreateButNotFound() {
        when(postRepository.findById(1L))
                .thenReturn(Optional.empty());
        var result = service.save(1000L, 1L);
        assertThat(result, is(Optional.empty()));
    }
}