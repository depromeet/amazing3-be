package io.raemian.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ApplicationEventMulticaster
import org.springframework.context.event.SimpleApplicationEventMulticaster
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@Configuration
class EventConfig {
    @Bean("applicationEventMulticaster")
    fun applicationEventMulticaster(): ApplicationEventMulticaster {
        val eventMulticaster = SimpleApplicationEventMulticaster()
        eventMulticaster.setTaskExecutor(asyncEventExecutor())
        return eventMulticaster
    }

    // EventListener 메서드 처리하는 ThreadPool
    private fun asyncEventExecutor(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.setCorePoolSize(10)
        executor.setMaxPoolSize(10)
        executor.setQueueCapacity(10000)
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setAwaitTerminationSeconds(10)
        executor.initialize()
        return executor
    }
}
