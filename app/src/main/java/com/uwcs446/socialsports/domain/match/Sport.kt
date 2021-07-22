package com.uwcs446.socialsports.domain.match

import com.uwcs446.socialsports.R

enum class Sport(val teamSize: Int, val imageResource: Int) {

    SOCCER(teamSize = 10, imageResource = R.drawable.ic_sports_soccer),
    BASKETBALL(teamSize = 10, imageResource = R.drawable.ic_baseline_sports_basketball),
    ULTIMATE(teamSize = 10, imageResource = R.drawable.ic_baseline_sports_ultimate),
    ANY(teamSize = 0, imageResource = R.drawable.ic_sports_soccer)
}
