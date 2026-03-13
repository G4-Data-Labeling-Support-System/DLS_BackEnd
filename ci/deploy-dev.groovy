def call() {

    String appName = 'data-labeling-be'
    String serverConnection = 'jso@10.0.1.74'
    String image = "fleeforezz/${appName}:dev-latest"

    stage('Deploy to Development Server') {

        sshagent(['development-srv']) {

            sh """
                ssh -o StrictHostKeyChecking=no ${serverConnection} \
                'sudo docker stop ${appName} || true && sudo docker rm ${appName} || true'
            """

            sh """
                ssh -o StrictHostKeyChecking=no ${serverConnection} \
                'sudo docker run -d -p 8081:8081 \
                --name ${appName} \
                --restart unless-stopped \
                ${image}'
            """
        }

    }
}

return this