package ru.job4j.cars.listener;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.job4j.cars.model.Revision;
import ru.job4j.cars.model.User;

import javax.persistence.PrePersist;

public class RevisionListener {

    @PrePersist
    public void onCreate(Revision revision) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        var session = attr.getRequest().getSession(true);
        var user = (User) session.getAttribute("user");
        revision.setUserId(user.getId());
    }
}