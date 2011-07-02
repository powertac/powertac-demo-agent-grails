
quartz {
    autoStartup = true
    jdbcStore = false
    waitForJobsToCompleteOnShutdown = false
}

environments {
    test {
        quartz {
            autoStartup = false
        }
    }
}
