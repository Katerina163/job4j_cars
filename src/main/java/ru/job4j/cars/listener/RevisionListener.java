package ru.job4j.cars.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.job4j.cars.model.Revision;
import ru.job4j.cars.model.User;

import javax.persistence.PrePersist;

@Slf4j
public class RevisionListener {

    @PrePersist
    public void onCreate(Revision revision) {
        User user = null;
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            var session = attr.getRequest().getSession(true);
            user = (User) session.getAttribute("user");
        } catch (IllegalStateException ise) {
            log.error("Ошибка при сохранении {}", revision);
        }
        if (user == null) {
            user = new User();
            user.setId(0L);
            log.info("При сохранении {} пользователь null", revision);
        }
        revision.setUserId(user.getId());
    }
}