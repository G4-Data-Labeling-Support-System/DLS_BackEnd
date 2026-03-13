node {
    
    def buildPipeline = load "ci/build.groovy"
    def dockerPipeline = load "ci/docker.groovy"

    stage('Checkout') {
        checkout scm
    }

    stage('Build') {
        buildPipeline.call()
    }

    stage('Docker Build & Push') {
        dockerPipeline.call()
    }

    if (env.BRANCH_NAME == "main") {
        def deployProd = load "ci/deploy-prod.groovy"

        stage('Deloy Production') {
            deployProd.call()
        }
    } else {
        def deployDev = load "ci/deploy-dev.groovy"

        stage('Deloy Development') {
            deployDev.call()
        }
    }

}