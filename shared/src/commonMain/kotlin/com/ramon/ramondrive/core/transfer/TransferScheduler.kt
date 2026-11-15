package com.ramon.ramondrive.core.transfer

interface TransferScheduler {

    fun kick(allowMetered: Boolean)

    fun rekick(allowMetered: Boolean)
}
