def call(Map config = [:]) {
    sshagent(["$config.credentialId"]) {
        String sshCommand = "ssh -o StrictHostKeyChecking=no -l $config.user $config.ip";
        String dockerComposeFileName = "docker-compose-springapp.yml";

        // Snapshot stable container before clean up
        sh "$sshCommand 'docker commit $config.container $config.privateHost/$config.image:stable' || true"

        // Copy docker compose file to host
        sh "scp $dockerComposeFileName $config.user@$config.ip:$config.hostDir"

        // Login to private Docker
        withCredentials([usernamePassword(credentialsId: config.nexusCredentialId, passwordVariable: 'p', usernameVariable: 'u')]) {
            sh "$sshCommand 'docker login -u=$u -p=$p $config.privateHost'"
        }

        // Deploy application
        sh """
            $sshCommand ' export TAG=$config.privateHost/$config.image:$config.version \
            export CONTEXT_PATH=$config.contextPath \
            export CONTAINER_NAME=$config.container \
            export HOST_PORT=$config.hostPort \
            export CONTAINER_PORT=$config.containerPort \
            export HOST_DIR=$config.hostDir \
            export CONTAINER_DIR=$config.containerDir \
            && docker compose -f $config.hostDir/$dockerComposeFileName down \
            && docker compose -f $config.hostDir/$dockerComposeFileName up -d'
        """
    }
}