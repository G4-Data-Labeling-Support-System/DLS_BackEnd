def call(config) {
    stage('Maven Build') {
        echo 'Running Maven Build...'

        script {
            String mavenHome = tool 'maven'
            withEnv(["PATH+MAVEN=${mavenHome}/bin"]) {
                dir("DLS_BE_development")
                sh '''
                    mvn -version
                    mvn clean package -DskipTests
                '''
            }
        }
    }
}

return this