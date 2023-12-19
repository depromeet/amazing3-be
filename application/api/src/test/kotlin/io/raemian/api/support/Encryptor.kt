package io.raemian.api.support

import io.raemian.api.config.EncryptorConfig
import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test

class Encryptor {

    /**
     * @Author 이진호
     * @see 암호화를_하는_방법은_아래와_같습니다 (https://github.com/depromeet/amazing3-be/pull/49)
     * 1. 사용할 yaml의 최상위 파일에 아래 코드를 추가합니다.
     * ```
     * jasypt:
     *   encryptor:
     *     bean: stringEncryptor
     *   secret-key: ${JASYPT_ENCRYPTION_PASSWORD}
     * ```
     *
     * 2. password 변수 값에 password를 대입한다. (빈으로 관리하고 싶지 않아 자동 주입 넣지 않았습니다.)
     * 3. encrypt 메서드에 ","로 구분해 암호화할 String 값을 넣으세요.
     * 4. ENC() 가 포함된 전체 결과를 기존 값 대신에 대입하세요.
     *
     *
     * @see 주의 : 순수 숫자는 안 됩니다.
     * */

    val password = "enter password"

    @Test
    fun encrypt() {
        // 암호화 할 값을 대입해세요
        encrypt("enter", "password")
    }

    fun encrypt(vararg string: String) {
        val encryptor: StringEncryptor = EncryptorConfig()
            .stringEncryptor(password)

        string.forEach {
            println("$it 암호화 -> ENC(${encryptor.encrypt(it)})")
        }
    }
}
