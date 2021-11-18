package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

// 등록일, 수정일, 등록자, 수정자 등 어플리케이션 운용 시 필요한 기본 데이터
// 다른 Entity에서 일괄적으로 처리 가능하도록 extends 처리
@MappedSuperclass   // 속성 공유 데이터 어노테이션
@Getter
public class JpaBaseEntity {
    @Column(updatable = false, insertable = true)  // 최초 등록 시만 처리, update 불가
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist 전에 처리하는 부분
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // update 전에 처리하는 부분
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
