package com.example.summarizednews.flux

abstract class Store<State>(
    initialState: State
) {
    protected var state = initialState
        set(value) {
            field = value
            onChange?.invoke(field)
        }

    private var onChange: ((State) -> Unit)? = null

    abstract fun sendAction(action: Action)

    fun onStateChange(onChange: (State) -> Unit) {
        this.onChange = onChange
        onChange(state)
    }
}