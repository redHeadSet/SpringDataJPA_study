package study.datajpa.entity;

import lombok.Getter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// Data JPA 에서 지원하는 기능 : JpaBaseEntity와 거의 동일한 기능을 지원
// 반드시 EnableJpaAuditing 어노테이션을 넣어야 기능이 정상 작동
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity{
    // 시간 정보(BaseTimeEntity)는 항상 필요한 경우가 많지만,
    // 등록자와 수정자는 항상 필요한 정보는 아니므로, 별도의 클래스로 두고
    // 필요한 경우에 따라 BaseTimeEntity를 상속하거나 BaseEntity를 상속받도록 처리


//    @CreatedDate
//    @Column(updatable = false, insertable = true)
//    private LocalDateTime createdDate;
//
//    @LastModifiedDate
//    private LocalDateTime lastModifiedDate;
//
//
    // 아래 등록자와 수정자의 경우, AuditorAware에 정보를 전달해야 함
    // 원래라면 세션 정보에서 사용자 데이터를 꺼내서 이름을 가져오는 등의 처리
    @CreatedBy
    @Column(updatable = false, insertable = true)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;
}
