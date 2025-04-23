package com.app.service;


import com.app.entities.ExerciseHistoryEntity;
import com.app.entities.IslandsEntity;
import com.app.entities.LevelsEntity;
import com.app.entities.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Transactional
@Component
@SuppressWarnings("unchecked")
public class Persist {

    private static final Logger LOGGER = LoggerFactory.getLogger(Persist.class);

    private final SessionFactory sessionFactory;


    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }
    public <T> void saveAll(List<T> objects) {
        for (T object : objects) {
            sessionFactory.getCurrentSession().saveOrUpdate(object);
        }
    }
    public <T> void remove(Object o){
        sessionFactory.getCurrentSession().remove(o);
    }


    public Session getQuerySession() {
        return sessionFactory.getCurrentSession();
    }

    public void save(Object object) {
        this.sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    public void update(Object object) {this.sessionFactory.getCurrentSession().saveOrUpdate(object);}

    public <T> T loadObject(Class<T> clazz, int oid) {
        return this.getQuerySession().get(clazz, oid);
    }

    public <T> List<T> loadList(Class<T> clazz) {
        return this.sessionFactory.getCurrentSession().createQuery("FROM " + clazz.getSimpleName()).list();
    }


    public UserEntity getUserByEmailAndPassword(String email) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email ", UserEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }
    public UserEntity getUserByEmail(String email) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE email = :email ", UserEntity.class)
                .setParameter("email", email)
                .uniqueResult();
    }

    public UserEntity getUserByToken(String password) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM UserEntity WHERE password = :password ", UserEntity.class)
                .setParameter("password", password)
                .uniqueResult();
    }

    public List<ExerciseHistoryEntity> getExercisesByUserId(UserEntity userId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ExerciseHistoryEntity WHERE userId = :userId ", ExerciseHistoryEntity.class)
                .setParameter("userId", userId)
                .list();
    }

    public LevelsEntity getLevelByUserIdAndIslandId(UserEntity userId, IslandsEntity islandId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM LevelsEntity WHERE userId = :userId AND islandId = :islandId", LevelsEntity.class)
                .setParameter("userId", userId)
                .setParameter("islandId", islandId)
                .uniqueResult();
    }

    public ExerciseHistoryEntity getExerciseByExerciseId(int exerciseId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ExerciseHistoryEntity WHERE exerciseId = :exerciseId", ExerciseHistoryEntity.class)
                .setParameter("exerciseId", exerciseId)
                .uniqueResult();
    }

    public List<ExerciseHistoryEntity> getExercisesByUserIdAndLevel(UserEntity userId, int level) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM ExerciseHistoryEntity WHERE userId = :userId AND level = :level", ExerciseHistoryEntity.class)
                .setParameter("userId", userId)
                .setParameter("level", level)
                .list();
    }

    public List<LevelsEntity> getLevelsByUserId(UserEntity userId) {
        return this.sessionFactory.getCurrentSession()
                .createQuery("FROM LevelsEntity WHERE userId = :userId ", LevelsEntity.class)
                .setParameter("userId", userId)
                .list();
    }
}