def call(config) {
    String image = "${config.dockerUser}/${config.appName}:dev-latest"

    stage('Deploy to Development Server with Dev tag') {

        withCredentials([
            string(credentialsId: 'dls-db-password', variable: 'DB_PASSWORD'),
            string(credentialsId: 'dls-jwt-secret', variable: 'JWT_SECRET')
        ]) {
            sshagent(['development-srv']) {
                sh"""
                    ssh -o StrictHostKeyChecking=no ${config.devServer} \
                    'sudo docker pull ${image} && 
                    
                    sudo docker stop ${config.appName}-dev || true && 
                    sudo docker rm ${config.appName}-dev || true &&
                    
                    sudo docker run -d -p ${config.devPort}:${config.containerPort} \
                    --name ${config.appName}-dev \
                    --restart unless-stopped \
                    -e SPRING_PROFILES_ACTIVE=dev \
                    -e DB_PASSWORD="${DB_PASSWORD}" \
                    -e JWT_SECRET="${JWT_SECRET}" \
                    ${image}'
                """
            }
        }
    }
}

return this