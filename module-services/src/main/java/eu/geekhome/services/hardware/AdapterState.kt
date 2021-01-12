package eu.geekhome.services.hardware

enum class AdapterState {
    Initialized,
    DiscoveryPending,
    DiscoverySuccess,
    DiscoveryError,
    Refreshing,
    OperationError,
    Operating
}