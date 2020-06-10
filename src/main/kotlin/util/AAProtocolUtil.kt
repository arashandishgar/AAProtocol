package util

//registered port number  for server
const val PORT_Connect = 1025
const val TEXT_TAG_Input="T"
const val FILE_TAG_Input="F"
const val SPLITER=":"
const val CONTENT_TYPE_KEYWORD="Content-Type"
const val FILE_SIZE_KEYWORD="Size"
const val SUBTYPE="/"
const val FILE_TYPE="File$SUBTYPE"
const val  FILE_HEADER_TYPE="${CONTENT_TYPE_KEYWORD}${SPLITER}$FILE_TYPE"
const val  FILE_HEADER_SIZE="$FILE_SIZE_KEYWORD${SPLITER}"
const val TEXT_TYPE="TEXT"
const val  TEXT_HEADER="${CONTENT_TYPE_KEYWORD}${SPLITER}$TEXT_TYPE"
const val BOUNDARY=""
const val BASE_STORE_PATH="src/main/resources/"
const val SERVER_STORE_PATH="${BASE_STORE_PATH}server/"
const val CLIENT_STORE_PATH="${BASE_STORE_PATH}client/"

const val USER_GUIDE_SEND_DATA_FORMAT="Send File format be attention do not space between tags and $SPLITER  $FILE_TAG_Input${SPLITER}fileName  sendMessage Format $TEXT_TAG_Input${SPLITER}message"