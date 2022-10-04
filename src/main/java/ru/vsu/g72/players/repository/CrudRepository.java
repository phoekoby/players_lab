package ru.vsu.g72.players.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CrudRepository <E, ID>{
    E save(E e);
    List<E> saveAll(Collection<E> e);


    Optional<E> getById(ID id);

    List<E> getAll();

    void delete(ID id);

    boolean existById(ID id);
}
