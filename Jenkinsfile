node {

    def config = [
        appName: 'data-labeling-be',
        dockerUser: 'fleeforezz',
        release: '1.0.0',
        alpha: '1.0.0',
        port: '8081',
        devServer: "jso@10.0.1.74"
    ]
    // def initPipeline = load "ci/init.groovy"
    def buildPipeline = load "ci/build.groovy"
    def dockerPipeline = load "ci/docker.groovy"

    // Call functions
    // initPipeline.call()
    buildPipeline.call(config)
    dockerPipeline.call(config)

    // Deploy base on branch
    if (env.BRANCH_NAME == "main") {
        def deployProd = load "ci/deploy-prod.groovy"
        deployProd.call(config)
    } else {
        def deployDev = load "ci/deploy-dev.groovy"
        deployDev.call(config)
    }

    // Clean up workspace after run the pipeline
    stage('Cleanup') {
        cleanWs()
    }
}