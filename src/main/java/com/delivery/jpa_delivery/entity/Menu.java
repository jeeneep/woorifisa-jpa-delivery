package com.delivery.jpa_delivery.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    private String name;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    public void confirmStore(Store store) {
        this.store = store;
        store.getMenus().add(this);
    }

    @OneToMany(mappedBy = "menu")
    private List<Orders> orders = new ArrayList<>();
}
