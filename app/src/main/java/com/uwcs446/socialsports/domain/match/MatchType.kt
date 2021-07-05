package com.uwcs446.socialsports.domain.match

enum class MatchType(val teamCount: Int, val teamSize: Int) {

    SOCCER(teamCount = 2, teamSize = 10),
    BASKETBALL(teamCount = 2, teamSize = 10),
    ULTIMATE(teamCount = 2, teamSize = 10)
}
