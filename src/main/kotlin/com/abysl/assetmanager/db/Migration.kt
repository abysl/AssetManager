package com.abysl.assetmanager.db

abstract class Migration(val version :Int) {

    abstract fun up()

    abstract fun down()
}