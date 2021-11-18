package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.NoGeneratedItem;

public interface NoGeneratedItemRepository extends JpaRepository<NoGeneratedItem, String> {
}
