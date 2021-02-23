package eu.geekhome.rest

import com.thoughtworks.xstream.XStream
import eu.geekhome.HardwareManager
import org.pf4j.PluginManager
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter

import com.thoughtworks.xstream.annotations.XStreamConverter

import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamImplicit


@XStreamAlias("block")
data class Block(

    @XStreamAsAttribute
    val type: String,

    val field: Field?,

    val next: Next?,

    @XStreamImplicit(itemFieldName="statement")
    val statements: List<Statement>?,

    val value: Value
)

@XStreamAlias("next")
data class Next(
    val block: Block?
)

@XStreamAlias("statement")
data class Statement(

    @XStreamAsAttribute
    val name: String,

    val block: Block?
)

@XStreamAlias("value")
data class Value(

    @XStreamAsAttribute
    val value: String,

    val block: Block?
)

@XStreamAlias("xml")
data class BLocklyXml(
    @XStreamImplicit(itemFieldName="block")
    val blocks: List<Block>
)

@XStreamAlias("field")
@XStreamConverter(value = ToAttributedValueConverter::class, strings = ["value"])
class Field {
    private val name: String? = null
    private val value: String? = null
}

class AutomationConductor(val hardwareManager: HardwareManager, val pluginsCoordinator: PluginManager) {

    private var enabled: Boolean = false

    fun isEnabled(): Boolean {
        return enabled
    }

    fun enable() {
        if (!enabled) {
            enabled = true
            println("Enabling automation")

            pluginsCoordinator
                .getRepository()
                .getAllInstances()
                .filter { it.automation != null }
                .map { it.automation }
                .forEach { xml ->
                    val xstream = XStream()
                    xstream.processAnnotations(arrayOf(
                        BLocklyXml::class.java,
                        Block::class.java,
                        Field::class.java
                    ))
                    val xxxml: BLocklyXml = xstream.fromXML(xml) as BLocklyXml
                    println(xxxml)
                }
        }
    }

    fun disable() {
        enabled = false
        println("Disabling automation")
    }
}