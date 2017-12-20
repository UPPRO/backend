package ru.nsu.fit.mock;

import ru.nsu.fit.database.entities.Token;
import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.TokenRepository;

import java.util.Map;
import java.util.TreeMap;

public class MockTokenRepository implements TokenRepository {

    private long id = 0;
    private Map<Long, Token> data = new TreeMap<>();

    @Override
    public Token findByData(String data) {
        return this.data.values().stream().filter(token->token.getData().equals(data)).findFirst().orElse(null);
    }

    @Override
    public <S extends Token> S save(S s) {
        ++id;
        data.put(id, s);
        return s;
    }

    @Override
    public <S extends Token> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Token findOne(Long aLong) {
        return data.getOrDefault(aLong, null);
    }

    @Override
    public boolean exists(Long aLong) {
        return data.containsKey(aLong);
    }

    @Override
    public Iterable<Token> findAll() {
        return data.values();
    }

    @Override
    public Iterable<Token> findAll(Iterable<Long> iterable) {
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
    public void delete(Token token) {
        Map.Entry<Long, Token> e = data.entrySet().stream().filter(entry->entry.getValue().getData().equals(token.getData())).findFirst().orElse(null);
        if(e != null){
            data.remove(e.getKey());
        }
    }

    @Override
    public void delete(Iterable<? extends Token> iterable) {

    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
