properties([
    buildDiscarder(
        logRotator(
            artifactDaysToKeepStr: '14',
            artifactNumToKeepStr: '5',
            daysToKeepStr: '14',
            numToKeepStr: '10'
        )
    ),
    
    pipelineTriggers([
        GenericTrigger(
            causeString: 'Push to master', 
            genericVariables: [[
                defaultValue: '',
                key: 'ref', 
                regexpFilter: '', 
                value: '$.ref'
            ]], 
            printContributedVariables: true, 
            printPostContent: true, 
            regexpFilterExpression: 'master$', 
            regexpFilterText: '$ref', 
            silentResponse: true, 
            token: '4092480392482342'
        )
    ])
])
def server = Artifactory.newServer url: 'http://35.228.43.230:8081/artifactory', credentialsId: '9b898bd5-1092-42b9-a0c2-1b0dc9ca9bb2'
node(){
    
    def workDir = sh(returnStdout: true, script: "pwd").trim()
    stage('CHEKOUT'){ 
        sh"sudo chmod -R 777 ."
        deleteDir()
		
        withCredentials([usernamePassword(credentialsId: 'aa0815bb-af35-4940-a3a8-b763ab24c501', passwordVariable: 'password', usernameVariable: 'name')]){
            sh"git clone https://$name:$password@github.com/HyperionP/spring-boot.git"
        }
        
    }
    stage('BUILD'){
    sh "cd $workDir && cd ./spring-boot/spring-boot-tests/spring-boot-smoke-tests/spring-boot-smoke-test-web-ui/ && ls -lh && sudo mvn clean install"
    }
    //input("Approve to continue")
     stage('UPLOAD ARTIFACT'){
         sh" cd $workDir && cd ./spring-boot/spring-boot-tests/spring-boot-smoke-tests/spring-boot-smoke-test-web-ui/target && sudo cp spring-boot-smoke-test-web-ui-2.2.0.BUILD-SNAPSHOT.jar ./spring-boot.latest.jar && sudo mv spring-boot-smoke-test-web-ui-2.2.0.BUILD-SNAPSHOT.jar ./spring-boot.${BUILD_NUMBER}.jar" 
        archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
        def uploadSpec = """{
  "files": [
    {
      "pattern": "**/target/*.jar",
      "target": "generic-local/"
    }
 ]
}"""
server.upload spec: uploadSpec
//ansiblePlaybook colorized: true, playbook: '$workDir/spring-boot/deploy_artf.yml'         
     }
     stage('CI DEPLOY'){
	     sh"sudo -S su - parasitchmax -c 'ansible-playbook deploy_artf.yml'"
		 sh"sudo -S su - parasitchmax -c 'ansible-playbook deploy_docker.yml -e varTag=${BUILD_NUMBER}'"
}
}
