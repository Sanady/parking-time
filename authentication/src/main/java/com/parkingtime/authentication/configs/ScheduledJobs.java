package com.parkingtime.authentication.configs;

import com.parkingtime.authentication.models.UserResetPasswordToken;
import com.parkingtime.authentication.repositories.UserResetPasswordTokenRepository;
import com.parkingtime.common.utilities.TimeUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.parkingtime.common.constants.ApplicationConstants.DELAY_DATABASE_PASSWORD_TOKEN_CLEAN_UP;


@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledJobs {
    private final UserResetPasswordTokenRepository userResetPasswordTokenRepository;

    @Scheduled(cron = DELAY_DATABASE_PASSWORD_TOKEN_CLEAN_UP)
    public void cleanUpUserPasswordTokens() {
        int count = 0;
        List<UserResetPasswordToken> userResetPasswordTokenList = userResetPasswordTokenRepository.findAll();
        for (UserResetPasswordToken userResetPasswordToken : userResetPasswordTokenList) {
            if(TimeUtilities.isAtLeastFiveMinutesAgo(userResetPasswordToken.getCreatedAt())) {
                userResetPasswordTokenRepository.delete(userResetPasswordToken);
                log.info("[LOG - CLEAN UP DATABASE] Token with user email {} has been deleted!", userResetPasswordToken.getUser().getEmail());
                count++;
            }
        }
        log.info("[LOG - CLEAN UP DATABASE] Total {} records has been deleted!", count);
    }
}
