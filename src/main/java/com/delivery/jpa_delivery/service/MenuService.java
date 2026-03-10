package com.delivery.jpa_delivery.service;

import com.delivery.jpa_delivery.entity.Menu;
import com.delivery.jpa_delivery.entity.Store;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final EntityManager em;

    // 메뉴 저장
    @Transactional
    public Long save(Menu menu, Long storeId) {
        // 메뉴가 소속될 가게 조회
        Store store = em.find(Store.class, storeId);
        if (store == null) {
            throw new IllegalArgumentException("존재하지 않는 가게입니다. ID: " + storeId);
        }

        menu.confirmStore(store);

        em.persist(menu);
        return menu.getId();
    }

    // 메뉴 단건 조회
    public Menu findOne(Long menuId) {
        Menu menu = em.find(Menu.class, menuId);
        if (menu == null) {
            throw new IllegalArgumentException("존재하지 않는 메뉴입니다.");
        }
        return menu;
    }

    // 전체 메뉴 조회
    public List<Menu> findAll() {
        return em.createQuery("select m from Menu m", Menu.class)
                .getResultList();
    }

    // 특정 가게의 메뉴 목록 조회
    public List<Menu> findByStore(Long storeId) {
        return em.createQuery(
                        "select m from Menu m where m.store.id = :storeId", Menu.class)
                .setParameter("storeId", storeId)
                .getResultList();
    }

    //메뉴 가격 수정
    @Transactional
    public void updateMenu(Long menuId, String name, int price) {
        Menu menu = em.find(Menu.class, menuId);
        if (menu == null) {
            throw new IllegalArgumentException("수정하려는 메뉴가 없습니다.");
        }

        menu.setName(name);
        menu.setPrice(price);
    }
}