def call(Map config = [:]) {
    retry (5) {
        sleep 5
        httpRequest url:"$config.protocol://$config.app_host:$config.port${config.contextPath}/actuator/health", validResponseCodes: '200', validResponseContent: '"status":"UP"'
    }
}
