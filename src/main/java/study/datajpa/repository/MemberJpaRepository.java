package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {
    private final EntityManager em;

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }

    public void delete(Member member) {
        em.remove(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public Long count() {
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findByUserNameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m" +
                        " where m.username = :username" +
                        " and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    public List<Member> tryNamedQuery(String username) {
        return em.createNamedQuery("Member.namedQueryTest", Member.class)
                .setParameter("username", username)
                .getResultList();
    }

    public int agePlus(int age){
        return em.createQuery("update Member m set m.age = m.age + 1" +
                " where m.age >= :age")
                .setParameter("age", age)
                .executeUpdate(); // update 된 row 갯수가 return
    }
}
