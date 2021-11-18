package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import study.datajpa.entity.GeneratedItem;
import study.datajpa.entity.NoGeneratedItem;

@SpringBootTest
@Transactional
class ItemRepositoryTest {
    @Autowired
    GeneratedItemRepository generatedItemRepository;
    @Autowired
    NoGeneratedItemRepository noGeneratedItemRepository;

    // SimpleJpaRepository 저장 로직
//    @Transactional
//    @Override
//    public <S extends T> S save(S entity) {
//
//        Assert.notNull(entity, "Entity must not be null.");
//
//        if (entityInformation.isNew(entity)) {
//            em.persist(entity);
//            return entity;
//        } else {
//            return em.merge(entity);
//        }
//    }

    @Test
    public void gen_save() {
        GeneratedItem item = new GeneratedItem();
        generatedItemRepository.save(item);
        // SimpleJpaRepository 내 JpaEntityInformation.isNew 확인 (현 코드 line 20확인)
        // 식별자(@Id)가 비었으니 새거라고 판단, persist 호출됨 - GeneratedValue인 경우에만!
    }

    // GeneratedValue 가 없는 상황의 Item이라면...? : NoGeneratedItem
    @Test
    public void no_gen_save() {
        noGeneratedItemRepository.save(new NoGeneratedItem("new"));
        // SimpleJpaRepository 내 JpaEntityInformation.isNew 확인 (현 코드 line 20확인)
        // isNew 까지 갔을 때, 이미 id에 값이 있기 때문에 persist 호출이 되지 않는다. : merge 처리되는 문제 발생
        // 해결하려면? Persistable 인터페이스 상속 구현 : Item 클래스에서 처리!
        // getId 및 isNew 직접 구현
    }
}