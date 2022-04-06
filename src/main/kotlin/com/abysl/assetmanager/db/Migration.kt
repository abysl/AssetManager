package com.abysl.assetmanager.db

abstract class Migration() {

    abstract fun up()

    abstract fun down()
}