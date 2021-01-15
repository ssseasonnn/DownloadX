package zlc.season.downloadx

sealed class State {
    class Waiting : State()
    class Started : State()
    class Stopped : State()
    class Failed : State()
    class Succeed : State()
}