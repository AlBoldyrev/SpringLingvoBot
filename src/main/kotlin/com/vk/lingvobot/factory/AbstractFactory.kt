package com.vk.lingvobot.factory

interface AbstractFactory<T> {
    fun getInstance(stateIndex: Int): T
}