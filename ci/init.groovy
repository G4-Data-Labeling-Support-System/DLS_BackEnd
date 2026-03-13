def call() {
    stage('Clean up Workspace') {
        cleanWs()
    }
}

return this