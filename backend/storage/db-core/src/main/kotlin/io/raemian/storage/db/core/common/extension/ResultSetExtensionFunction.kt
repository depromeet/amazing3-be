package io.raemian.storage.db.core.common.extension

import java.sql.ResultSet

fun ResultSet.getLongOrNull(columnName: String): Long? {
    return wasNull(this, getLong(columnName))
}

fun <T> wasNull(resultSet: ResultSet, value: T) = when {
    resultSet.wasNull() -> null
    else -> value
}
