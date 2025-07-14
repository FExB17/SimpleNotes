package com.fe_b17.simplenotes.reminder.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public enum RepeatMode {
    NONE {
        @Override
        public Instant getNextTrigger(Instant current) {
            return null;
        }
    },
    DAILY {
        @Override
        public Instant getNextTrigger(Instant current) {
            return current.plus(1, ChronoUnit.DAYS);
        }
    },
    WEEKLY {
        @Override
        public Instant getNextTrigger(Instant current) {
            return current.plus(7, ChronoUnit.DAYS);
        }
    },
    MONTHLY {
        @Override
        public Instant getNextTrigger(Instant current) {
            return current.plus(1, ChronoUnit.MONTHS);
        }
    },
    YEARLY {
        @Override
        public Instant getNextTrigger(Instant current) {
            return current.plus(1, ChronoUnit.YEARS);
        }
    },
    CUSTOM{
        @Override
        public Instant getNextTrigger(Instant current) {
            return current.plus(1, ChronoUnit.DAYS);
        }
    };

    public abstract Instant getNextTrigger(Instant current);

}