package eu.geekhome.domain.automation

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.annotations.XStreamAlias
import com.thoughtworks.xstream.annotations.XStreamAsAttribute
import com.thoughtworks.xstream.annotations.XStreamConverter
import com.thoughtworks.xstream.annotations.XStreamImplicit
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter

@XStreamAlias("block")
data class Block(

    @XStreamAsAttribute
    val type: String,

    @XStreamImplicit(itemFieldName="field")
    val fields: List<Field>?,

    val next: Next?,

    @XStreamImplicit(itemFieldName="statement")
    val statements: List<Statement>?,

    @XStreamImplicit(itemFieldName="value")
    val values: List<Value>?
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

    @XStreamAsAttribute
    val name: String,

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
    val name: String? = null
    val value: String? = null
}

class BlocklyParser {

    private val xStream = XStream()

    fun parse(xml: String) : BLocklyXml {
        xStream.processAnnotations(arrayOf(
            BLocklyXml::class.java,
            Block::class.java,
            Field::class.java,
            Next::class.java,
            Statement::class.java,
            Value::class.java
        ))

        return xStream.fromXML(xml) as BLocklyXml
    }
}