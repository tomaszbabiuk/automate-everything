package eu.geekhome.domain.hardware

enum class AdapterState {
    Initialized,
    DiscoveryPending,
    DiscoverySuccess,
    DiscoveryError,
    Refreshing,
    OperationError,
    Operating
}