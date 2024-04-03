package com.github.silhouettemc.util

import com.github.silhouettemc.parsing.duration.TimeUnit
import java.time.Duration

/**
 * Adds [amount] of [unit] to this duration, allows for fractional amounts and attempts to avoid overflowing for large values.
 *
 * @param amount the amount to add
 * @param unit the unit of the amount
 */
fun Duration.plus(amount: Double, unit: TimeUnit): Duration {
    // To avoid overflowing, we only keep nanosecond precision upto years, then we switch to seconds
    return if (unit.duration.seconds * amount >= TimeUnit.YEARS.duration.seconds) this.plusSeconds((amount * unit.duration.seconds).toLong())
    else this.plusNanos((amount * unit.duration.toNanos()).toLong())
}