package za.ac.sun.grapl.hooks

interface IHookBuilder {
    @Throws(Exception::class)
    fun build(): IHook?
}