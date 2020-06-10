package util

import core.AAProtocol
import java.lang.Exception

class InvalidInputFormatException : Exception("Invalid input format")

fun handleInputFrom(input: String?): Pair<AAProtocol.TypeData, String> {
    val list =input!!.split(SPLITER).map { it.replace(" ","") }
    if(list.isEmpty() or (list.size==1)){
        throw InvalidInputFormatException()
    }
    when(list.get(0)){
        FILE_TAG_Input -> return Pair(AAProtocol.TypeData.File,input.substringAfter("$FILE_TAG_Input${SPLITER}"))
        TEXT_TAG_Input -> return Pair(AAProtocol.TypeData.TEXT,input.substringAfter("$TEXT_TAG_Input${SPLITER}"))
        else -> throw InvalidInputFormatException()
    }
}