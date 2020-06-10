package util

fun Any?.println(){
    println(this)
}
private fun  produceTag(): String {
    val stringBuffer = StringBuffer()
    stringBuffer.append(Thread.currentThread().stackTrace[5].fileName)
    stringBuffer.append(" , ")
    stringBuffer.append(Thread.currentThread().stackTrace[5].className)
    stringBuffer.append(" , ")
    stringBuffer.append(Thread.currentThread().stackTrace[5].methodName)
    stringBuffer.append(" , ")
    stringBuffer.append(Thread.currentThread().stackTrace[5].lineNumber)
    return stringBuffer.toString()
}

private fun log(tag: String, message: Any?) {
    println("[$tag] : [$message]")
}

fun log(message: Any?){
    log(produceTag(), message)
}
private fun Any?.log(tag: String) {
    log(tag, this)
}
@JvmName("call by Object")
fun Any?.log() {
    this.log(produceTag())
}