package com.fe_b17.simplenotes;

import com.fe_b17.simplenotes.session.repo.RefreshTokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CleanupService {

    private final RefreshTokenRepo refreshTokenRepo;

//    @Scheduled(initialDelay = 6000000, fixedRate = 6000000)
//    public void cleanupExpiredAndOldTokens() {
//        Instant cutoff = Instant.now().minus(Duration.ofDays(10));
//        refreshTokenRepo.deleteByActiveFalseAndExpiresAtBefore(cutoff);
//        System.out.println("Deleted the inactive tokens");
//    }

//    @Scheduled(fixedRate = 600000)
//    public void cleanupSessions() {
//
//        refreshTokenRepo.deleteByActiveFalse();
//        sessionRepo.deleteByActiveFalse();
//        System.out.println("Deleted the inactive sessions");
//    }

}
