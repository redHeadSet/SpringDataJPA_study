package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

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
    @Query("select m from Member m" +
            " where m.username = :username" +
            " and m.age > :age")
    List<Member> repositoryMethodQuery(@Param("username") String username,
                                       @Param("age") int age);
    // cf. 동적 쿼리는? : QueryDSL 로 처리하는 것이 좋다

    @Query("select m.username from Member m")
    List<String> findByUserNameList();

    // DTO 조회
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m" +
            " join m.team t")
    List<MemberDto> findDtos();

    // 반환 타입을 유연하게 써도 알아먹음
    // https://docs.spring.io/spring-data/jpa/docs/2.6.0/reference/html/#appendix.query.return.types
    List<Member> findMemberListByUsername(String username);
    Member findSingleMemberByUsername(String username);
    Optional<Member> findOptionalMemberByUsername(String username);

    Page<Member> findPageByAge(int age, Pageable pageable);
    Slice<Member> findSliceByAge(int age, Pageable pageable);

    // Paging 처리 시, 성능 문제가 되는 부분은 total count를 구하는 부분이다
    // (total count 쿼리 역시 똑같이 join 처리를 하는... 비효율적인 부분이 생길 수 있다)
    // 이를 위해 count query는 따로 처리해야 하는 경우가 있다
    @Query( value = "select m from Member m where m.age = :age",
            countQuery = "select count(m) from Member m where m.age = :age")
    Page<Member> findPageCustomCountByAge(@Param("age") int age, Pageable pageable);

    @Modifying(clearAutomatically = true)  // 벌크 update 처리 시 필요한 어노테이션 - 없는 경우, InvalidDataAccessApiUsageException 발생
    @Query("update Member m set m.age = m.age + 1 where age >= :age")
    int agePlus(@Param("age") int age);
    // [벌크 연산의 주의점]
    // 하지만 DB에 바로 쿼리를 던지는 것이기 때문에, 영속성 컨텍스트와 일치하지 않을 수 있다
    // 즉, 벌크 연산 후에는 영속성 컨텍스트를 전부 날려주는 것이 좋다
    // Data JPA에서 지원하는 Modifying(clearAutomatically 값을 true로 설정하면 자동으로 EntityManager를 클리어

    @Query("select m from Member m join fetch m.team")
    List<Member> findAllFetchJoin();
    // fetch join을 매번 적기 너무 귀찮음 : EntityGraph로 처리 가능 - JPA 표준에 등록된 기능

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll(); // findAll 처리 시 team 객체를 들고옴
 
    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findAllFetchJoinToEntityGraph();
}