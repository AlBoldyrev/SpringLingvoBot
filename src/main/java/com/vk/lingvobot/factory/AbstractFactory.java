package com.vk.lingvobot.factory;

public interface AbstractFactory<T> {
    T getInstance(Integer stateIndex);
}