package com.diegorezm.ratemymusic.modules.common

class PublicException(
    override val message: String = "Something went wrong.",
) : RuntimeException(message) {
}