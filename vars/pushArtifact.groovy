def call(Map config = [:]) {
    withCredentials([usernameColonPassword(credentialsId: config.credentialId, variable: 'NEXUS_CREDENTIAL')]) {
        sh """
            curl -v -u \$NEXUS_CREDENTIAL "$config.url/service/rest/v1/components?repository=$config.repository" \
            -F "maven2.groupId=$config.groupId" \
            -F "maven2.artifactId=$config.artifactId" \
            -F "version=$config.version" \
            -F "maven2.asset1=@$config.file" \
            -F "maven2.asset1.extension=$config.extension"
        """
    }
}