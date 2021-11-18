package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.GeneratedItem;

public interface GeneratedItemRepository extends JpaRepository<GeneratedItem, Long> {
}
