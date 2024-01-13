package io.raemian.infra.logging.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class ErrorLocationEnum(val value: String) {
    @JsonProperty("CL")
    CLIENT("CLIENT"),

    @JsonProperty("FS")
    FRONTEND_SERVER("FRONTEND SERVER"),

    @JsonProperty("FM")
    FRONTEND_MIDDLEWARE("FRONTEND MIDDLEWARE"),

    @JsonProperty("BS")
    BACKEND_SERVER("BACKEND SERVER"),
    ;
}
