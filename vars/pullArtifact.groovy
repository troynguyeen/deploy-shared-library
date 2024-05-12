def call(Map config = [:]) {
    String username;
    String password;
    withCredentials([usernamePassword(credentialsId: config.credentialId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        username = USERNAME;
        password = PASSWORD;
    }
    ansiblePlaybook (
        become: true, 
        colorized: true, 
        credentialsId: config.ansibleCredentialId,
        playbook: config.playbook,
        inventory: config.inventory,
        extraVars: [
            'artifact_url': config.artifactUrl,
            'username_nexus': username,
            'password_nexus': password
        ]
    )
}