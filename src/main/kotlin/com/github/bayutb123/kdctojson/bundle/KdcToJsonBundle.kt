package com.github.bayutb123.kdctojson.bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.PropertyKey

private const val BUNDLE = "messages.KdcToJsonBundle"

object KdcToJsonBundle : DynamicBundle(BUNDLE) {

    @Nls
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ): String = getMessage(key, *params)

    @Nls
    fun messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ) = getLazyMessage(key, *params)
}
