def call(config) {
    stage('Maven Build') {
        echo "Building ${config.appName}..."

        script {
            String mavenHome = tool 'maven'
            withEnv(["PATH+MAVEN=${mavenHome}/bin"]) {
                sh '''
                    mvn -version
                    mvn clean package -DskipTests
                '''
            }
        }
    }
}

return this