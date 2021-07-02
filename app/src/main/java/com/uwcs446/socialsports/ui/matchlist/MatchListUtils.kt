package com.uwcs446.socialsports.ui.matchlist

class MatchListUtils {

    companion object {

        fun genFakeMatchData(len: Int): List<List<String>> {
            val fakeMatchDataList: ArrayList<List<String>> = arrayListOf()
            for (i in 1..len) {
                val entry = listOf(
                    "Soccer for All! $i",
                    "Soccer",
                    "4 / 10",
                    "Jun 30, 2021",
                    "12:30 PM",
                    "Central Tech Stadium",
                    "725 Bathurst St, Toronto, ON M5S 2R5"
                )
                fakeMatchDataList.add(entry)
            }
            return fakeMatchDataList
        }
    }
}
