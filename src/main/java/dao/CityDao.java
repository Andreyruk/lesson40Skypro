package dao;

import jakarta.persistence.*;
import model.City;

import java.util.List;
import java.util.function.Consumer;

public class CityDao implements Dao<City>{

    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("myUnit");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public City get(long id) {
        return entityManager.find(City.class, id);
    }

    @Override
    public List<City> getAll() {
        Query query = entityManager.createQuery("SELECT c FROM City c");
        return query.getResultList();
    }

    @Override
    public void save(City city) {
        executeInsideTransaction(entityManager -> {
            entityManager.persist(city);
            entityManager.flush();
            entityManager.clear();
        });
    }

    @Override
    public void update(long id, City model) {
        City city = this.get(id);
        city.setCityName(model.getCityName());
        executeInsideTransaction(entityManager -> {
            entityManager.merge(city);
            entityManager.flush();
            entityManager.clear();
        });
    }

    @Override
    public void delete(long id) {
        City city = this.get(id);
        executeInsideTransaction(entityManager -> {
            entityManager.remove(city);
            entityManager.flush();
            entityManager.clear();
        });
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        }
        catch (RuntimeException e) {
            tx.rollback();
            throw e;
        }
    }
}
