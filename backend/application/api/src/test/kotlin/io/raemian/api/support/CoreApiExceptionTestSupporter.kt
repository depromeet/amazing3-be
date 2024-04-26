package io.raemian.api.support

import io.raemian.api.support.exception.CoreApiException
import io.raemian.api.support.exception.ErrorInfo

class CoreApiExceptionTestSupporter {

    companion object {
        fun assertThrowsCoreApiExceptionExactly(
            expectedErrorInfo: ErrorInfo,
            shouldRaiseCoreApiException: () -> Any,
        ) {
            try {
                shouldRaiseCoreApiException()
                throw RuntimeException("아무 예외도 발생하지 않았습니다.")
            } catch (coreApiException: CoreApiException) {
                validateErrorInfo(coreApiException, expectedErrorInfo)
            } catch (exception: Exception) {
                throw RuntimeException("CoreApiException이 아닌 예와가 발생했습니다.")
            }
        }

        private fun validateErrorInfo(
            coreApiException: CoreApiException,
            expectedErrorInfo: ErrorInfo,
        ) {
            if (coreApiException.errorInfo != expectedErrorInfo) {
                throw RuntimeException("CoreApiException이 발생했으나, 예상한 ErorrInfo와 다릅니다.")
            }
        }
    }
}
