package ru.nsu.fit.mock;

import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.UserRepository;
import ru.nsu.fit.database.types.Role;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MockUserRepository implements UserRepository {

    private long id = 0;
    private Map<Long, User> data = new TreeMap<>();

    @Override
    public User findByLogin(String login) {
        return data.values().stream().filter(user->user.getLogin().equals(login)).findFirst().orElse(null);
    }

    @Override
    public User findById(int id) {
        return data.getOrDefault((long)id, null);
    }

    @Override
    public User[] findAllByRole(Role role) {
        List<User> users = data.values().stream().filter(user->user.getRole().equals(role)).collect(Collectors.toList());
        return users.toArray(new User[users.size()]);
    }

    @Override
    public <S extends User> S save(S s) {
        ++id;
        s.setId((int)id);
        data.put(id, s);
        return s;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public User findOne(Long aLong) {
        return data.getOrDefault(aLong, null);
    }

    @Override
    public boolean exists(Long aLong) {
        return data.containsKey(aLong);
    }

    @Override
    public Iterable<User> findAll() {
        return data.values();
    }

    @Override
    public Iterable<User> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return data.size();
    }

    @Override
    public void delete(Long aLong) {
        data.remove(aLong);
    }

    @Override
    public void delete(User user) {
        data.remove(user);
    }

    @Override
    public void delete(Iterable<? extends User> iterable) {

    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
