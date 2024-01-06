dependencies {
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.sentry:sentry-logback:${property("sentryVersion")}")

    implementation("com.slack.api:slack-api-model-kotlin-extension:1.36.1")
    implementation("com.slack.api:slack-api-client-kotlin-extension:1.36.1")
    implementation("com.slack.api:slack-api-client:1.36.1")
}
