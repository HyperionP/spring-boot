node(){
    def workDir = sh(returnStdout: true, script: "pwd").trim()
    stage('CHEKOUT'){ 
        //sh"sudo chmod -R 777 ."
        deleteDir()
        
        // withCredentials([usernamePassword(credentialsId: '0765f4d3-40d9-4d02-a696-5891d171ac31', passwordVariable: 'password', usernameVariable: 'name')]){
            // sh"git clone https://$name:$password@github.com/HyperionP/spring-boot.git"
        // }
        
    }
    stage('BUILD'){
    //sh "cd $workDir && cd ./spring-boot/spring-boot-project/spring-boot && ls -lh && sudo mvn clean install"
    }
}
//input("Approve to continue")
node(){
     stage('save'){
       // archiveArtifacts artifacts: "**/target/*.jar", fingerprint: true
    }
    
}
