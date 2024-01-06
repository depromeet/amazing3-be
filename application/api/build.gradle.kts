tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":infra:metrics"))
    implementation(project(":infra:logging"))
    implementation(project(":storage:db-core"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    /* jwt */
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    /* security */
    implementation("org.springframework.boot:spring-boot-starter-security")

    /* oauth-client */
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")

    /* test */
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
    testImplementation("io.rest-assured:spring-mock-mvc")

    /* swagger */
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}
