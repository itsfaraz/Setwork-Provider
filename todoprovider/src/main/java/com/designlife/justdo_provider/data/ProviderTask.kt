package com.designlife.justdo_provider.data
import androidx.compose.ui.graphics.Color

data class ProviderTask(
    val id : Long = 0L,
    val title : String = "",
    val color : Color = Color(0),
    val description : String = "",
    val completed : Boolean = false,
    val time: Long = 0L
){
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
