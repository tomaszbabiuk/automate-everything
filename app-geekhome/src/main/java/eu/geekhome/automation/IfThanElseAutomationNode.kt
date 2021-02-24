package eu.geekhome.automation

class IfThanElseAutomationNode(
    override val next: AutomationNode?,
    val ifNode: AutomationNode?,
    val elseNode: AutomationNode?
    ) : AutomationNode {

    override fun process(timeInMillis: Long) {

    }
}