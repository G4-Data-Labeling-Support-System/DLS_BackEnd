node {

    def buildStatus = 'SUCCESS'

    try {
        stage('Checkout') {
            checkout scm
        }

        // Env var
        def config = [
            appName: 'data-labeling-be',
            dockerUser: 'fleeforezz',

            release: '1.0.0',
            beta: '1.0.0',
            
            containerPort: '8081',
            testPort: '8081',
            devPort: '8082',
            betaPort: '8083',
            prodPort: '8084',
            
            devServer: "jso@10.0.1.74",
            prodServer: "jso@10.0.1.23"
        ]
        def slackNotify = load "ci/slack.groovy"
        def buildPipeline = load "ci/build.groovy"
        def sonarqubePipeline = load "ci/sonarqube.groovy"
        def trivyFilesystemScan = load "ci/trivy-filesystem-scan.groovy"
        def dockerPipeline = load "ci/docker.groovy"

        // Call functions base on branch
        if (env.BRANCH_NAME == "main") {
            buildPipeline.call(config)
            sonarqubePipeline.call(config)
            trivyFilesystemScan.call()
            dockerPipeline.call(config)
        } else if (env.BRANCH_NAME == "development") {
            buildPipeline.call(config)
            sonarqubePipeline.call(config)
            trivyFilesystemScan.call()
            dockerPipeline.call(config)
        } else {
            buildPipeline.call(config)
            dockerPipeline.call(config)
        }

        // Deploy base on branch
        if (env.BRANCH_NAME == "main") {
            def deployProd = load "ci/deploy-prod.groovy"
            deployProd.call(config)
        } else if (env.BRANCH_NAME == "development") {
            def deployBeta = load "ci/deploy-beta.groovy"
            deployBeta.call(config)
        } else {
            def deployDev = load "ci/deploy-dev.groovy"
            deployDev.call(config)
        }
    }
    catch (err) {
        buildStatus = 'FAILURE'
        currentBuild.result = 'FAILURE'
        throw err // keep pipeline failed
    } finally {
        slackNotify.call(buildStatus)

        // Clean up workspace after run the pipeline
        stage('Cleanup') {
            cleanWs()
        }
    }

}