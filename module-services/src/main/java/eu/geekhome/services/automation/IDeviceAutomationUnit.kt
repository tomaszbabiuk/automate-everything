package eu.geekhome.services.automation

interface IDeviceAutomationUnit<T> : ICalculableAutomationUnit {
    val value: T?
    fun buildEvaluationResult(): EvaluationResult
}