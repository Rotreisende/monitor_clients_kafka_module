package validate

object Headers extends Enumeration {
  type Header = Value
  val HASH_ID, LOAD_DATE, RECORD_ID, CLIENT_SIN, SURNAME, NAME, PATRONYMIC, BIRTH_DATE,
      BIRTH_PLACE, PRIMARY_ID_TYPE, PRIMARY_ID_NUMBER, DELETE_FLAG = Value

  val checks = Seq(
    RECORD_ID, CLIENT_SIN, SURNAME, NAME, PATRONYMIC, BIRTH_DATE,
    BIRTH_PLACE, PRIMARY_ID_TYPE, PRIMARY_ID_NUMBER, DELETE_FLAG)

  val all = Seq(
    HASH_ID, LOAD_DATE, RECORD_ID, CLIENT_SIN, SURNAME, NAME, PATRONYMIC, BIRTH_DATE,
    BIRTH_PLACE, PRIMARY_ID_TYPE, PRIMARY_ID_NUMBER, DELETE_FLAG
  )

  val options = List(PATRONYMIC,BIRTH_PLACE)

  implicit class RichHeaders(header: Header) {
    def convertToString(): String = {
      header match {
        case HASH_ID => "hashId"
        case LOAD_DATE => "loadDate"
        case RECORD_ID => "recordId"
        case CLIENT_SIN => "clientSin"
        case SURNAME => "surname"
        case NAME => "name"
        case PATRONYMIC => "patronymic"
        case BIRTH_DATE => "birthDate"
        case BIRTH_PLACE => "birthPlace"
        case PRIMARY_ID_TYPE => "primaryIdType"
        case PRIMARY_ID_NUMBER => "primaryIdNumber"
        case DELETE_FLAG => "deleteFlag"
      }
    }
  }
}
