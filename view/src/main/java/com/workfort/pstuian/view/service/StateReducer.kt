package com.workfort.pstuian.view.service


interface StateReducer<State, Update: StateUpdate<State>> {
    val initial: State
    fun reduce(currentState: State, update: Update) = update(oldState = currentState)
}