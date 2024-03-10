package com.github.silhouettemc.util.type

class ReasonContext(
    unparsed: String?,
) {

    // in the future, we might want to add more things to this, like notes? or an explicit -p for public, if we make an option to silent by default

    var reason: String? = ""
        private set

    var isSilent: Boolean = false
        private set

    init {
        if (unparsed != null) {
            isSilent = unparsed.startsWith("-s ", true) || unparsed.endsWith(" -s", true)
            reason = unparsed
                .removePrefix("-s ")
                .removeSuffix(" -s")
        }
    }

}