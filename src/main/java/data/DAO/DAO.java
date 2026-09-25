package data.DAO;

import data.misc.StoredQuery;

import java.util.List;

public interface DAO<T> {
    void addEntry(T entry);
    void overwriteEntry(T newEntry);
    void removeEntry(T entry);
    T findEntry(int id);
    List<T> getByQuery(StoredQuery criteria);
    List<T> getAllEntries();
}
