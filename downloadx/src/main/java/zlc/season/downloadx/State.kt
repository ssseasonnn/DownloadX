package zlc.season.downloadx

sealed class State {
    object Waiting : State()
    object Started : State()
    object Stopped : State()
    object Failed : State()
    object Succeed : State()
}