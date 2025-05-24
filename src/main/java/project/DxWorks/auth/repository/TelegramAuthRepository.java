package project.DxWorks.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.DxWorks.auth.domain.Entity.TelegramAuthEntity;
import project.DxWorks.auth.domain.Entity.UserAuthEntity;

import java.util.Optional;

@Repository
public interface TelegramAuthRepository extends JpaRepository<TelegramAuthEntity, Long> {
    Optional<TelegramAuthEntity> findByTelegramId(Long telegramId);
}
