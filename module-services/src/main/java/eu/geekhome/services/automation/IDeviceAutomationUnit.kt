package eu.geekhome.services.automation

interface IDeviceAutomationUnit<T> : ICalculableAutomationUnit {
    val valueType: Class<T>
    val value: T?
    fun buildEvaluationResult(): EvaluationResult
}