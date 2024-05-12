def call(Map config = [:]) {
    sh "docker build -t $config.privateHost/$config.image:$config.version ."
}