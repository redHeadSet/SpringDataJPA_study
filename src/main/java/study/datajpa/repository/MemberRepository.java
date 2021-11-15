package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

// JPA 확장 인터페이스. JpaRepository를 상속받는 인터페이스로 만들어야 함
// 타입과 키의 변수형을 알려줘야 한다 <[클래스], [키 타입]>
public interface MemberRepository extends JpaRepository<Member, Long> {
}
