return this

node {

    def APP_NAME = "data-labeling-be"
    def SERVER = "jso@10.0.1.12"
    def IMAGE = "fleeforezz/${APP_NAME}:dev-latest"

    stage('Deploy to Development Server') {
        
        sshagent(['development-srv']) {

            sh """
                ssh -o StrictHostKeyChecking=no ${SERVER_CONNECTION} \
                'sudo docker stop ${APP_NAME} || true && sudo docker rm ${APP_NAME} || true'
            """

            sh """
                ssh -o StrictHostKeyChecking=no ${SERVER_CONNECTION} \
                'sudo docker run -d -p 8081:8081 \
                --name ${APP_NAME} \
                --restart unless-stopped \
                ${IMAGE}'
            """
        }
    }

}