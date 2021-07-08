package com.uwcs446.socialsports.domain.match

import android.widget.ImageView
import com.uwcs446.socialsports.R

enum class Sport(val teamSize: Int, val iconResource: Int) {

    SOCCER(teamSize = 10, iconResource = R.drawable.ic_sports_soccer),
    BASKETBALL(teamSize = 10, iconResource = R.drawable.ic_baseline_sports_basketball),
    ULTIMATE(teamSize = 10, iconResource = R.drawable.ic_baseline_sports_ultimate);
}
