package zlc.season.downloadx

sealed class State() {
    var progress: Progress = Progress()
        internal set

    class None : State()
    class Waiting : State()
    class Downloading : State()
    class Stopped : State()
    class Failed : State()
    class Succeed : State()

    fun isEnd(): Boolean {
        return this is Stopped || this is Failed || this is Succeed
    }
}