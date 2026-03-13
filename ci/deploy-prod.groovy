def call(config) {
    String image = "${config.dockerUser}/${config.appName}"
    String version = "${config.release}-release.${env.BUILD_NUMBER}b"
    String imageTagged = "${image}:${version}"

    stage('Deploy to Production Server') {

        sshagent(['development-srv']) {
            sh"""
                ssh -o StrictHostKeyChecking=no ${config.devServer} \
                'echo "Deploying to version ${version}"

                sudo docker pull ${imageTagged} && 

                sudo docker stop ${config.appName} || true && 
                sudo docker rm ${config.appName} || true &&

                sudo docker run -d -p ${config.prodPort}:${config.port} \
                --name ${config.appName} \
                --restart unless-stopped \
                ${imageTagged}'
            """
        }
    }
}

return this