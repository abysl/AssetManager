package com.abysl.assetmanager.model

data class AssetIndexScore(
    val score:Int,
    val assetIndex: AssetIndex
): Comparable<AssetIndexScore>{

    override fun compareTo(other: AssetIndexScore): Int = compareByDescending<AssetIndexScore> { it.score }.compare(this, other)
}