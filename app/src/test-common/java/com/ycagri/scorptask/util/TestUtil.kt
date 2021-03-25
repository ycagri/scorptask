package com.ycagri.scorptask.util

import com.ycagri.scorptask.datasource.Person


object TestUtil {


    fun createPeopleList() = listOf(
        Person(id = 1, fullName = "Person 1"),
        Person(id = 2, fullName = "Person 2")
    )
}