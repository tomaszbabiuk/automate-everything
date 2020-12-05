package eu.geekhome.sqldelightplugin

import com.geekhome.common.extensibility.PluginMetadata
import com.geekhome.common.localization.Resource
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import eu.geekhome.sqldelightplugin.database.Database
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

class SqlDelightPlugin(wrapper: PluginWrapper?) : Plugin(wrapper), PluginMetadata {
    override fun start() {
        println("Starting SQLDelight plugin")

        val driver: SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        Database.Schema.create(driver)
    }

    override fun stop() {
        println("SQLDelight plugin.stop()")
    }

    override fun getName(): Resource {
        return R.plugin_name
    }

    override fun getDescription(): Resource {
        return R.plugin_description
    }
}
