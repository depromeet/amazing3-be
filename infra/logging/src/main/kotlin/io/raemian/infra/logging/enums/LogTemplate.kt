package io.raemian.infra.logging.enums

enum class LogTemplate(val message: String) {
    ERROR_HEADER(":red_circle: [%s] 오류 발생"),
    ERROR_MESSAGE("Error Message"),
    APP_NAME("*App Name*: %s"),
    USERAGENT("*User Agent*: %s"),
    REFERER("*Referer*: %s"),
    ;

    fun of(vararg value: String): String {
        return this.message.format(*value)
    }
}
