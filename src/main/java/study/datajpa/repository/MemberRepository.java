package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Member;

import java.util.List;

// JPA 확장 인터페이스. JpaRepository를 상속받는 인터페이스로 만들어야 함
// 타입과 키의 변수형을 알려줘야 한다 <[클래스], [키 타입]>
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 함수명만 잘 입력해도 DataJpa가 알아서 잘 만들어줌
    // https://docs.spring.io/spring-data/jpa/docs/2.6.0/reference/html/#jpa.query-methods.query-creation
    // 이름이 좀 길어지는 단점이 생김
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);
}
