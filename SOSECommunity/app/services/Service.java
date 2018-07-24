package services;

/**
 * Created by imrenagi on 10/31/17.
 */
public interface Service<T> {

    Iterable<T> findAll();
    T find(Long id);
    void delete(Long id);
//    T createOrUpdate(T object);

}
