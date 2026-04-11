package com.workfort.pstuian.reducer.service


interface StateReducer<State, Update: StateUpdate<State>> {
    val initial: State
    fun reduce(currentState: State, update: Update) = update(oldState = currentState)
}