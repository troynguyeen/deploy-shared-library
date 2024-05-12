def call(Map config = [:]) {
    ansiblePlaybook (
        become: true, 
        colorized: true, 
        credentialsId: config.ansibleCredentialId,
        playbook: config.playbook,
        inventory: config.inventory,
        extraVars: [
            'nginx_server': config.nginx_host,
            'app_server': "$config.protocol://$config.app_host:$config.port"
        ]
    )
}