package betman.db

import java.sql.Connection
import javax.sql.DataSource

interface Database {
    fun connect(): Connection
    val datasource: DataSource
    fun create()
}