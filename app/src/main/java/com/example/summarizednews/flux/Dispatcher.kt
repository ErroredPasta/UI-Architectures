package com.example.summarizednews.flux

object Dispatcher {
    private val stores = mutableListOf<Store<*>>()

    fun subscribe(store: Store<*>) {
        stores.add(store)
    }

    fun unsubscribe(store: Store<*>) {
        stores.remove(store)
    }

    fun dispatch(action: Action) {
        for (store in stores) {
            store.sendAction(action = action)
        }
    }
}