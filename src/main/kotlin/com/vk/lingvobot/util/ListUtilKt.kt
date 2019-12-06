package com.vk.lingvobot.util

import com.vk.lingvobot.entities.Dialog
import java.util.*
import kotlin.math.min

fun List<Dialog>.containsDialog(dialogName: String): Boolean {
    return this.any { it.dialogName == dialogName }
}

fun <T> List<T>.chop(divideBy: Int): List<List<T>>? {
    val choppedList: MutableList<List<T>> = ArrayList()
    val elementsInOriginalList = this.size
    var elementCounter = 0
    while (elementCounter < elementsInOriginalList) {
        choppedList.add(
            ArrayList(this.subList(elementCounter, min(elementsInOriginalList, elementCounter + divideBy)))
        )
        elementCounter += divideBy
    }
    return choppedList
}