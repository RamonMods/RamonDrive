package com.ramon.ramondrive.core.crypto

class KeyUnavailableException(name: String) :
    IllegalStateException("Key '$name' cannot be unwrapped on this device")
