def call() {

    def appName = "data-labeling-be"
    def release = "1.1"
    def dockerUser = "fleeforezz"

    def image = "${dockerUser}/${appName}"
    def version = "${release}.${env.BUILD_NUMBER}"

    def imageTagged = env.BRANCH_NAME == "main" ?
        "${image}:latest" :
        "${image}:${version}-beta"

    stage('Docker Build') {

        echo "Building Docker image..."

        docker.build("${image}:${version}")

    }

    stage('Docker Test') {

        script {

            def containerName = "test-${appName}-${env.BUILD_NUMBER}"
            def testPort = "8081"

            sh """
                docker run -d --name ${containerName} \
                -p ${testPort}:${testPort} ${imageTagged}

                echo "Waiting for Spring Boot health check..."

                ATTEMPTS=40
                SLEEP=3

                for i in \$(seq 1 \$ATTEMPTS); do
                    if curl -fs http://localhost:${testPort}/actuator/health > /dev/null; then
                        echo "App is UP"
                        break
                    fi

                    echo "Attempt \$i/\$ATTEMPTS"
                    sleep \$SLEEP
                done

                curl -f http://localhost:${testPort}/actuator/health

                docker stop ${containerName}
                docker rm ${containerName}
            """
        }

    }

    stage('Push Docker Image') {

        withDockerRegistry(
            credentialsId: 'Docker_Login',
            url: 'https://index.docker.io/v1/'
        ) {

            def dockerImage = docker.image("${image}:${version}")

            if (env.BRANCH_NAME == "main") {

                dockerImage.push("latest")
                dockerImage.push("v${version}")

            } else {

                dockerImage.push("${version}-beta")
                dockerImage.push("dev-latest")

            }

        }

    }

}

return this