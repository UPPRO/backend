package ru.nsu.fit.mock;

import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.repositories.FolderRepository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MockFolderRepository implements FolderRepository {

    private long id = 0;
    private Map<Long, Folder> data = new TreeMap<>();

    @Override
    public Folder findById(int id) {
        return data.values().stream().filter(file -> file.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Folder findByRoot(boolean root) {
        return data.values().stream().filter(Folder::isRoot).findFirst().orElse(null);
    }

    @Override
    public Folder findByNameAndAndParentFolder(String name, Folder parentFolder) {
        return data.values().stream().filter(file -> file.getName().equals(name) && file.getParentFolder() != null && file.getParentFolder().getId().equals(parentFolder.getId())).findFirst().orElse(null);
    }

    @Override
    public Folder[] findAllByParentFolder(Folder parentFolder) {
        List<Folder> folders = data.values().stream().filter(folder -> folder.getParentFolder() != null && folder.getParentFolder().getId().equals(parentFolder.getId())).collect(Collectors.toList());
        return folders.toArray(new Folder[folders.size()]);
    }

    @Override
    public <S extends Folder> S save(S s) {
        ++id;
        data.put(id, s);
        s.setId((int)id);
        return s;
    }

    @Override
    public <S extends Folder> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Folder findOne(Long aLong) {
        return data.getOrDefault(aLong, null);
    }

    @Override
    public boolean exists(Long aLong) {
        return data.containsKey(aLong);
    }

    @Override
    public Iterable<Folder> findAll() {
        return data.values();
    }

    @Override
    public Iterable<Folder> findAll(Iterable<Long> iterable) {
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
    public void delete(Folder folder) {
        Map.Entry<Long, Folder> e = data.entrySet().stream().filter(entry -> entry.getValue().getId().equals(folder.getId())).findFirst().orElse(null);
        if (e != null) {
            data.remove(e.getKey());
        }
    }

    @Override
    public void delete(Iterable<? extends Folder> iterable) {

    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
