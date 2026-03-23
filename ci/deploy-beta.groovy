def call(config) {
    String image = "${config.dockerUser}/${config.appName}"
    String version = "${config.beta}-beta.${env.BUILD_NUMBER}b"
    String imageTagged = "${image}:${version}"

    stage('Deploy to Development Server with Beta tag') {
        withCredentials([
            string(credentialsId: 'dls-db-password', variable: 'DB_PASSWORD'),
            string(credentialsId: 'dls-jwt-secret', variable: 'JWT_SECRET')
        ]) {
            sshagent(['development-srv']) {
                sh"""
                    ssh -o StrictHostKeyChecking=no ${config.devServer} \
                    'sudo docker pull ${imageTagged} && 
                    
                    sudo docker stop ${config.appName}-beta || true && 
                    sudo docker rm ${config.appName}-beta || true &&
                    
                    sudo docker run -d -p ${config.betaPort}:${config.containerPort} \
                    --name ${config.appName}-beta \
                    --restart unless-stopped \
                    -e SPRING_PROFILES_ACTIVE=dev \
                    -e DB_PASSWORD="${DB_PASSWORD}" \
                    -e JWT_SECRET="${JWT_SECRET}" \
                    ${image}"
                """
            }
        }
    }
}

return this