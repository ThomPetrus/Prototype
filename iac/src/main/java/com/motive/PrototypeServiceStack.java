package com.motive;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.applicationautoscaling.EnableScalingProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ecr.IRepository;
import software.amazon.awscdk.services.ecr.Repository;
import software.amazon.awscdk.services.ecs.*;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedFargateService;
import software.amazon.awscdk.services.ecs.patterns.ApplicationLoadBalancedTaskImageOptions;
import software.amazon.awscdk.services.elasticloadbalancingv2.HealthCheck;
import software.amazon.awscdk.services.logs.LogGroup;
import software.constructs.Construct;

public class PrototypeServiceStack extends Stack {
    public PrototypeServiceStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        // Get the deployment environment from an environment variable named "ENV"
        String environment = System.getenv("ENV");

        // This will create a VPC with a NAT-gateway, IGW, default routes and route tables, and a private and public subnet in all availability zones
        Vpc vpc = new Vpc(this, "PrototypeVpc");

        // Create the ECS cluster
        Cluster cluster = Cluster.Builder.create(this, "PrototypeCluster").vpc(vpc).build();

        // Create a load-balanced Fargate service and make it public
        ApplicationLoadBalancedFargateService fargateService = ApplicationLoadBalancedFargateService.Builder.create(this, "PrototypeService")
                .cluster(cluster)
                .cpu(1024) // Default is 256, but for a Spring boot app, 1024 is the minimum if we want the app to start within a minute
                .memoryLimitMiB(2048) // Default is 512
                .desiredCount(2) // Default is 1
                .taskImageOptions(ApplicationLoadBalancedTaskImageOptions.builder()
                        // We want to use the DockerFile of our Spring boot App. In this example, the Dockerfile is located in the `service` directory
                        // This will create an ECR repository and upload the docker image to that repository
                        .image(ContainerImage.fromEcrRepository(Repository.fromRepositoryName(this, "motive/prototype","motive/prototype")))
                        .containerPort(8080)// The default is port 80, The Spring boot default port is 8010
                        .enableLogging(true)
                        .build())
                .publicLoadBalancer(true) // Default is false
                .assignPublicIp(false) // If set to true, it will associate the service to a public subnet
                .build();

        // For our Spring boot application, we need to configure the health check
        fargateService.getTargetGroup().configureHealthCheck(HealthCheck.builder()
                .healthyHttpCodes("200") // Specify which http codes are considered healthy
                // The load balancer REQUIRES a healthcheck endpoint to determine the state of the app.
                // In this example, we're using the Spring Actuator. Configure this in your app if missing.
                .path("/actuator/health")
                .port("8080") // The default is port 80
                .build());

        // Configure auto scaling capabilities only when our environment equals "prd"
        if(environment != null && !environment.isEmpty() && environment.equals("prd")) {
            // Configure the service auto-scaling
            ScalableTaskCount scalableTask = fargateService.getService().autoScaleTaskCount(EnableScalingProps.builder()
                    .minCapacity(2)
                    .maxCapacity(6)
                    .build()
            );

            // Scale based on the CPU utilization
            scalableTask.scaleOnCpuUtilization("PrototypeCpuBasedScaling", CpuUtilizationScalingProps.builder()
                    .targetUtilizationPercent(50) // Scale when the CPU utilization is at 50%
                    .build()
            );
        }
    }

}
