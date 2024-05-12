def call(Map config = [:]) {
    sshagent(["$config.credentialId"]) {
        String sshCommand = "ssh -o StrictHostKeyChecking=no -l $config.user $config.ip";
        String dockerComposeFileName = "docker-compose-reverseproxy.yml";
        String nginxConf = "nginx-reverseproxy.conf";

        // Copy files to host
        sh "scp $dockerComposeFileName $config.user@$config.ip:$config.hostDir"
        sh "scp $nginxConf $config.user@$config.ip:$config.hostDir"

        // Clean up nginx reverse proxy
        sh "$sshCommand 'docker compose -f $config.hostDir/$dockerComposeFileName down'"

        // Deploy nginx reverse proxy
        sh "$sshCommand 'docker compose -f $config.hostDir/$dockerComposeFileName up -d'"
    }
}