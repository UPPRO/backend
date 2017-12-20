package ru.nsu.fit.mock;

import ru.nsu.fit.database.entities.File;
import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.entities.Token;
import ru.nsu.fit.database.entities.User;
import ru.nsu.fit.database.repositories.FileRepository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MockFileRepository implements FileRepository {

    private long id = 0;
    private Map<Long, File> data = new TreeMap<>();

    @Override
    public File findById(int id) {
        return data.values().stream().filter(file -> file.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public File[] findAllByParentFolder(Folder parentFolder) {
        List<File> files = data.values().stream().filter(file->file.getParentFolder().getId().equals(parentFolder.getId())).collect(Collectors.toList());
        return files.toArray(new File[files.size()]);
    }

    @Override
    public <S extends File> S save(S s) {
        if(s.getId() != null && s.getId() > 0){
            data.put((long)s.getId(), s);
            return s;
        }

        ++id;
        data.put(id, s);
        s.setId((int)id);
        return s;
    }

    @Override
    public <S extends File> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public File findOne(Long aLong) {
        return data.getOrDefault(aLong, null);
    }

    @Override
    public boolean exists(Long aLong) {
        return data.containsKey(aLong);
    }

    @Override
    public Iterable<File> findAll() {
        return data.values();
    }

    @Override
    public Iterable<File> findAll(Iterable<Long> iterable) {
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
    public void delete(File file) {
        Map.Entry<Long, File> e = data.entrySet().stream().filter(entry->entry.getValue().getId().equals(file.getId())).findFirst().orElse(null);
        if(e != null){
            data.remove(e.getKey());
        }
    }

    @Override
    public void delete(Iterable<? extends File> iterable) {

    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
