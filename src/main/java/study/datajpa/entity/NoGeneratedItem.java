package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoGeneratedItem implements Persistable<String> {
    @Id
    private String id;
    @CreatedDate
    private LocalDateTime createdTime;

    public NoGeneratedItem(String id) {
        this.id = id;
    }

    // Persistable 인터페이스 함수
    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        // 새거인지 아닌지 판단
        // 이 판단을 Auditing을 사용하는 것이 편하다 (생성 날짜)
        return createdTime == null;
    }
}
