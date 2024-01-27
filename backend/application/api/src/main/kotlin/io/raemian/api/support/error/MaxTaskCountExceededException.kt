package io.raemian.api.support.error

class MaxTaskCountExceededException : CoreApiException(
    ErrorInfo.MAX_TASK_COUNT_EXCEEDED_EXCEPTION,
)
