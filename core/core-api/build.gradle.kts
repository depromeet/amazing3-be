tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":core:core-enum"))
    implementation(project(":support:monitoring"))
    implementation(project(":support:logging"))
    implementation(project(":storage:db-core"))
    implementation(project(":clients:client-example"))

    testImplementation(project(":tests:api-docs"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")
    // oauth-client
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    // security-test
    testImplementation("org.springframework.security:spring-security-test")



}
