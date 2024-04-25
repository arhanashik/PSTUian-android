package com.workfort.pstuian.view.service


interface StateUpdate<State> {
    operator fun invoke(oldState: State): State
}