package com.delivery.jpa_delivery.service;

import com.delivery.jpa_delivery.entity.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StoreService {

    @PersistenceContext
    private EntityManager em;

    // 가게 등록
    public Store save(Store store) {
        em.persist(store);
        return store;
    }

    // 가게 단건 조회
    @Transactional(readOnly = true)
    public Store findById(Long id) {
        return em.find(Store.class, id);
    }

    // 전체 가게 목록 조회
    @Transactional(readOnly = true)
    public List<Store> findAll() {
        return em.createQuery("SELECT s FROM Store s", Store.class)
                .getResultList();
    }

    // 가게 삭제
    public void delete(Long id) {
        Store store = em.find(Store.class, id);
        if (store != null) {
            em.remove(store);
        }
    }
}
