def call(Map config = [:]) {
    withCredentials([usernamePassword(credentialsId: config.credentialId, passwordVariable: 'p', usernameVariable: 'u')]) {
        sh "docker login -u=$u -p=$p $config.privateHost"
        sh "docker push $config.privateHost/$config.image:$config.version"
    }
}