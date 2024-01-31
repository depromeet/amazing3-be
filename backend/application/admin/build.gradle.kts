tasks.getByName("bootJar") {
    enabled = true
}

tasks.getByName("jar") {
    enabled = false
}

dependencies {
    implementation(project(":storage:db-core"))
    implementation(project(":storage:image"))
    implementation(project(":infra:logging"))

    implementation("org.springframework.boot:spring-boot-starter-web")

    /* swagger */
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}
