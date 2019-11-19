properties([
    buildDiscarder(
        logRotator(
            artifactDaysToKeepStr: '14',
            artifactNumToKeepStr: '5',
            daysToKeepStr: '14',
            numToKeepStr: '10'
        )
    ),
    parameters([
        choice(
            name: 'BRANCH',
            choices: 'master\nstable\nrelease',
            description: 'Choise master, stable, release'
        )
    ]),
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
node(){
    def workDir = sh(returnStdout: true, script: "pwd").trim()
    stage('CHEKOUT'){ 
		currentBuild.displayName = "#${BUILD_NUMBER} text1 ${BRANCH}"
        //sh"sudo chmod -R 777 ."
		ansiColor('xterm') {
            printlnGreen "ttexttt"
        }
        deleteDir()
        checkout scm
		
		sh"pwd && ls -lh"
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
def printlnGreen(text) {
    println "\033[1;4;37;42m$text\033[0m"
}
