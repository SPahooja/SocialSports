package com.uwcs446.socialsports.ui.matchlist

import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.User
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.min

class MatchListUtils {
    companion object {
        val mockEntries = listOf(
            Match(
                id = "1",
                sport = Sport.SOCCER,
                title = "Soccer for All!",
                description = "Soccer for All",
                date = LocalDate.parse("2021-07-10"),
                time = LocalTime.parse("12:30:00"),
                duration = Duration.parse("PT2H"),
                host = User("1"),
                teamOne = listOf(User("1")),
                teamTwo = listOf(User("1"))
            ),
            Match(
                id = "2",
                sport = Sport.BASKETBALL,
                title = "Pro Players Only",
                description = "Pro Players Only",
                date = LocalDate.parse("2021-07-20"),
                time = LocalTime.parse("10:00:00"),
                duration = Duration.parse("PT2H"),
                host = User("1"),
                teamOne = listOf(User("1"), User("1")),
                teamTwo = listOf(User("1"))
            ),
            Match(
                id = "3",
                sport = Sport.SOCCER,
                title = "Scarborough Soccer 6v6",
                description = "Scarborough Soccer 6v6",
                date = LocalDate.parse("2021-07-17"),
                time = LocalTime.parse("18:00:00"),
                duration = Duration.parse("PT1H"),
                host = User("1"),
                teamOne = listOf(User("1"), User("1")),
                teamTwo = listOf(User("1"), User("1"))
            ),
            Match(
                id = "4",
                sport = Sport.ULTIMATE,
                title = "Ultimate Frisbee Pickup",
                description = "Ultimate Frisbee Pickup",
                date = LocalDate.parse("2021-07-22"),
                time = LocalTime.parse("15:30:00"),
                duration = Duration.parse("PT2H"),
                host = User("1"),
                teamOne = listOf(User("1"), User("1")),
                teamTwo = listOf(User("1"), User("1"), User("1"))
            )
        )

        fun genFakeMatchData(len: Int): List<Match> {
            val boundedLen = min(mockEntries.size, len)
            return mockEntries.slice(IntRange(0, boundedLen - 1))
        }
    }
}
