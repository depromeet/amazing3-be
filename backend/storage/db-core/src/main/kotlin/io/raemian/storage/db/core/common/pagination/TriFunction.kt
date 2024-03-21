package io.raemian.storage.db.core.common.pagination

@FunctionalInterface
fun interface TriFunction<T, U, V, R> {
    fun apply(t: T, u: U, v: V): R
}
