package com.uwcs446.socialsports.ui.matchlist

import com.google.android.gms.maps.model.LatLng
import com.uwcs446.socialsports.domain.match.Location
import com.uwcs446.socialsports.domain.match.Match
import com.uwcs446.socialsports.domain.match.Sport
import com.uwcs446.socialsports.domain.user.User
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import kotlin.math.min

class MatchListUtils {
    companion object {
        private val mockEntries = listOf(
            Match(
                id = "1",
                sport = Sport.SOCCER,
                title = "Soccer for All!",
                description = "Soccer for All",
                date = LocalDate.parse("2021-07-10"),
                time = LocalTime.parse("12:30:00"),
                duration = Duration.parse("PT2H"),
                hostId = UUID.randomUUID().toString(),
                location = Location(LatLng(0.0, 0.0)),
                teamOne = listOf("1"),
                teamTwo = listOf("1")
            ),
            Match(
                id = "2",
                sport = Sport.BASKETBALL,
                title = "Pro Players Only",
                description = "Pro Players Only",
                date = LocalDate.parse("2021-07-20"),
                time = LocalTime.parse("10:00:00"),
                duration = Duration.parse("PT2H"),
                hostId = UUID.randomUUID().toString(),
                location = Location(LatLng(0.0, 0.0)),
                teamOne = listOf("1", "1"),
                teamTwo = listOf("1")
            ),
            Match(
                id = "3",
                sport = Sport.SOCCER,
                title = "Scarborough Soccer 6v6",
                description = "Scarborough Soccer 6v6",
                date = LocalDate.parse("2021-07-17"),
                time = LocalTime.parse("18:00:00"),
                duration = Duration.parse("PT1H"),
                hostId = UUID.randomUUID().toString(),
                location = Location(LatLng(0.0, 0.0)),
                teamOne = listOf("1", "1"),
                teamTwo = listOf("1", "1")
            ),
            Match(
                id = "4",
                sport = Sport.ULTIMATE,
                title = "Ultimate Frisbee Pickup",
                description = "Ultimate Frisbee Pickup",
                date = LocalDate.parse("2021-07-22"),
                time = LocalTime.parse("15:30:00"),
                duration = Duration.parse("PT2H"),
                hostId = UUID.randomUUID().toString(),
                teamOne = listOf("1", "1"),
                teamTwo = listOf("1", "1", "1"),
                location = Location(LatLng(0.0, 0.0)),
            ),
            Match(
                id = "5",
                sport = Sport.SOCCER,
                title = "UEFA Euro Cup Finals",
                description = "Italy vs England",
                date = LocalDate.parse("2021-07-11"),
                time = LocalTime.parse("15:00:00"),
                duration = Duration.parse("PT2H"),
                hostId = UUID.randomUUID().toString(),
                location = Location(LatLng(0.0, 0.0)),
                teamOne = listOf("1", "1"),
                teamTwo = listOf("1", "1", "1")
            )
        )

        fun genFakeMatchData(len: Int): List<Match> {
            val boundedLen = min(mockEntries.size, len)
            return mockEntries.slice(IntRange(0, boundedLen - 1))
        }
    }
}
