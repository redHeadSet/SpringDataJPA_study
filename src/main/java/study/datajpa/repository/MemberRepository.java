package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

// JPA 확장 인터페이스. JpaRepository를 상속받는 인터페이스로 만들어야 함
// 타입과 키의 변수형을 알려줘야 한다 <[클래스], [키 타입]>
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 함수명만 잘 입력해도 DataJpa가 알아서 잘 만들어줌
    // https://docs.spring.io/spring-data/jpa/docs/2.6.0/reference/html/#jpa.query-methods.query-creation
    // 이름이 좀 길어지는 단점이 생김
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    // namedQuery 사용
    @Query(name = "Member.namedQueryTest")
    // 해당 어노테이션 없어도 잘 동작함
    // 만약, 네임드쿼리가 없다면 이름을 기반으로 쿼리를 생성하는 처리가 진행됨
    List<Member> tryNamedQuery(@Param("username") String username);

    
    // repository에 바로 쿼리 지정 - 많이 쓰는 방법
    // namedQuery의 장점을 가짐 : 정적 쿼리기 때문에 오타 발생 시 어플리케이션 로딩 시점에서 확인 가능 
    @Query( "select m from Member m" + 
            " where m.username = :username" +
            " and m.age > :age")
    List<Member> repositoryMethodQuery(@Param("username") String username, 
                                       @Param("age") int age);
    // cf. 동적 쿼리는? : QueryDSL 로 처리하는 것이 좋다

}
