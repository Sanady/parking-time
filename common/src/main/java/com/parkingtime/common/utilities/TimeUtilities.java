package com.parkingtime.common.utilities;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TimeUtilities {
    private static Date convertLocalDateTimeToDateUsingInstant(LocalDateTime dateToConvert) {
        return Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    public static boolean isAtLeastFiveMinutesAgo(LocalDateTime date) {
        Date convertedDate = convertLocalDateTimeToDateUsingInstant(date);
        Instant instant = Instant.ofEpochMilli(convertedDate.getTime());
        Instant twentyMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));
        return instant.isBefore(twentyMinutesAgo);
    }
}
