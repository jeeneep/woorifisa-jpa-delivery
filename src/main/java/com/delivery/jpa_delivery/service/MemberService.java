package com.delivery.jpa_delivery.service;

import com.delivery.jpa_delivery.entity.Member;
import com.delivery.jpa_delivery.entity.Orders;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    // Spring Data JPA 대신 순수 JPA의 EntityManager 사용
    private final EntityManager em;

    /** 1. 회원 단건 조회 (ID 기반) */
    public Member findOne(Long memberId) {
        Member member = em.find(Member.class, memberId);
        if (member == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }
        return member;
    }

    /** 2. 모든 회원 조회 */
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    /** 3. 특정 회원의 주문 목록 조회 (성능 최적화 버전) */
    public List<Orders> getMemberOrders(Long memberId) {
        // fetch join을 활용해 N+1 문제 해결
        List<Member> members = em.createQuery(
                        "select m from Member m join fetch m.orders where m.id = :memberId", Member.class)
                .setParameter("memberId", memberId)
                .getResultList();

        if (members.isEmpty()) {
            throw new IllegalArgumentException("해당 회원이 없습니다.");
        }

        // 결과가 한 건이므로 첫 번째 요소를 꺼내 주문 목록 반환
        return members.get(0).getOrders();
    }

    /** 4. 회원 가입 (데이터 저장) */
    public Long join(Member member) {
        // save() 대신 persist()를 사용합니다.
        // persist를 호출하는 순간 영속성 컨텍스트에 객체가 담깁니다.
        em.persist(member);
        return member.getId();
    }
}