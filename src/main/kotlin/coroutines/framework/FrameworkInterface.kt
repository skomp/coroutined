package coroutines.framework

// This is an interface to be implemented by the framework client, e.g., a grpc service.
interface FrameworkInterface {
    suspend fun callFromFramework(r: Request): Response

    object Request
    object Response
}
