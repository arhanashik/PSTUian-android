package com.workfort.pstuian.reducer.service


interface StateUpdate<State> {
    operator fun invoke(oldState: State): State
}