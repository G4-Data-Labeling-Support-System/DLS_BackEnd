node {

    def buildStatus = 'SUCCESS'
    def slackNotify

    try {
        stage('Checkout') {
            checkout scm
        }

        // Configs
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
            
            devServer: 'jso@192.168.1.74',
            prodServer: 'jso@192.168.1.23',

            manifestRepo: 'https://github.com/G4-Data-Labeling-Support-System/Infrastructure.git',
            // env: '${env.BRANCH_NAME == 'main' ? 'production' : 'development'}',
            // k8sNamespace: '${env.BRANCH_NAME == 'main' ? 'prod' : 'dev'}'
        ]

        // Env variables
        slackNotify = load "ci/slack.groovy"
        def buildPipeline = load "ci/build.groovy"
        def sonarqubePipeline = load "ci/sonarqube.groovy"
        def trivyFilesystemScan = load "ci/trivy-filesystem-scan.groovy"
        def dockerPipeline = load "ci/docker.groovy"

        def deployProd = load "ci/deploy-prod.groovy"
        def deployBeta = load "ci/deploy-beta.groovy"
        def deployDev = load "ci/deploy-dev.groovy"

        def updateManifest = load "ci/update-manifest.groovy"

        // Call functions base on branch
        if (env.BRANCH_NAME == "main") {
            // buildPipeline.call(config)
            // sonarqubePipeline.call(config)
            trivyFilesystemScan.call()
            dockerPipeline.call(config)
            deployProd.call(config)
            // updateManifest.call(config)
        } else if (env.BRANCH_NAME == "development") {
            // buildPipeline.call(config)
            // sonarqubePipeline.call(config)
            trivyFilesystemScan.call()
            dockerPipeline.call(config)
            deployBeta.call(config)
            // updateManifest.call(config)
        } else {
            // buildPipeline.call(config)
            dockerPipeline.call(config)
            deployDev.call(config)
        }
    }
    catch (err) {
        buildStatus = 'FAILURE'
        currentBuild.result = 'FAILURE'
        throw err // keep pipeline failed
    } finally {
        if (slackNotify != null) {
            slackNotify.call(buildStatus)
        } else {
            echo "Slack notify not loaded"
        }

        // Clean up workspace after run the pipeline
        stage('Cleanup') {
            cleanWs()

            // Docker cleanup
            // sh"""
            //     docker rmi ${DOCKER} 
            // """
        }
    }

}