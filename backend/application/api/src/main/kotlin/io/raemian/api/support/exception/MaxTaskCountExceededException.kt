package io.raemian.api.support.exception

class MaxTaskCountExceededException : CoreApiException(
    ErrorInfo.MAX_TASK_COUNT_EXCEEDED_EXCEPTION,
)
