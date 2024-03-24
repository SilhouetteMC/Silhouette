import java.time.Duration

enum class TimeUnit(
    val singular: String,
    val aliases: List<String>,
    val duration: Duration,
    val plural: String = singular + "s"
) {

    NANOS("Nanosecond", listOf("ns", "nano", "nanos", "nanosecond", "nanoseconds"),
        Duration.ofNanos(1)),

    MICROS("Microsecond", listOf("micro", "micros", "microsecond", "microseconds"),
        Duration.ofNanos(1000)),

    MILLIS("Millisecond", listOf("ms", "milli", "millis", "millisecond", "milliseconds"),
        Duration.ofNanos(1000_000)),

    SECONDS("Second", listOf("s", "sec", "secs", "second", "seconds"),
        Duration.ofSeconds(1)),

    MINUTES("Minute", listOf("m", "min", "mins", "minute", "minutes"),
        Duration.ofSeconds(60)),

    HOURS("Hour", listOf("h", "hr", "hrs", "hour", "hours"),
        Duration.ofSeconds(3600)),

    DAYS("Day", listOf("d", "day", "days"),
        Duration.ofSeconds(86400)),

    WEEKS("Week", listOf("w", "wk", "wks", "week", "weeks"),
        Duration.ofSeconds(86400L * 7L)),

    MONTHS("Month", listOf("mo", "mos", "month", "months"),
        Duration.ofSeconds(86400L * 30L)),

    YEARS("Year", listOf("y", "yr", "yrs", "year", "years"),
        Duration.ofSeconds(86400L * 365L)),

    DECADES("Decade", listOf("dec", "decs", "decade", "decades"),
        Duration.ofSeconds(86400L * 365L * 10L)),

    CENTURIES("Century", listOf("cen", "cens", "century", "centuries"),
        Duration.ofSeconds(86400L * 365L * 100L), "Centuries"),

    MILLENNIA("Millennia", listOf("mil", "mils", "millennium", "millennia"),
        Duration.ofSeconds(86400L * 365L * 1000L)),

    ERAS("Era", listOf("era", "eras"),
        Duration.ofSeconds(86400L * 365L * 1000_000_000L)),
}