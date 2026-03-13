node {
    
    stage('Checkout') {
        checkout scm
    }

    def config = [
        appName: 'data-labeling-be',
        dockerUser: 'fleeforezz',
        release: '1.0.0',
        alpha: '1.0.0',
        port: '8081',
        devServer: "jso@10.0.1.74"
    ]

    load "ci/init.groovy"
    load "ci/build.groovy"
    load "ci/docker.groovy"

    // Call functions
    initPipeline.call()
    buildPipeline.call(config)
    dockerPipeline.call(config)

    // Deploy base on branch
    if (env.BRANCH_NAME == "main") {
        load "ci/deploy-prod.groovy"
        deployProd.call(config)
    } else {
        load "ci/deploy-dev.groovy"
        deployDev.call(config)
    }
}