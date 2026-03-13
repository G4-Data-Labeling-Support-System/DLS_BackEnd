def call() {

    stage('Maven Build') {
        echo "Running Maven Build..."

        sh '''
            mvn clean package -DskipTests
        '''
    }
}

return this