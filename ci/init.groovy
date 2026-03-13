def call() {
    stage('Clean up Workspace') {
        cleanWs()
    }

    // stage('Github Checkout') {
    //     git branch: "${env.BRANCH_NAME}",
    //         credentialsId: 'github-credentials',
    //         url: "${GITHUB_URL}"
    // }
}

return this