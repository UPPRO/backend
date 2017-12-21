package ru.nsu.fit.mock;

import ru.nsu.fit.database.entities.File;
import ru.nsu.fit.database.entities.FileData;
import ru.nsu.fit.database.entities.Folder;
import ru.nsu.fit.database.repositories.FileDataRepository;
import ru.nsu.fit.database.repositories.FileRepository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class MockFileDataRepository implements FileDataRepository {

    private long id = 0;
    private Map<Long, FileData> data = new TreeMap<>();

    @Override
    public FileData findByFile(File file) {
        return data.values().stream().filter(fileData -> fileData.getFile().getId().equals(file.getId())).findFirst().orElse(null);
    }

    @Override
    public <S extends FileData> S save(S s) {
        ++id;
        data.put(id, s);
        return s;
    }

    @Override
    public <S extends FileData> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public FileData findOne(Long aLong) {
        return data.getOrDefault(aLong, null);
    }

    @Override
    public boolean exists(Long aLong) {
        return data.containsKey(aLong);
    }

    @Override
    public Iterable<FileData> findAll() {
        return data.values();
    }

    @Override
    public Iterable<FileData> findAll(Iterable<Long> iterable) {
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
    public void delete(FileData fileData) {
        Map.Entry<Long, FileData> e = data.entrySet().stream().filter(entry->entry.getValue().getId().equals(fileData.getId())).findFirst().orElse(null);
        if(e != null){
            data.remove(e.getKey());
        }
    }

    @Override
    public void delete(Iterable<? extends FileData> iterable) {

    }

    @Override
    public void deleteAll() {
        data.clear();
    }
}
