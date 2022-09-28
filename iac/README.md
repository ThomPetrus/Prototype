# Welcome to your CDK Java project!

This is a blank project for CDK development with Java.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java IDE to build and run tests.

## Useful commands

* `cdk bootstrap`    bootstrap environment if not done yet - will see warning if not completed, otherwise skip.
 * `mvn package`     compile and run tests
 * `cdk ls`          list all stacks in the app
 * `cdk synth`       emits the synthesized CloudFormation template
 * `cdk deploy`      deploy this stack to your default AWS account/region
 * `cdk diff`        compare deployed stack with current state
 * `cdk docs`        open CDK documentation


Deploying Prototype
---
```
# deploy image to ECR
cd /service && ./app.sh deploy-ecr

# deploy infra using image from ECR
cd /iac && ./iac.sh deploy 
```

Enjoy!
