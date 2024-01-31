package io.raemian.infra.logging.enums

enum class LogTemplate(val message: String) {
    ERROR_HEADER(":red_circle: [%s] 오류 발생"),
    SERVICE_STATICS_HEADER(":bar_chart: %s 반디부디 서비스 현황"),
    ERROR_MESSAGE("Error Message"),
    APP_NAME("*App Name*: %s"),
    USERAGENT("*User Agent*: %s"),
    REFERER("*Referer*: %s"),
    STATICS_INCREASE("총: %s (오늘 %s 증가)"),
    ;

    fun of(vararg value: String?): String {
        return this.message.format(*value)
    }
}
